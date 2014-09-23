/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
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
package io.sarl.eclipse.wizards.newproject;


/**
 * Provides the constants for the wizard "New SARL project".
 * <p>
 * These constants are externalized for helping Jnario to
 * check the documentation without instanciating Eclipse RCP.
 *
 * @author $Author: ngaud$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class Config {

	/** Path of the Java source files
	 */
	public static final String FOLDER_SOURCE_JAVA = "src/main/java"; //$NON-NLS-1$

	/** Path of the SARL source files
	 */
	public static final String FOLDER_SOURCE_SARL = "src/main/sarl"; //$NON-NLS-1$

	/** Path of the generated source files
	 */
	public static final String FOLDER_SOURCE_GENERATED = "src/main/generated-sources/xtend"; //$NON-NLS-1$

	/** Path of the generated source files that should be no more used when creating
	 * new projects. This value is the default generation folder form Xtext.
	 */
	public static final String FOLDER_SOURCE_GENERATED_XTEXT = "src-gen"; //$NON-NLS-1$

	/** Path of the resource files
	 */
	public static final String FOLDER_RESOURCES = "src/main/resources"; //$NON-NLS-1$

	/** Path of the binary files
	 */
	public static final String FOLDER_BIN = "target/classes"; //$NON-NLS-1$

	/**
	 * ID of the project nature defined by XText.
	 */
	public static final String XTEXT_NATURE_ID = "org.eclipse.xtext.ui.shared.xtextNature"; //$NON-NLS-1$

	/**
	 * ID of the Xtend plugin.
	 */
	public static final String XTEND_PLUGIN_ID = ""; //$NON-NLS-1$

	private Config() {
		//
	}

}
