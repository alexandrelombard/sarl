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

package io.janusproject.modules.eventserial;

import java.text.DateFormat;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

import io.janusproject.JanusConfig;
import io.janusproject.kernel.services.gson.GsonEventSerializer;
import io.janusproject.kernel.services.jdk.network.AESEventEncrypter;
import io.janusproject.kernel.services.jdk.network.JavaBinaryEventSerializer;
import io.janusproject.kernel.services.jdk.network.PlainTextEventEncrypter;
import io.janusproject.services.network.EventEncrypter;
import io.janusproject.services.network.EventSerializer;
import io.janusproject.services.network.NetworkConfig;

/**
 * Module that provides the network events.
 *
 * @author $Author: srodriguez$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NetworkEventModule extends AbstractModule {

	private static final Class<? extends EventSerializer> DEFAULT_EVENT_SERIALIZER = JavaBinaryEventSerializer.class;

	private static final Class<? extends EventEncrypter> DEFAULT_EVENT_ENCRYPTER = PlainTextEventEncrypter.class;

	/**
	 * Replies the default values for the properties supported by Janus config.
	 *
	 * @param defaultValues filled with the default values supported by the Janus platform.
	 */
	public static void getDefaultValues(Properties defaultValues) {
		defaultValues.put(NetworkConfig.SERIALIZER_CLASSNAME, DEFAULT_EVENT_SERIALIZER.getName());
		defaultValues.put(NetworkConfig.ENCRYPTER_CLASSNAME, DEFAULT_EVENT_ENCRYPTER.getName());
	}

	@Override
	protected void configure() {
		//
	}

	@Provides
	private static Gson createGson() {
		final GsonBuilder builder = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.serializeSpecialFloatingPointValues()
				.setDateFormat(DateFormat.SHORT, DateFormat.FULL)
				.registerTypeAdapter(Class.class, new GsonEventSerializer.ClassTypeAdapter());
		// Make the serializer pretty when the application is launched in debug mode (ie. with assertions enabled).
		if (NetworkEventModule.class.desiredAssertionStatus()) {
			builder.setPrettyPrinting();
		}
		return builder.create();
	}

	@Provides
	private static EventSerializer createEventSerializer(Injector injector) {
		Class<? extends EventSerializer> serializerType = DEFAULT_EVENT_SERIALIZER;
		final String serializerClassname = JanusConfig.getSystemProperty(NetworkConfig.SERIALIZER_CLASSNAME);
		if (serializerClassname != null && !serializerClassname.isEmpty()) {
			final Class<?> type;
			try {
				type = Class.forName(serializerClassname);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (type != null && EventSerializer.class.isAssignableFrom(type)) {
				serializerType = type.asSubclass(EventSerializer.class);
			}
			assert injector != null;
			return injector.getInstance(serializerType);
		}
		return injector.getInstance(serializerType);
	}

	@Provides
	private static EventEncrypter getEncrypter(Injector injector) {
		Class<? extends EventEncrypter> encrypterType = null;
		final String encrypterClassname = JanusConfig.getSystemProperty(NetworkConfig.ENCRYPTER_CLASSNAME);
		if (encrypterClassname != null && !encrypterClassname.isEmpty()) {
			try {
				final Class<?> type = Class.forName(encrypterClassname);
				if (type != null && EventEncrypter.class.isAssignableFrom(type)) {
					encrypterType = type.asSubclass(EventEncrypter.class);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		if (encrypterType == null) {
			final String aesKey = JanusConfig.getSystemProperty(NetworkConfig.AES_KEY);
			if (aesKey != null && !aesKey.isEmpty()) {
				encrypterType = AESEventEncrypter.class;
			} else {
				encrypterType = DEFAULT_EVENT_ENCRYPTER;
			}
		}
		assert injector != null;
		return injector.getInstance(encrypterType);
	}

}
