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

package io.sarl.pythongenerator.generator.generator;

import java.util.Collections;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IGeneratorContext;

import io.sarl.lang.extralanguage.compiler.IExtraLanguageGeneratorProvider;
import io.sarl.lang.extralanguage.compiler.IRootGenerator;

/** The provider of a generator from SARL to the Python language.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.6
 */
@Singleton
public class PyGeneratorProvider implements IExtraLanguageGeneratorProvider {

	@Inject
	private Injector injector;

	@Override
	public Iterable<IRootGenerator> getGenerators(IGeneratorContext context, Resource resource) {
		final PyGenerator generator = this.injector.getInstance(PyGenerator.class);
		return Collections.singleton(generator);
	}

}
