package io.sarl.intellij;

import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.framework.detection.impl.FrameworkDetectionManager;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportUtil;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class SarlModuleBuilder extends ModuleBuilder {

    public static final SarlModuleBuilder INSTANCE = new SarlModuleBuilder();

    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        // region Initialize SDK
        if (myJdk != null){
            modifiableRootModel.setSdk(myJdk);
        } else {
            modifiableRootModel.inheritSdk();
        }
        // endregion

        // region Initialize the project structure
        final ContentEntry contentEntry = doAddContentEntry(modifiableRootModel);
        if(contentEntry != null) {
            final VirtualFile baseDir = contentEntry.getFile();
            if (baseDir != null) {
                final Collection<VirtualFile> filesToOpen;
                try {
                    filesToOpen = SarlProjectTemplate.DEFAULT.generateProject(modifiableRootModel.getModule(), baseDir);

                    if(!filesToOpen.isEmpty()) {
                        final FileEditorManager manager = FileEditorManager.getInstance(modifiableRootModel.getProject());
                        for (VirtualFile file : filesToOpen) {
                            manager.openFile(file, true);
                        }
                    }
                } catch (IOException e) {
                    Logger.getLogger(getName())
                            .severe("Unable to generate the SARL template project. Cause: " + e.getMessage());
                }
            }

            contentEntry.addSourceFolder("src/main/sarl", false);
            contentEntry.addSourceFolder("src/test/sarl", true);
        }
        // endregion



        // region Add maven framework
        final List<FrameworkSupportInModuleProvider> frameworkSupportProviderList = FrameworkSupportUtil.getAllProviders();
        // endregion
    }

    @Override
    public ModuleType getModuleType() {
        return SarlModuleType.INSTANCE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(
            @NotNull WizardContext wizardContext,
            @NotNull ModulesProvider modulesProvider) {
        final ModuleWizardStep[] moduleWizardSteps = super.createWizardSteps(wizardContext, modulesProvider);
        return moduleWizardSteps;
    }
}
