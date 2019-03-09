/*
 * $Id$
 *
 * File is automatically generated by the Xtext language generator.
 * Do not change it.
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
package io.sarl.lang.codebuilder.builders;

import io.sarl.lang.sarl.SarlFactory;
import io.sarl.lang.sarl.SarlFormalParameter;
import javax.inject.Inject;
import javax.inject.Provider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend.core.xtend.XtendExecutable;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmVoid;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.resource.IFragmentProvider;
import org.eclipse.xtext.util.EmfFormatter;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.lib.Procedures;
import org.eclipse.xtext.xbase.lib.Pure;

/** Builder of a Sarl formal parameter.
 */
@SuppressWarnings("all")
public class FormalParameterBuilderImpl extends AbstractBuilder implements IFormalParameterBuilder {

	@Inject
	private Provider<IExpressionBuilder> expressionProvider;

	private XtendExecutable context;

	private SarlFormalParameter parameter;

	private IExpressionBuilder defaultValue;

	@Inject
		private TypesFactory jvmTypesFactory;

	@Inject
 private IFragmentProvider fragmentProvider;

	/** Initialize the formal parameter.
	 * @param context the context of the formal parameter.
	 * @param name the name of the formal parameter.
	 */
	public void eInit(XtendExecutable context, String name, IJvmTypeProvider typeContext) {
		setTypeResolutionContext(typeContext);
		this.context = context;
			this.parameter = SarlFactory.eINSTANCE.createSarlFormalParameter();
			this.parameter.setName(name);
			this.parameter.setParameterType(newTypeRef(this.context, Object.class.getName()));
			this.context.getParameters().add(this.parameter);
	}

	/** Replies the created parameter.
	 *
	 * @return the parameter.
	 */
	@Pure
	public SarlFormalParameter getSarlFormalParameter() {
		return this.parameter;
	}

	/** Replies the JvmIdentifiable that corresponds to the formal parameter.
	 *
	 * @param container the feature call that is supposed to contains the replied identifiable element.
	 */
	public void setReferenceInto(XFeatureCall container) {
		JvmVoid jvmVoid = this.jvmTypesFactory.createJvmVoid();
		if (jvmVoid instanceof InternalEObject) {
			final InternalEObject			jvmVoidProxy = (InternalEObject) jvmVoid;
			final EObject param = getSarlFormalParameter();
			final Resource resource = param.eResource();
			// Get the derived object
			final SarlFormalParameter jvmParam = getAssociatedElement(SarlFormalParameter.class, param, resource);
			// Set the proxy URI
			final URI uri = EcoreUtil2.getNormalizedURI(jvmParam);
			jvmVoidProxy.eSetProxyURI(uri);
		}
		container.setFeature(jvmVoid);
	}

	/** Replies the resource to which the formal parameter is attached.
	 */
	@Pure
	public Resource eResource() {
		return getSarlFormalParameter().eResource();
	}

	/** Change the type.
	 *
	 * @param type the formal parameter type.
	 */
	public void setParameterType(String type) {
		String typeName;
		if (Strings.isEmpty(type)) {
			typeName = Object.class.getName();
		} else {
			typeName = type;
		}
		this.parameter.setParameterType(newTypeRef(this.context, typeName));
	}

	/** Change the variadic property of the parameter.
	 *
	 * @param isVariadic indicates if the parameter is variadic.
	 */
	public void setVarArg(boolean isVariadic) {
		this.parameter.setVarArg(isVariadic);
	}

	/** Replies the default value of the parameter.
	 * @return the default value builder.
	 */
	@Pure
	public IExpressionBuilder getDefaultValue() {
		if (this.defaultValue == null) {
			this.defaultValue = this.expressionProvider.get();
			this.defaultValue.eInit(this.parameter, new Procedures.Procedure1<XExpression>() {
					public void apply(XExpression it) {
						getSarlFormalParameter().setDefaultValue(it);
					}
				}, getTypeResolutionContext());
		}
		return this.defaultValue;
	}

	@Override
	@Pure
	public String toString() {
		return EmfFormatter.objToStr(getDefaultValue());
	}

}

