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

package io.sarl.sarldoc.modules.configs;

import static io.bootique.BQCoreModule.extend;
import static io.sarl.sarldoc.configs.subconfigs.SarldocConfig.DOC_OUTPUT_DIRECTORY_NAME;
import static io.sarl.sarldoc.configs.subconfigs.SarldocConfig.JAVADOC_EXECUTABLE_NAME;
import static io.sarl.sarldoc.configs.subconfigs.SarldocConfig.TITLE_NAME;

import com.google.inject.AbstractModule;
import io.bootique.meta.application.OptionMetadata;
import org.arakhne.afc.bootique.variables.VariableDecls;

/**
 * Module for creating and configuring the configuration that is specific to sarldoc.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public class SarldocConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		VariableDecls.extend(binder()).declareVar(JAVADOC_EXECUTABLE_NAME);
		extend(binder()).addOption(OptionMetadata.builder(
				"javadoc", //$NON-NLS-1$
				Messages.SarldocConfigModule_0)
				.configPath(JAVADOC_EXECUTABLE_NAME)
				.build());

		VariableDecls.extend(binder()).declareVar(DOC_OUTPUT_DIRECTORY_NAME);
		extend(binder()).addOption(OptionMetadata.builder(
				"d", //$NON-NLS-1$
				Messages.SarldocConfigModule_1)
				.configPath(DOC_OUTPUT_DIRECTORY_NAME)
				.build());

		VariableDecls.extend(binder()).declareVar(TITLE_NAME);
		extend(binder()).addOption(OptionMetadata.builder(
				"doctitle", //$NON-NLS-1$
				Messages.SarldocConfigModule_2)
				.configPath(TITLE_NAME)
				.build());
	}

}
