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

package io.sarl.docs.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.tools.doclets.formats.html.LinkInfoImpl;
import com.sun.tools.doclets.formats.html.MethodWriterImpl;
import com.sun.tools.doclets.formats.html.SubWriterHolderWriter;
import com.sun.tools.doclets.formats.html.markup.HtmlTag;
import com.sun.tools.doclets.formats.html.markup.HtmlTree;
import com.sun.tools.doclets.formats.html.markup.StringContent;
import com.sun.tools.doclets.internal.toolkit.Content;

/** Writer of methods dedicated to the SARL doclet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.7
 */
public class SarlMethodWriter extends MethodWriterImpl {

	/** Constructor.
	 *
	 * @param writer the writer for the class that the methods belong to.
	 * @param classDoc the class being documented.
	 */
	public SarlMethodWriter(SubWriterHolderWriter writer, ClassDoc classDoc) {
		super(writer, classDoc);
	}

	/** Constructor.
	 *
	 * @param writer the writer for the class that the methods belong to.
	 */
	public SarlMethodWriter(SubWriterHolderWriter writer) {
		super(writer);
	}

	@Override
	public Content getSignature(MethodDoc method) {
		final Content pre = new HtmlTree(HtmlTag.PRE);
		addAnnotations(method, pre);
		addModifiers(method, pre);
		pre.addContent(Utils.keyword(Utils.getKeywords().getDefKeyword()));
		pre.addContent(this.writer.getSpace());
		if (this.configuration.linksource) {
			final Content methodName = new StringContent(method.name());
			this.writer.addSrcLink(method, methodName, pre);
		} else {
			addName(method.name(), pre);
		}
		final int indent = pre.charCount();
		addParameters(method, pre, indent);
		addReturnType(method, pre);
		addTypeParameters(method, pre);
		addExceptions(method, pre, indent);
		return pre;
	}

	@Override
	protected void addReturnType(MethodDoc method, Content htmlTree) {
		htmlTree.addContent(this.writer.getSpace());
		htmlTree.addContent(Utils.getKeywords().getColonKeyword());
		htmlTree.addContent(" "); //$NON-NLS-1$
		super.addReturnType(method, htmlTree);
	}

	/** Add annotations, except the reserved annotations.
	 *
	 * @param member the members.
	 * @param htmlTree the output.
	 */
	protected void addAnnotations(ExecutableMemberDoc member, Content htmlTree) {
		this.writer.addAnnotationInfo(member, htmlTree);
	}

	@Override
	protected void addTypeParameters(ExecutableMemberDoc member, Content htmlTree) {
        final Content typeParameters = getTypeParameters(member);
        if (!typeParameters.isEmpty()) {
    		htmlTree.addContent(" "); //$NON-NLS-1$
    		htmlTree.addContent(Utils.keyword(Utils.getKeywords().getWithKeyword()));
    		htmlTree.addContent(" "); //$NON-NLS-1$
            htmlTree.addContent(typeParameters);
            htmlTree.addContent(" "); //$NON-NLS-1$
        }
	}

	@Override
	protected void addParam(ExecutableMemberDoc member, Parameter param, boolean isVarArg, Content htmlTree) {
        final String defaultValue = Utils.getParameterDefaultValue(member, param, this.configuration);
        final boolean addDefaultValueBrackets = Utils.isNullOrEmpty(defaultValue)
        		&& Utils.isDefaultValuedParameter(member, param, this.configuration);
        if (addDefaultValueBrackets) {
        	htmlTree.addContent("["); //$NON-NLS-1$
        }
        if (param.name().length() > 0) {
            htmlTree.addContent(param.name());
        }
		htmlTree.addContent(this.writer.getSpace());
		htmlTree.addContent(Utils.getKeywords().getColonKeyword());
		htmlTree.addContent(" "); //$NON-NLS-1$
        if (param.type() != null) {
            final Content link = this.writer.getLink(new LinkInfoImpl(
                    this.configuration, LinkInfoImpl.Kind.EXECUTABLE_MEMBER_PARAM,
                    param.type()).varargs(isVarArg));
            htmlTree.addContent(link);
        }
        if (addDefaultValueBrackets) {
        	htmlTree.addContent("]"); //$NON-NLS-1$
        } else if (!Utils.isNullOrEmpty(defaultValue)) {
    		htmlTree.addContent(" "); //$NON-NLS-1$
    		htmlTree.addContent(Utils.getKeywords().getEqualsSignKeyword());
    		htmlTree.addContent(this.writer.getSpace());
    		htmlTree.addContent(defaultValue);
        }
	}

}
