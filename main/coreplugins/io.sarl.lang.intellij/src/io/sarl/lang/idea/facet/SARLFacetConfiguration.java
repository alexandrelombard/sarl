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

package io.sarl.lang.idea.facet;

import com.intellij.openapi.components.PersistentStateComponent;
import org.eclipse.xtext.xbase.idea.facet.XbaseFacetConfiguration;
import org.eclipse.xtext.xbase.idea.facet.XbaseGeneratorConfigurationState;

/**
 * SARL facet configuration.
 * @author $Author: alombard$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
// FIXME
//@State(name = "io.sarl.lang.SARLGenerator", storages = {
//		@Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
//		@Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR
//				+ "/SARLGeneratorConfig.xml", scheme = StorageScheme.DIRECTORY_BASED)})
public class SARLFacetConfiguration extends XbaseFacetConfiguration implements PersistentStateComponent<XbaseGeneratorConfigurationState> {
	//
}
