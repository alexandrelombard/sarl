package io.sarl.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SarlFileType extends LanguageFileType {
    public static final SarlFileType INSTANCE = new SarlFileType();

    protected SarlFileType() {
        super(SarlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Sarl";
    }

    @NotNull
    @Override
    public String getDescription() {
        // TODO return SarlFileType.message("sarl.file.type.description");
        return "Sarl file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "sarl";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SarlIcons.SARL_PLUGIN;
    }
}