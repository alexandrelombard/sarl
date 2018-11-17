package io.sarl.intellij;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SarlModuleType extends ModuleType {

    public static final SarlModuleType INSTANCE = new SarlModuleType();

    protected SarlModuleType() {
        super("sarl");
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
