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

package io.sarl.examples.tests;

import static io.sarl.examples.wizard.SarlExampleLaunchConfiguration.LAUNCH_PROPERTY_FILE;
import static io.sarl.examples.wizard.SarlExampleLaunchConfiguration.readLaunchConfigurationFromXml;
import static io.sarl.examples.wizard.SarlExampleLaunchConfiguration.readXmlAttribute;
import static io.sarl.examples.wizard.SarlExampleLaunchConfiguration.readXmlContent;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.inject.Injector;
import org.arakhne.afc.vmutil.ClasspathUtil;
import org.arakhne.afc.vmutil.FileSystem;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.validation.IssueCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.helpers.NOPLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sarl.lang.SARLStandaloneSetup;
import io.sarl.lang.SARLVersion;
import io.sarl.lang.compiler.batch.CleaningPolicy;
import io.sarl.lang.compiler.batch.SarlBatchCompiler;
import io.sarl.tests.api.AbstractSarlTest;

/** Class for testing the examples.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RunWith(Parameterized.class)
@SuppressWarnings("all")
public class ExamplesTest extends AbstractSarlTest {

	/** Name of the property that contains the root path for the project to test.
	 */
	public static final String ROOT_TEST_FOLDER_PROPERTY = "io.sarl.examples.test.rootDir"; //$NON-NLS-1$

	private static final File DEFAULT_RELATIVE_PATH = FileSystem.convertStringToFile("file:../io.sarl.examples.plugin"); //$NON-NLS-1$

	private static final String CONTENTS_FOLDER_NAME = "contents"; //$NON-NLS-1$

	private static final String PROJECTS_FOLDER_NAME = "projects"; //$NON-NLS-1$

	/** Replies the archives for the examples.
	 *
	 * @return the archive locations.
	 * @throws Exception in case of error.
	 */
	@Parameters(name = "Example {1}")
	public static Collection<Object[]> getExampleArchives() throws Exception {
		final Set<String> names = new TreeSet<>();
		final Map<String, Pair<File, File>> rawSources = new TreeMap<>();

		File rootPath = null;
		final String projectdir = System.getProperty(ROOT_TEST_FOLDER_PROPERTY);
		if (!Strings.isEmpty(projectdir)) {
			rootPath = FileSystem.convertStringToFile(projectdir);
		}
		if (rootPath == null) {
			rootPath = DEFAULT_RELATIVE_PATH;
		}

		final File sourceFolder = new File(rootPath, PROJECTS_FOLDER_NAME);
		final File projectFolder = new File(rootPath, CONTENTS_FOLDER_NAME);
		if (projectFolder.isDirectory()) {
			for (File child : projectFolder.listFiles()) {
				if (child.isFile()) {
					final String basename = child.getName();
					final String sbasename = FileSystem.shortBasename(child);
					rawSources.putIfAbsent(basename, Pair.of(child,
							new File(sourceFolder, sbasename)));
					names.add(basename);
				}
			}
		}

		if (names.isEmpty()) {
			throw new Exception("no test found"); //$NON-NLS-1$
		}

		final List<Object[]> list = new ArrayList<>();

		for (final String name : names) {
			final Pair<File, File> pair = rawSources.get(name);
			final File zipFile = pair.getKey();
			final File projectSourceFolder = pair.getValue();
			list.add(new Object[] {name, zipFile, projectSourceFolder});
		}

		return list;
	}

	private final String name;

	private final File exampleZipFile;

	private final File exampleSourceFile;

	private SarlBatchCompiler compiler;

	/** Constructor.
	 *
	 * @param name the name of the test.
	 * @param exampleZipFile the path to the zip file to open.
	 */
	public ExamplesTest(String name, File exampleZipFile, File exampleSourceFile) {
		this.name = name;
		this.exampleZipFile = exampleZipFile.getAbsoluteFile();
		this.exampleSourceFile = exampleSourceFile.getAbsoluteFile();
	}

	@Before
	public void setUp() throws Exception {
		Injector injector = SARLStandaloneSetup.doSetup();
		this.compiler = injector.getInstance(SarlBatchCompiler.class);
	}

	@After
	public void tearDown() throws Exception {
		this.compiler = null;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Test
	public void path() {
		assertNotNull(this.exampleZipFile);
	}
	
	@Test
	public void compilation() throws Exception {
		assumeTrue(this.exampleZipFile != null);
		final File projectRoot = createProject(); 
		final List<File> installedFiles = unpackFiles(projectRoot);
		assertFalse("No installed file in " + projectRoot, installedFiles.isEmpty());
		if (isMavenProject()) {
			// Maven compilation
			final String errors = compileMaven(projectRoot);
			assertTrue(errors, Strings.isEmpty(errors));
		} else {
			// Standard SARL compilation
			List<String> issues = compileFiles(projectRoot, installedFiles);
			assertNoIssue(issues);
		}
	}

	@Test
	public void launchConfiguration() throws Exception {
		assumeTrue(this.exampleZipFile != null);
		final File projectRoot = createProject(); 
		final List<File> installedFiles = unpackFiles(projectRoot);
		final File launchConfiguration = new File(projectRoot, LAUNCH_PROPERTY_FILE);
		assumeTrue(launchConfiguration.exists());

		final File relativeLaunchConfiguration = FileSystem.makeRelative(launchConfiguration, projectRoot);
		assertTrue(installedFiles.contains(relativeLaunchConfiguration));

		final File folder;

		if (isMavenProject()) {
			// Maven compilation
			compileMaven(projectRoot);
			folder = FileSystem.join(projectRoot, "src", "main", "generated-sources", "sarl");
		} else {
			// Standard SARL compilation
			compileFiles(projectRoot, installedFiles);
			folder = getSourceGenPath(projectRoot);
		}

		final Document launch = readXmlContent(launchConfiguration);

		readLaunchConfigurationFromXml(launch, (type, name, isAgent) -> {
			final String filename = type.replaceAll("\\.", File.separator).concat(".java");
			File file = FileSystem.convertStringToFile(filename);
			file = FileSystem.join(folder, file);
			assertTrue("The type specified into the launch configuration \""
					+ name + "\" was not found into the compilation results: "
					+ type,
					file.exists());
		});
	}

	@Test
	public void fileToOpen() throws Exception {
		assumeTrue(this.exampleZipFile != null);
		final File projectRoot = createProject(); 
		final List<File> installedFiles = unpackFiles(projectRoot);
		final File pluginFile = new File(DEFAULT_RELATIVE_PATH, "plugin.xml");
		final Document document = readXmlContent(pluginFile);
		Node node = readNode(document, "plugin");
		node = readExtensionPoint(node);
		assumeNotNull(node);
		
		String locationStr = readXmlAttribute(node, "location");
		assertNotNull(locationStr);
		IPath location = Path.fromPortableString(locationStr);
	
		// Format 1: <project-name>/<source-folder>/<qualified-filename>
		IPath filePath = location.removeFirstSegments(1);
		File file = FileSystem.join(projectRoot, filePath.toFile());
		if (file == null || !file.exists()) {
			// Format 2: <source-folder>/<qualified-filename>
			file = FileSystem.join(projectRoot, location.toFile());
			if (file == null || !file.exists()) {
				// Format 3: <qualified-filename>
				final File folder = getSourcePath(projectRoot);
				file = FileSystem.join(folder, location.toFile());
				assertTrue("The filename to be opened that is specified into the plugin.xml file for \""
						+ this.exampleZipFile.getName() + "\" was not found into the zip file",
						file != null && file.exists());
			}
		}
	}

	private static Node readNode(Node root, String name) {
		NodeList nodes = root.getChildNodes();
		final int len = nodes.getLength();
		for (int i = 0; i < len; ++i) {
			Node node = nodes.item(i);
			if (node != null) {
				if (name.equals(node.getNodeName())) {
					return node;
				}
			}
		}
		return null;
	}
	
	private Node readExtensionPoint(Node root) {
		NodeList nodes = root.getChildNodes();
		final int len = nodes.getLength();
		for (int i = 0; i < len; ++i) {
			Node node = nodes.item(i);
			if (node != null) {
				if ("extension".equals(node.getNodeName())
					&& "org.eclipse.emf.common.ui.examples".equals(readXmlAttribute(node, "point"))) {
					node = readNode(node, "example");
					if (node != null) {
						Node prjNode = readNode(node, "projectDescriptor");
						if (prjNode != null) {
							String field = readXmlAttribute(prjNode, "contentURI");
							assertNotNull(field);
							File zip = FileSystem.convertStringToFile(field);
							if (this.exampleZipFile.getPath().endsWith(zip.getPath())) {
								return readNode(node, "fileToOpen");
							}
						}
					}
				}
			}
		}
		return null;
	}

	private boolean isMavenProject() {
		if (this.exampleSourceFile != null) {
			final File pomFile = new File(this.exampleSourceFile, "pom.xml");
			return pomFile.exists();
		}
		return false;
	}

	private void assertNoIssue(List<String> issues) {
		if (!issues.isEmpty()) {
			throw new ComparisonFailure("Errors in the example code", "", Strings.concat("\n", issues));
		}
	}

	private String compileMaven(File root) throws Exception {
		File compiledFile = new File(root, ".sarltestscompiled.tmp");
		if (compiledFile.exists()) {
			return null;
		}
		compiledFile.createNewFile();
		
		final String[] command = new String[] {
				"mvn", "-q", "clean", "package"
		};
		final Process p = Runtime.getRuntime().exec(command, null, root);
		p.waitFor();
		final StringBuilder output = new StringBuilder();
		output.append("Exit code: ").append(p.exitValue()).append("\n");
		final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = reader.readLine();
		while (line != null) {
			output.append(line + "\n");
			line = reader.readLine();
		}
		final BufferedReader readerErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		line = reader.readLine();
		while (line != null) {
			output.append(line + "\n");
			line = reader.readLine();
		}
		if (p.exitValue() != 0) {
			return output.toString();
		}
		return null;
	}

	private List<String> compileFiles(File root, List<File> installedFiles) throws Exception {
		final List<String> issues = new ArrayList<>();
		compiler.setBasePath(root.getAbsolutePath());
		compiler.setTempDirectory(getTempPath(root));
		compiler.addSourcePath(getSourcePath(root));
		compiler.setClassOutputPath(getBinPath(root));
		compiler.setOutputPath(getSourceGenPath(root));
		compiler.setGenerateGeneratedAnnotation(false);
		compiler.setGenerateInlineAnnotation(false);
		compiler.setGenerateSyntheticSuppressWarnings(true);
		compiler.setCleaningPolicy(CleaningPolicy.NO_CLEANING);
		compiler.setClassPath(getClasspath());
		compiler.setJavaSourceVersion(SARLVersion.MINIMAL_JDK_VERSION_FOR_SARL_COMPILATION_ENVIRONMENT);
		compiler.setAllWarningSeverities(Severity.IGNORE);
		compiler.setWarningSeverity(IssueCodes.DEPRECATED_MEMBER_REFERENCE, Severity.ERROR);
		compiler.setJavaCompilerVerbose(false);
		compiler.setLogger(NOPLogger.NOP_LOGGER);
		compiler.addIssueMessageListener((issue, uri, message) -> {
			if (issue.isSyntaxError() || issue.getSeverity().compareTo(Severity.ERROR) >= 0) {
				final Integer line = issue.getLineNumber();
				final int issueLine = (line == null ? 0 : line.intValue());
				issues.add(message + " (line " + issueLine + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
		if (compiler.compile()) {
			return Collections.emptyList();
		}
		return issues;
	}

	private List<File> getClasspath() throws Exception {
		final List<File> classpath = new ArrayList<>();
		final Iterator<URL> iterator = ClasspathUtil.getClasspath();
		while (iterator.hasNext()) {
			final URL url = iterator.next();
			try {
				final File file = FileSystem.convertURLToFile(url);
				classpath.add(file);
			} catch (IllegalArgumentException exception) {
				//
			}
		}
		return classpath;
	}

	private List<File> unpackFiles(File root) throws Exception {
		FileSystem.unzipFile(this.exampleZipFile, root);

		final List<File> installedFiles = new ArrayList<>();
		final List<File> folders = new ArrayList<>();
		folders.add(root);
		while (!folders.isEmpty()) {
			final File folder = folders.remove(0);
			for (final File file : folder.listFiles()) {
				if (file.isDirectory()) {
					folders.add(file);
				} else if (file.isFile()) {
					if (!isIgnorableFile(file)) {
						final File relPathFile = FileSystem.makeRelative(file, root);
						installedFiles.add(relPathFile);
					} else {
						file.delete();
					}
				}
			}
		}
		return installedFiles;
	}

	private static boolean isIgnorableFile(File file) {
		final String name = file.getName();
		return ".classpath".equals(name) || ".project".equals(name);
	}

	private File getSourcePath(File rootPath) {
		return FileSystem.join(rootPath, "src", "main", "sarl");
	}

	private File getSourceGenPath(File rootPath) {
		return new File(rootPath, "src-gen");
	}

	private File getBinPath(File rootPath) {
		return new File(rootPath, "bin");
	}

	private File getTempPath(File rootPath) {
		return new File(rootPath, "build");
	}

	private File createProject() throws Exception {
		final File rootPath = FileSystem.createTempDirectory("exampletests", ".tmp").getAbsoluteFile();
		getSourcePath(rootPath).mkdirs();
		getSourceGenPath(rootPath).mkdirs();
		getBinPath(rootPath).mkdirs();
		getTempPath(rootPath).mkdirs();
		FileSystem.deleteOnExit(rootPath);
		return rootPath;
	}

}
