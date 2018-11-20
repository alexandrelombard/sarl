package io.sarl.intellij.actions

import com.intellij.ide.IdeBundle
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.actions.CreateFromTemplateAction
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import io.sarl.intellij.SarlIcons

import javax.swing.*
import java.util.Properties

class NewSarlFileAction : CreateFromTemplateAction<PsiFile>("Sarl File", null, SarlIcons.SARL_PLUGIN) {

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String {
        return "Creating File $newName"
    }

    override fun createFile(className: String, templateName: String, dir: PsiDirectory): PsiFile? {
        try {
            val project = dir.project
            val props = Properties(
                    FileTemplateManager.getInstance(project).defaultProperties)
            props.setProperty(FileTemplate.ATTRIBUTE_NAME, className)

            val template = FileTemplateManager.getInstance(project).getInternalTemplate(templateName)

            val element = FileTemplateUtil.createFromTemplate(template, className, props, dir, NewSarlFileAction::class.java.classLoader)

            return element.containingFile
        } catch (e: Exception) {
            throw IncorrectOperationException(e.message, e)
        }

    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(IdeBundle.message("action.create.new.class"))

        for (fileTemplate in SarlFileTemplateUtil.getApplicableTemplates(project)) {
            val templateName = fileTemplate.name
            val shortName = SarlFileTemplateUtil.getTemplateShortName(templateName)
            val icon = SarlIcons.SARL_PLUGIN
            builder.addKind(shortName, icon, templateName)
        }

        builder.setValidator(object : InputValidatorEx {
            override fun getErrorText(inputString: String): String? {
                return if (inputString.length > 0 && !StringUtil.isJavaIdentifier(inputString)) {
                    "This is not a valid Sarl name"
                } else null
            }

            override fun checkInput(inputString: String): Boolean {
                return true
            }

            override fun canClose(inputString: String): Boolean {
                return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null
            }
        })
    }
}
