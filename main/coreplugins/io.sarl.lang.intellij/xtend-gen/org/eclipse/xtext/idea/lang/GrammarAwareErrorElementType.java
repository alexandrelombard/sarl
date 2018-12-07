package org.eclipse.xtext.idea.lang;

import com.intellij.psi.tree.IElementType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class GrammarAwareErrorElementType extends IElementType {
  @Accessors
  private final IElementType elementType;
  
  public GrammarAwareErrorElementType(final IElementType elementType) {
    super(GrammarAwareErrorElementType.class.getName(), elementType.getLanguage(), false);
    this.elementType = elementType;
  }
  
  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�class.simpleName� [�elementType�]");
    return _builder.toString();
  }
  
  @Pure
  public IElementType getElementType() {
    return this.elementType;
  }
}
