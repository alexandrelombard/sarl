package io.sarl.intellij;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class SarlIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, SarlIcons.class);
    }

    public static final Icon SARL_PLUGIN = load("/icons/sarlIcon.png"); // 256x256
}
