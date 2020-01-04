/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2020 the original authors or authors.
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

package io.sarl.sarldoc.modules.internal;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

/** Provider of the module for injecting the dynamic class loader.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public class SarldocDynamicClassLoaderModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new SarldocDynamicClassLoaderModule();
	}

	@Override
    public BQModule.Builder moduleBuilder() {
        return BQModule
                .builder(module())
                .overrides(overrides())
                .providerName(name())
                .configs(configs())
                .description(Messages.SarldocDynamicClassLoaderModuleProvider_0);
    }

}
