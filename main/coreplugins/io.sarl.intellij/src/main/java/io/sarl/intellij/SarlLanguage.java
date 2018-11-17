package io.sarl.intellij;

import com.intellij.lang.Language;

public class SarlLanguage extends Language {

    public static final SarlLanguage INSTANCE = new SarlLanguage();

    protected SarlLanguage() {
        super("Sarl", "text/x-sarl");
    }
}
