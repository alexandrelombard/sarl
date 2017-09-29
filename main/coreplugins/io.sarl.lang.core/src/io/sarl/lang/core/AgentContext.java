/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2017 the original authors or authors.
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

package io.sarl.lang.core;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import io.sarl.lang.util.SynchronizedIterable;

/**
 * An AgentContext defines the boundary of a sub-system, and gathers a collection of Spaces.
 * Each context has a default context that provides a basic interaction context.
 *
 * @author $Author: srodriguez$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AgentContext {

	/** Replies the identifier of the context.
	 *
	 * @return the identifier of the context.
	 */
	@Pure
	UUID getID();

	/** Replies the default space of the context.
	 * The default space is assumed to be always an {@link EventSpace event-based space}.
	 *
	 * @return the default space.
	 */
	@Pure
	EventSpace getDefaultSpace();

	/** Replies all the spaces defined in this context.
	 *
	 * @return the spaces.
	 */
	@Pure
	SynchronizedIterable<? extends Space> getSpaces();

	/** Replies all the spaces that are implementing the given specification.
	 *
	 * @param <S> - type of the replied space.
	 * @param spec specification of the space to retreive.
	 * @return the spaces associated to the given space specification.
	 */
	@Pure
	<S extends Space> SynchronizedIterable<S> getSpaces(Class<? extends SpaceSpecification<S>> spec);


	/** Create an instance of space following the given specification.
	 * This function always creates a new instance of space.
	 * It means that is a space with the given identifier already
	 * exists, this function does nothing and replies <code>null</code>.
	 * If you want to find an existing space prior to the creation of
	 * a new one, you should use {@link #getOrCreateSpace(Class, UUID, Object...)}.
	 *
	 * @param <S> - type of the created space.
	 * @param spec specification of the space to create.
	 * @param spaceUUID identifier of the new space.
	 * @param creationParams parameters to pass to the space constructor.
	 * @return the new space, or <code>null</code> if the space already exists.
	 * @see #getOrCreateSpace(Class, UUID, Object...)
	 * @see #getSpace(UUID)
	 */
	<S extends Space> S createSpace(Class<? extends SpaceSpecification<S>> spec,
			UUID spaceUUID, Object... creationParams);

	/** Retreive or create an instance of space which was created with the given specification.
	 *
	 * <p>This function tries to find a space that was created with the given specification.
	 * If none was found, this function creates a new space with the given space
	 * identifier and creation parameters.
	 *
	 * <p><strong>Caution:</strong> The <code>spaceUUID</code> parameter is used only
	 * if no existing space created with the given specification was found.
	 *
	 * @param <S> - type of the replied space.
	 * @param spec specification of the space to retreive/create.
	 * @param spaceUUID identifier used only when creating the space.
	 * @param creationParams parameters to pass to the space constructor.
	 * @return the space, never <code>null</code>.
	 * @see #getOrCreateSpaceWithID(UUID, Class, Object...)
	 * @see #createSpace(Class, UUID, Object...)
	 * @see #getSpace(UUID)
	 * @deprecated see {@link #getOrCreateSpaceWithSpec(Class, UUID, Object...)}
	 */
	@Deprecated
	default <S extends Space> S getOrCreateSpace(Class<? extends SpaceSpecification<S>> spec,
			UUID spaceUUID, Object... creationParams) {
		return getOrCreateSpaceWithSpec(spec, spaceUUID, creationParams);
	}

	/** Retreive or create an instance of space which was created with the given specification.
	 *
	 * <p>This function tries to find a space that was created with the given specification.
	 * If none was found, this function creates a new space with the given space
	 * identifier and creation parameters.
	 *
	 * <p><strong>Caution:</strong> The <code>spaceUUID</code> parameter is used only
	 * if no existing space created with the given specification was found.
	 *
	 * @param <S> - type of the replied space.
	 * @param spec specification of the space to retreive/create.
	 * @param spaceUUID identifier used only when creating the space.
	 * @param creationParams parameters to pass to the space constructor.
	 * @return the space, never <code>null</code>.
	 * @see #getOrCreateSpaceWithID(UUID, Class, Object...)
	 * @see #createSpace(Class, UUID, Object...)
	 * @see #getSpace(UUID)
	 */
	<S extends Space> S getOrCreateSpaceWithSpec(Class<? extends SpaceSpecification<S>> spec,
			UUID spaceUUID, Object... creationParams);

	/** Retreive or create an instance of space with the given identifier.
	 *
	 * <p>This function tries to find a space with the given identifier.
	 * If none was found, this function creates a new space with the given
	 * specification and creation parameters.
	 *
	 * <p><strong>Caution:</strong> The <code>spaceUUID</code> parameter is given to
	 * the specification when creating the space.
	 *
	 * @param <S> - type of the replied space.
	 * @param spaceUUID identifier of the space.
	 * @param spec specification of the space for creating the space.
	 * @param creationParams parameters to pass to the space constructor.
	 * @return the space, never <code>null</code>.
	 * @see #getOrCreateSpaceWithID(UUID, Class, Object...)
	 * @see #createSpace(Class, UUID, Object...)
	 * @see #getSpace(UUID)
	 * @deprecated see {@link #getOrCreateSpaceWithID(Class, UUID, Object...)}
	 */
	@Deprecated
	default <S extends Space> S getOrCreateSpaceWithID(UUID spaceUUID,
			Class<? extends SpaceSpecification<S>> spec, Object... creationParams) {
		return getOrCreateSpaceWithID(spec, spaceUUID, creationParams);
	}

	/** Retreive or create an instance of space with the given identifier.
	 *
	 * <p>This function tries to find a space with the given identifier.
	 * If none was found, this function creates a new space with the given
	 * specification and creation parameters.
	 *
	 * <p><strong>Caution:</strong> The <code>spaceUUID</code> parameter is given to
	 * the specification when creating the space.
	 *
	 * @param <S> - type of the replied space.
	 * @param spaceUUID identifier of the space.
	 * @param spec specification of the space for creating the space.
	 * @param creationParams parameters to pass to the space constructor.
	 * @return the space, never <code>null</code>.
	 * @see #getOrCreateSpaceWithID(UUID, Class, Object...)
	 * @see #createSpace(Class, UUID, Object...)
	 * @see #getSpace(UUID)
	 * @since 0.6
	 */
	<S extends Space> S getOrCreateSpaceWithID(Class<? extends SpaceSpecification<S>> spec,
			UUID spaceUUID, Object... creationParams);

	/** Retreive, but do not create, an instance of space following the given ID.
	 * This function tries to find a space that fits the given specification.
	 * If none was found, this function replies <code>null</code>.
	 *
	 * @param <S> - type of the replied space.
	 * @param spaceUUID identifier of the space.
	 * @return the space, or <code>null</code> if there is no space found.
	 * @see #createSpace(Class, UUID, Object...)
	 * @see #getOrCreateSpace(Class, UUID, Object...)
	 */
	@Pure
	<S extends Space> S getSpace(UUID spaceUUID);

}
