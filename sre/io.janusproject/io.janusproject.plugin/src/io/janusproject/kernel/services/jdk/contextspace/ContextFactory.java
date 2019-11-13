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

package io.janusproject.kernel.services.jdk.contextspace;

import java.util.UUID;

import io.janusproject.services.contextspace.SpaceRepositoryListener;

/**
 * Factory of contexts.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@FunctionalInterface
public interface ContextFactory {

	/**
	 * Create an instance of context.
	 *
	 * @param contextId id of the context.
	 * @param defaultSpaceId id of the default space.
	 * @param factory factory of the space repository in the new context.
	 * @param listener listener on the space repository that must be given to the created context at startup.
	 * @return the context
	 */
	Context newInstance(UUID contextId, UUID defaultSpaceId, SpaceRepositoryFactory factory, SpaceRepositoryListener listener);

}
