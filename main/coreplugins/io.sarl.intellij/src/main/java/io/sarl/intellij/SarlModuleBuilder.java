package io.sarl.intellij;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;

public class SarlModuleBuilder extends ModuleBuilder {

    public static final SarlModuleBuilder INSTANCE = new SarlModuleBuilder();

    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {

    }

    @Override
    public ModuleType getModuleType() {
        return SarlModuleType.INSTANCE;
    }
}
