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


import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import io.sarl.lang.SARLRuntimeModule;
import io.sarl.lang.compiler.SARLJvmGenerator;
import io.sarl.lang.validation.ConfigurableIssueSeveritiesProvider;
import io.sarl.lang.validation.IConfigurableIssueSeveritiesProvider;
import io.sarl.lang.validation.SARLValidator;
import io.sarl.lang.validation.StandardSarlConfigurableIssueCodesProvider;
import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.service.SingletonBinding;
import org.eclipse.xtext.validation.CompositeEValidator;
import org.eclipse.xtext.validation.ConfigurableIssueCodesProvider;
import org.eclipse.xtext.validation.IssueSeveritiesProvider;

/**
 * Use this class to register components to be used within IntelliJ IDEA.
 * @author $Author: alombard$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SARLIdeaModule extends AbstractSARLIdeaModule {
    /** Provider of {@link ConfigurableIssueSeveritiesProvider}.
     *
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 0.5
     */
    private static class ConfigurableIssueSeveritiesProviderProvider implements Provider<ConfigurableIssueSeveritiesProvider> {

        private ConfigurableIssueSeveritiesProvider severityProvider;

        @Inject
        private Injector injector;

        ConfigurableIssueSeveritiesProviderProvider() {
            //
        }

        @Override
        public ConfigurableIssueSeveritiesProvider get() {
            if (this.severityProvider == null) {
                this.severityProvider = new ConfigurableIssueSeveritiesProvider();
                this.injector.injectMembers(this.severityProvider);
            }
            return this.severityProvider;
        }

    }

    public void configureIConfigurableIssueSeveritiesProvider(Binder binder) {
        final ConfigurableIssueSeveritiesProviderProvider provider = new ConfigurableIssueSeveritiesProviderProvider();
        binder.bind(ConfigurableIssueSeveritiesProvider.class).toProvider(provider);
        binder.bind(IssueSeveritiesProvider.class).toProvider(provider);
        binder.bind(IConfigurableIssueSeveritiesProvider.class).toProvider(provider);
    }

    public Class<? extends ConfigurableIssueCodesProvider> bindConfigurableIssueCodesProvider() {
        return StandardSarlConfigurableIssueCodesProvider.class;
    }

    @SingletonBinding(eager=true)
    public Class<? extends SARLValidator> bindSARLValidator() {
        return SARLValidator.class;
    }

    public void configureIGenerator2ExtraLanguageMainGenerator(Binder binder) {
        binder.bind(IGenerator2.class)
                //.annotatedWith(Names.named(io.sarl.lang.extralanguage.compiler.ExtraLanguageGeneratorSupport.MAIN_GENERATOR_NAME))
                .to(SARLJvmGenerator.class);
    }
}
