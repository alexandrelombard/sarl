/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2019 the original authors or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sarl.maven.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import javax.inject.Provider;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.utils.io.DirectoryScanner;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;
import org.apache.maven.toolchain.ToolchainPrivate;
import org.apache.maven.toolchain.java.JavaToolchain;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import io.sarl.lang.SARLStandaloneSetup;
import io.sarl.lang.compiler.batch.IJavaBatchCompiler;
import io.sarl.lang.compiler.batch.OptimizationLevel;
import io.sarl.lang.compiler.batch.SarlBatchCompiler;

/** Abstract mojo that is able to use the SARL batch compiler.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSarlBatchCompilerMojo extends AbstractSarlMojo {

	private Injector injector;

	private Provider<SarlBatchCompiler> sarlBatchCompilerProvider;

	private ReflectExtensions reflect;

	@Component
	private ToolchainManager toolchainManager;

	@Parameter(readonly = true, defaultValue = "${basedir}/.settings/io.sarl.lang.SARL.prefs")
	private String propertiesFileLocation;

	@Override
	public void prepareExecution() throws MojoExecutionException {
		if (this.injector == null) {
			final Injector mainInjector = SARLStandaloneSetup.doSetup();
			this.injector = mainInjector.createChildInjector(Arrays.asList(new MavenPrivateModule()));
		}
		if (this.sarlBatchCompilerProvider == null) {
			this.sarlBatchCompilerProvider = this.injector.getProvider(SarlBatchCompiler.class);
		}
		if (this.reflect == null) {
			this.reflect = this.injector.getInstance(ReflectExtensions.class);
		}
		if (this.sarlBatchCompilerProvider == null || this.reflect == null) {
			throw new MojoExecutionException(Messages.AbstractSarlBatchCompilerMojo_0);
		}
	}

	/** Replies the batch compiler for SARL.
	 *
	 * @return the batch compiler.
	 */
	protected SarlBatchCompiler getBatchCompiler() {
		return this.sarlBatchCompilerProvider.get();
	}

	/** Replies the current project.
	 *
	 * @return the current project.
	 */
	protected MavenProject getProject() {
		return this.mavenHelper.getSession().getCurrentProject();
	}

	/** Replies the version of the source.
	 *
	 * @return the source version.
	 */
	protected abstract String getSourceVersion();

	/** Replies the encoding of the source.
	 *
	 * @return the encoding.
	 */
	protected abstract String getEncoding();

	/** Replies if the inline annotations must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the inline annotations.
	 */
	protected abstract boolean getGenerateInlines();

	/** Replies if the pure annotations must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the pure annotations.
	 */
	protected abstract boolean getGeneratePures();

	/** Replies if the trace files must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the trace files.
	 */
	protected abstract boolean getGenerateTraceFiles();

	/** Replies if the storage files must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the storage files.
	 */
	protected abstract boolean getGenerateStorageFiles();

	/** Replies if the equality test functions must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the functions.
	 * @since 0.8
	 */
	protected abstract boolean getGenerateEqualityTestFunctions();

	/** Replies if the toString functions must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the functions.
	 * @since 0.8
	 */
	protected abstract boolean getGenerateToStringFunctions();

	/** Replies if the clone functions must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the functions.
	 * @since 0.8
	 */
	protected abstract boolean getGenerateCloneFunctions();

	/** Replies if the serial number fields must be generated by the SARL compiler.
	 *
	 * @return <code>true</code> for generating the fields.
	 * @since 0.8
	 */
	protected abstract boolean getGenerateSerialNumberFields();

	/** Replies the list of the extra-language generators' identifiers that should be enabled.
	 *
	 * @return the list of extra-language generators' identifiers.
	 * @since 0.8
	 */
	protected abstract String[] getExtraGenerators();

	/** Replies the Java compiler to use.
	 *
	 * @return the Java compiler, never {@code null}.
	 * @since 0.8
	 */
	protected abstract JavaCompiler getJavaCompiler();

	/** Replies the optimization level to use.
	 *
	 * @return the optimization level, never {@code null}.
	 * @since 0.8
	 */
	protected abstract OptimizationLevel getOptimization();

	/** Replies if the mojo is used within a test code compilation context.
	 *
	 * @return {@code true} if this mojo is used within a test phase.
	 * @since 0.8
	 */
	protected abstract boolean isTestContext();

	/** Run compilation.
	 *
	 * @param classPath the classpath
	 * @param sourcePaths the source paths.
	 * @param sarlOutputPath the output path for receiving the SARL code.
	 * @param classOutputPath the output path for receiving the Java class files.
	 * @throws MojoExecutionException if error.
	 * @throws MojoFailureException if failure.
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	protected void compile(List<File> classPath, List<File> sourcePaths, File sarlOutputPath,
			File classOutputPath) throws MojoExecutionException, MojoFailureException {
		final SarlBatchCompiler compiler = getBatchCompiler();
		final MavenProject project = getProject();
		compiler.setResourceSetProvider(new MavenProjectResourceSetProvider(project));
		final Iterable<File> filtered = Iterables.filter(sourcePaths, input -> input.isDirectory());
		if (Iterables.isEmpty(filtered)) {
			final String dir = Iterables.toString(sourcePaths);
			getLog().info(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_1, dir));
			return;
		}
		final String baseDir = project.getBasedir().getAbsolutePath();
		final JavaCompiler compilerType = getJavaCompiler();
		compiler.setJavaPostCompilationEnable(compilerType != JavaCompiler.NONE);
		compiler.setOptimizationLevel(getOptimization());
		compiler.setClassOutputPath(classOutputPath);
		compiler.setJavaSourceVersion(getSourceVersion());
		compiler.setBasePath(baseDir);
		compiler.setTempDirectory(getTempDirectory());
		compiler.setDeleteTempDirectory(false);
		compiler.setClassPath(classPath);
		final String bootClassPath = getBootClassPath();
		compiler.setBootClassPath(bootClassPath);
		final List<File> filteredSourcePaths = Lists.newArrayList(filtered);
		compiler.setSourcePath(filteredSourcePaths);
		compiler.setOutputPath(sarlOutputPath);
		compiler.setFileEncoding(getEncoding());
		compiler.setWriteTraceFiles(getGenerateTraceFiles());
		compiler.setWriteStorageFiles(getGenerateStorageFiles());
		compiler.setGenerateInlineAnnotation(getGenerateInlines());
		compiler.setGeneratePureAnnotation(getGeneratePures());
		compiler.setGenerateEqualityTestFunctions(getGenerateEqualityTestFunctions());
		compiler.setGenerateToStringFunctions(getGenerateToStringFunctions());
		compiler.setGenerateCloneFunctions(getGenerateCloneFunctions());
		compiler.setGenerateSerialNumberFields(getGenerateSerialNumberFields());

		final StringBuilder builder = new StringBuilder();
		for (final String identifier : getExtraGenerators()) {
			if (builder.length() > 0) {
				builder.append(File.pathSeparator);
			}
			builder.append(identifier);
		}
		compiler.setExtraLanguageGenerators(builder.toString());

		StaticLoggerBinder.getSingleton().registerMavenLogger(getLog());
		final Logger logger = LoggerFactory.getLogger(getClass());
		compiler.setLogger(logger);
		compiler.setIssueMessageFormatter((issue, uriToProblem) -> {
			final String filename;
			if (uriToProblem != null) {
				filename = uriToProblem.toFileString();
			} else {
				filename = Messages.AbstractSarlBatchCompilerMojo_2;
			}
			return MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_3,
					filename, issue.getLineNumber(),
					issue.getColumn(), issue.getMessage());
		});
		final String[] errorMessage = new String[] {null};
		compiler.addIssueMessageListener((issue, uri, message) -> {
			if ((issue.isSyntaxError() || issue.getSeverity() == Severity.ERROR) && (Strings.isEmpty(errorMessage[0]))) {
				errorMessage[0] = message;
			}
		});
		if (!compiler.compile()) {
			final StringBuilder dir = new StringBuilder();
			for (final File file : filtered) {
				if (dir.length() > 0) {
					dir.append(File.pathSeparator);
				}
				dir.append(file.getAbsolutePath());
			}
			if (Strings.isEmpty(errorMessage[0])) {
				throw new MojoFailureException(Messages.AbstractSarlBatchCompilerMojo_4);
			}
			throw new MojoFailureException(errorMessage[0]);
		}
	}

	private String getBootClassPath() throws MojoExecutionException {
		final Toolchain toolchain = this.toolchainManager.getToolchainFromBuildContext("jdk", this.mavenHelper.getSession()); //$NON-NLS-1$
		if (toolchain instanceof JavaToolchain && toolchain instanceof ToolchainPrivate) {
			final JavaToolchain javaToolChain = (JavaToolchain) toolchain;
			final ToolchainPrivate privateJavaToolChain = (ToolchainPrivate) toolchain;
			getLog().info(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_5, javaToolChain));

			String[] includes = {"jre/lib/*", "jre/lib/ext/*", "jre/lib/endorsed/*"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String[] excludes = new String[0];
			final Xpp3Dom config = (Xpp3Dom) privateJavaToolChain.getModel().getConfiguration();
			if (config != null) {
				final Xpp3Dom bootClassPath = config.getChild("bootClassPath"); //$NON-NLS-1$
				if (bootClassPath != null) {
					final Xpp3Dom includeParent = bootClassPath.getChild("includes"); //$NON-NLS-1$
					if (includeParent != null) {
						includes = getValues(includeParent.getChildren("include")); //$NON-NLS-1$
					}
					final Xpp3Dom excludeParent = bootClassPath.getChild("excludes"); //$NON-NLS-1$
					if (excludeParent != null) {
						excludes = getValues(excludeParent.getChildren("exclude")); //$NON-NLS-1$
					}
				}
			}

			try {
				return scanBootclasspath(Objects.toString(this.reflect.invoke(javaToolChain, "getJavaHome")), includes, excludes); //$NON-NLS-1$
			} catch (Exception e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			}
		}
		return ""; //$NON-NLS-1$
	}

	private String scanBootclasspath(String javaHome, String[] includes, String[] excludes) {
		getLog().debug(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_6,
				javaHome, Arrays.toString(includes), Arrays.toString(excludes)));
		final DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(new File(javaHome));
		scanner.setIncludes(includes);
		scanner.setExcludes(excludes);
		scanner.scan();

		final StringBuilder bootClassPath = new StringBuilder();
		final String[] includedFiles = scanner.getIncludedFiles();
		for (int i = 0; i < includedFiles.length; i++) {
			if (i > 0) {
				bootClassPath.append(File.pathSeparator);
			}
			bootClassPath.append(new File(javaHome, includedFiles[i]).getAbsolutePath());
		}
		return bootClassPath.toString();
	}

	private static String[] getValues(Xpp3Dom[] children) {
		final String[] values = new String[children.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = children[i].getValue();
		}
		return values;
	}

	/** Replies temporary directory.
	 *
	 * @return the temporary directory.
	 */
	protected abstract File getTempDirectory();

	/** Read the SARL Eclipse settings for the project if existing.
	 *
	 * @param sourceDirectory the source directory.
	 * @return the path from the settings.
	 */
	protected String readSarlEclipseSetting(String sourceDirectory) {
		if (this.propertiesFileLocation != null) {
			final File file = new File(this.propertiesFileLocation);
			if (file.canRead()) {
				final Properties sarlSettings = new Properties();
				try (FileInputStream stream = new FileInputStream(file)) {
					sarlSettings.load(stream);
					final String sarlOutputDirProp = sarlSettings.getProperty("outlet.DEFAULT_OUTPUT.directory", null); //$NON-NLS-1$
					if (sarlOutputDirProp != null) {
						final File srcDir = new File(sourceDirectory);
						getLog().debug(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_7,
								srcDir.getPath(), srcDir.exists()));
						if (srcDir.exists() && srcDir.getParent() != null) {
							final String path = new File(srcDir.getParent(), sarlOutputDirProp).getPath();
							getLog().debug(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_8, sarlOutputDirProp));
							return path;
						}
					}
				} catch (FileNotFoundException e) {
					getLog().warn(e);
				} catch (IOException e) {
					getLog().warn(e);
				}
			} else {
				getLog().debug(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_9, this.propertiesFileLocation));
			}
		}
		return null;
	}

	/** Replies the classpath for the standard code.
	 *
	 * @return the current classpath.
	 * @throws MojoExecutionException on failure.
	 * @see #getTestClassPath()
	 */
	protected List<File> getClassPath() throws MojoExecutionException {
		final Set<String> classPath = new LinkedHashSet<>();
		final MavenProject project = getProject();
		classPath.add(project.getBuild().getSourceDirectory());
		try {
			classPath.addAll(project.getCompileClasspathElements());
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException(e.getLocalizedMessage(), e);
		}
		for (final Artifact dep : project.getArtifacts()) {
			classPath.add(dep.getFile().getAbsolutePath());
		}
		classPath.remove(project.getBuild().getOutputDirectory());
		final List<File> files = new ArrayList<>();
		for (final String filename : classPath) {
			final File file = new File(filename);
			if (file.exists()) {
				files.add(file);
			} else {
				getLog().warn(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_10, filename));
			}
		}
		return files;
	}

	/** Replies the classpath for the test code.
	 *
	 * @return the current classpath.
	 * @throws MojoExecutionException on failure.
	 * @since 0.8
	 * @see #getClassPath()
	 */
	protected List<File> getTestClassPath() throws MojoExecutionException {
		final Set<String> classPath = new LinkedHashSet<>();
		final MavenProject project = getProject();
		classPath.add(project.getBuild().getTestSourceDirectory());
		try {
			classPath.addAll(project.getTestClasspathElements());
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException(e.getLocalizedMessage(), e);
		}
		for (final Artifact dep : project.getArtifacts()) {
			classPath.add(dep.getFile().getAbsolutePath());
		}
		final List<File> files = new ArrayList<>();
		for (final String filename : classPath) {
			final File file = new File(filename);
			if (file.exists()) {
				files.add(file);
			} else {
				getLog().warn(MessageFormat.format(Messages.AbstractSarlBatchCompilerMojo_10, filename));
			}
		}
		return files;
	}

	/** Child injection module for the SARL maven plugin.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 0.8
	 */
	private class MavenPrivateModule implements Module {

		/**
		 * Constructor.
		 */
		MavenPrivateModule() {
			//
		}

		@Override
		public void configure(Binder binder) {
			//
		}

		@Provides
		@Singleton
		public IJavaBatchCompiler providesJavaBatchCompiler(Injector injector) {
			final JavaCompiler cmp = getJavaCompiler();
			final IJavaBatchCompiler compiler = cmp.newCompilerInstance(getProject(),
					AbstractSarlBatchCompilerMojo.this.mavenHelper,
					isTestContext());
			injector.injectMembers(compiler);
			return compiler;
		}

	}

}
