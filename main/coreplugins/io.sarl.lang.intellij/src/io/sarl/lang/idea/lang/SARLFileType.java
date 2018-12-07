/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2018 the original authors or authors.
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

package io.sarl.lang.idea.lang;


import com.intellij.openapi.util.IconLoader;
import org.eclipse.xtext.idea.Icons;

import javax.swing.*;

/**
 * SARL file type.
 * @author $Author: alombard$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SARLFileType extends AbstractSARLFileType {

	public static final Icon SARL_FILE_TYPE = IconLoader.getIcon("/icons/sarl_file_type.png");
	/**
	 * SARL file type instance.
	 */
	public static final SARLFileType INSTANCE = new SARLFileType();

	/**
	 * SARL file type constructor.
	 */
	public SARLFileType() {
		super(SARLLanguage.INSTANCE);
	}

	@Override
	public Icon getIcon() {
		return SARL_FILE_TYPE;
	}
}
