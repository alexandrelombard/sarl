/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.execution;

import com.google.inject.Inject;
import com.intellij.execution.filters.ExceptionFilter;
import com.intellij.execution.filters.FileHyperlinkInfo;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.DocumentUtil;
import org.eclipse.xtext.idea.shared.IdeaSharedInjectorProvider;
import org.eclipse.xtext.idea.trace.IIdeaTrace;
import org.eclipse.xtext.idea.trace.ILocationInVirtualFile;
import org.eclipse.xtext.idea.trace.ITraceForVirtualFileProvider;
import org.eclipse.xtext.idea.trace.VirtualFileInProject;
import org.eclipse.xtext.util.TextRegion;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TraceBasedExceptionFilter extends ExceptionFilter {
  @Inject
  private ITraceForVirtualFileProvider traceProvider;
  
  public TraceBasedExceptionFilter(final GlobalSearchScope scope) {
    super(scope);
    IdeaSharedInjectorProvider.injectMembers(this);
  }
  
  @Override
  public Filter.Result applyFilter(final String line, final int textEndOffset) {
    final Filter.Result result = super.applyFilter(line, textEndOffset);
    if ((result == null)) {
      return null;
    }
    final Function1<Filter.ResultItem, Boolean> _function = (Filter.ResultItem it) -> {
      HyperlinkInfo _hyperlinkInfo = it.getHyperlinkInfo();
      return Boolean.valueOf((_hyperlinkInfo != null));
    };
    final Filter.ResultItem resultItem = IterableExtensions.<Filter.ResultItem>findFirst(result.getResultItems(), _function);
    final HyperlinkInfo hyperlinkInfo = resultItem.getHyperlinkInfo();
    if ((hyperlinkInfo instanceof FileHyperlinkInfo)) {
      final OpenFileDescriptor descriptor = ((FileHyperlinkInfo)hyperlinkInfo).getDescriptor();
      if ((descriptor != null)) {
        VirtualFile _file = descriptor.getFile();
        Project _project = descriptor.getProject();
        final VirtualFileInProject fileInProject = new VirtualFileInProject(_file, _project);
        boolean _isGenerated = this.traceProvider.isGenerated(fileInProject);
        if (_isGenerated) {
          final IIdeaTrace trace = this.traceProvider.getTraceToSource(fileInProject);
          final RangeMarker rangeMarker = descriptor.getRangeMarker();
          if (((trace != null) && (rangeMarker != null))) {
            final Computable<Integer> _function_1 = () -> {
              int _xblockexpression = (int) 0;
              {
                final Document document = FileDocumentManager.getInstance().getDocument(descriptor.getFile());
                final int lineNumber = document.getLineNumber(rangeMarker.getStartOffset());
                _xblockexpression = DocumentUtil.getFirstNonSpaceCharOffset(document, lineNumber);
              }
              return Integer.valueOf(_xblockexpression);
            };
            final Integer nonSpaceCharOffset = ApplicationManager.getApplication().<Integer>runReadAction(_function_1);
            TextRegion _textRegion = new TextRegion((nonSpaceCharOffset).intValue(), 0);
            final ILocationInVirtualFile location = trace.getBestAssociatedLocation(_textRegion);
            if ((location != null)) {
              Project _project_1 = location.getPlatformResource().getProject();
              VirtualFile _file_1 = location.getPlatformResource().getFile();
              int _lineNumber = location.getTextRegion().getLineNumber();
              final OpenFileDescriptor sourceFileDescriptor = new OpenFileDescriptor(_project_1, _file_1, _lineNumber, 
                0);
              final OpenFileHyperlinkInfo linkInfo = new OpenFileHyperlinkInfo(sourceFileDescriptor);
              int _highlightStartOffset = resultItem.getHighlightStartOffset();
              int _highlightEndOffset = resultItem.getHighlightEndOffset();
              TextAttributes _highlightAttributes = resultItem.getHighlightAttributes();
              return new Filter.Result(_highlightStartOffset, _highlightEndOffset, linkInfo, _highlightAttributes);
            }
          }
        }
      }
    }
    return result;
  }
}
