/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.editorActions;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.eclipse.xtext.idea.editorActions.IdeaAutoEditHandler;
import org.eclipse.xtext.idea.editorActions.IdeaAutoEditHandlerExtension;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class XtextAutoEditTypedHandler extends TypedHandlerDelegate {
  @Override
  public TypedHandlerDelegate.Result beforeCharTyped(final char c, final Project project, final Editor editor, final PsiFile file, final FileType fileType) {
    final IdeaAutoEditHandler autoEditHandler = IdeaAutoEditHandlerExtension.INSTANCE.forLanguage(file.getLanguage());
    if ((autoEditHandler == null)) {
      return super.beforeCharTyped(c, project, editor, file, fileType);
    }
    return this.translateResult(autoEditHandler.beforeCharTyped(c, project, ((EditorEx) editor), file, fileType));
  }
  
  @Override
  public TypedHandlerDelegate.Result charTyped(final char c, final Project project, final Editor editor, final PsiFile file) {
    final IdeaAutoEditHandler autoEditHandler = IdeaAutoEditHandlerExtension.INSTANCE.forLanguage(file.getLanguage());
    if ((autoEditHandler == null)) {
      return super.charTyped(c, project, editor, file);
    }
    return this.translateResult(autoEditHandler.charTyped(c, project, ((EditorEx) editor), file));
  }
  
  protected TypedHandlerDelegate.Result translateResult(final IdeaAutoEditHandler.Result result) {
    TypedHandlerDelegate.Result _switchResult = null;
    if (result != null) {
      switch (result) {
        case DEFAULT:
          _switchResult = TypedHandlerDelegate.Result.DEFAULT;
          break;
        case CONTINUE:
          _switchResult = TypedHandlerDelegate.Result.CONTINUE;
          break;
        case STOP:
          _switchResult = TypedHandlerDelegate.Result.STOP;
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }
}
