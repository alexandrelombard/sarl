package io.sarl.intellij;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SarlModuleType extends ModuleType {

    private static final String ID = "SARL_MODULE_TYPE";

    public static final SarlModuleType INSTANCE = new SarlModuleType();

//    public static SarlModuleType getInstance() {
//        return (SarlModuleType) ModuleTypeManager.getInstance().findByID(ID);
//    }

    protected SarlModuleType() {
        super(ID);
    }

    @NotNull
    @Override
    public ModuleBuilder createModuleBuilder() {
        return SarlModuleBuilder.INSTANCE;
    }

    @NotNull
    @Override
    public String getName() {
        return "Sarl";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Sarl module";
    }

    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return SarlIcons.SARL_PLUGIN;
    }
}
