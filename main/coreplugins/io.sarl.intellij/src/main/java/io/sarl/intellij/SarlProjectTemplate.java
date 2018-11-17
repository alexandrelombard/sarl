package io.sarl.intellij;

import com.google.common.io.Files;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl;
import com.intellij.spellchecker.FileLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public abstract class SarlProjectTemplate {
    public abstract @NotNull Collection<VirtualFile> generateProject(@NotNull final Module module,
                                                            @NotNull final VirtualFile baseDir)
            throws IOException;

    public static final SarlProjectTemplate DEFAULT = new SarlProjectTemplate() {
        @Override
        public @NotNull Collection<VirtualFile> generateProject(
                @NotNull Module module,
                @NotNull VirtualFile baseDir) throws IOException {
            final List<VirtualFile> fileList = new ArrayList<>();

            // region Create directories
            final File mainSarl = new File(baseDir.getPath(), "src/main/sarl");
            mainSarl.mkdirs();
            final File testSarl = new File(baseDir.getPath(), "src/test/sarl");
            testSarl.mkdirs();
            refresh(mainSarl);
            refresh(testSarl);
            // endregion

            // region Create the POM file
            final File pom = new File(baseDir.getPath(), "pom.xml");
            final boolean success = pom.createNewFile();

            if(success) {
                try(InputStream is = getClass().getResourceAsStream("/module/pom.xml"); FileOutputStream fos = new FileOutputStream(pom)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                }

                fileList.add(LocalFileSystem.getInstance().refreshAndFindFileByIoFile(pom));
            }
            // endregion

            return fileList;
        }
    };

    private static VirtualFile refresh(File file) {
        return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
    }
}
