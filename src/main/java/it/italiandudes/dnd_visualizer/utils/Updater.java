package it.italiandudes.dnd_visualizer.utils;

import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class Updater {

    // Attributes
    private static final String LATEST_PAGE = "https://github.com/ItalianDudes/DnD_Visualizer/releases/latest";
    private static final String LATEST_DOWNLOAD_PART = "https://github.com/ItalianDudes/DnD_Visualizer/releases/latest/download/DnD_Visualizer-";

    // Methods
    @Nullable
    public static String getLatestVersion() {
        try {
            URL url = new URL(LATEST_PAGE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getResponseCode();
            connection.disconnect();
            return connection.getURL().toString().split("/tag/")[1];
        } catch (IOException e) {
            Logger.log(e);
        }
        return null;
    }
    public static void downloadNewVersion(@NotNull final String destPath) throws IOException {
        String latestVersion = getLatestVersion();
        String downloadURL = LATEST_DOWNLOAD_PART+latestVersion+".jar";
        URL url = new URL(downloadURL);
        InputStream is = url.openConnection().getInputStream();
        Files.copy(is, Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
        is.close();
    }

}
