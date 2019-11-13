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

package io.janusproject.kernel.space;

import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Permission;
import java.util.concurrent.locks.ReadWriteLock;

import com.google.inject.Provider;

import io.janusproject.services.distributeddata.DistributedDataStructureService;

import io.sarl.lang.core.Address;
import io.sarl.lang.core.EventListener;
import io.sarl.lang.core.SpaceID;
import io.sarl.util.RestrictedAccessEventSpace;

/**
 * Default implementation of a restricted-access event space.
 *
 * @author $Author: srodriguez$
 * @author $Author: ngaud$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RestrictedAccessEventSpaceImpl extends AbstractEventSpace implements RestrictedAccessEventSpace {

	private final Acl acl;

	private final Permission accessPermission;

	/**
	 * Constructs an event space.
	 *
	 * @param id identifier of the space.
	 * @param acl Access Control List
	 * @param accessPermission permission that corresponds to the registration in the space.
	 * @param factory factory that is used to create the internal data structure.
	 * @param lockProvider a provider of synchronization locks.
	 */
	public RestrictedAccessEventSpaceImpl(SpaceID id, Acl acl, Permission accessPermission,
			DistributedDataStructureService factory, Provider<ReadWriteLock> lockProvider) {
		super(id, factory, lockProvider);
		assert acl != null;
		assert accessPermission != null;
		this.acl = acl;
		this.accessPermission = accessPermission;
	}

	/**
	 * Replies the Access Control List.
	 *
	 * @return the acl.
	 */
	public Acl getAcl() {
		return this.acl;
	}

	/**
	 * Replies the permission to register into this space.
	 *
	 * @return the permission.
	 */
	public Permission getRegistrationPermission() {
		return this.accessPermission;
	}

	@Override
	public Address register(EventListener entity, Principal principal) {
		if (this.acl.checkPermission(principal, this.accessPermission)) {
			return getParticipantInternalDataStructure().registerParticipant(new Address(getSpaceID(), entity.getID()), entity);
		}
		return null;
	}

	@Override
	public final <P extends EventListener & Principal> Address register(P entity) {
		return register(entity, entity);
	}

	@Override
	public Address unregister(EventListener entity) {
		return getParticipantInternalDataStructure().unregisterParticipant(entity);
	}

}
