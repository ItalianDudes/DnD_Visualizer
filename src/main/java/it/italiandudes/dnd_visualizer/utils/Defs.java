package it.italiandudes.dnd_visualizer.utils;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Defs {

    // Jar App Position
    public static final String JAR_POSITION = new File(DnD_Visualizer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();

    // Resources Location
    public static final class Resources {
        //Resource Getter
        public static URL get(@NotNull final String resourceConst) {
            return Objects.requireNonNull(DnD_Visualizer.class.getResource(resourceConst));
        }

        public static InputStream getAsStream(@NotNull final String resourceConst) {
            return Objects.requireNonNull(DnD_Visualizer.class.getResourceAsStream(resourceConst));
        }
    }
}
