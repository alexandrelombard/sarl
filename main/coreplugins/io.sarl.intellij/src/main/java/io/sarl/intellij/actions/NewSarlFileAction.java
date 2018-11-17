package io.sarl.intellij.actions;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import io.sarl.intellij.SarlIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Properties;

public class NewSarlFileAction extends CreateFromTemplateAction<PsiFile> {

    public NewSarlFileAction() {
        super("Sarl File", null, SarlIcons.SARL_PLUGIN);
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "Creating File " + newName;
    }

    @Nullable
    @Override
    protected PsiFile createFile(String className, String templateName, PsiDirectory dir) {
        try {
            final Project project = dir.getProject();
            final Properties props = new Properties(
                    FileTemplateManager.getInstance(project).getDefaultProperties());
            props.setProperty(FileTemplate.ATTRIBUTE_NAME, className);

            final FileTemplate template = FileTemplateManager.getInstance(project).getInternalTemplate(templateName);

            final PsiElement element = FileTemplateUtil.createFromTemplate(template, className, props, dir, NewSarlFileAction.class.getClassLoader());

            return element.getContainingFile();
        }
        catch (Exception e) {
            throw new IncorrectOperationException(e.getMessage(), e);
        }
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle(IdeBundle.message("action.create.new.class"));

        for (FileTemplate fileTemplate : SarlFileTemplateUtil.getApplicableTemplates(project)) {
            final String templateName = fileTemplate.getName();
            final String shortName = SarlFileTemplateUtil.getTemplateShortName(templateName);
            final Icon icon = SarlIcons.SARL_PLUGIN;
            builder.addKind(shortName, icon, templateName);
        }

        builder.setValidator(new InputValidatorEx() {
            @Override
            public String getErrorText(String inputString) {
                if (inputString.length() > 0 && !StringUtil.isJavaIdentifier(inputString)) {
                    return "This is not a valid Sarl name";
                }
                return null;
            }

            @Override
            public boolean checkInput(String inputString) {
                return true;
            }

            @Override
            public boolean canClose(String inputString) {
                return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
            }
        });
    }
}
