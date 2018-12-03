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

package io.sarl.lang.idea;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.util.Modules2;

import io.sarl.lang.SARLRuntimeModule;
import io.sarl.lang.SARLStandaloneSetupGenerated;

/**
 * SARL standalone setup idea.
 * @author $Author: alombard$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SARLStandaloneSetupIdea extends SARLStandaloneSetupGenerated {
	@Override
	public Injector createInjector() {
		final SARLRuntimeModule runtimeModule = new SARLRuntimeModule();
		final SARLIdeaModule ideaModule = new SARLIdeaModule();
		final com.google.inject.Module mergedModule = Modules2.mixin(runtimeModule, ideaModule);
		return Guice.createInjector(mergedModule);
	}
}
