package it.italiandudes.dnd_visualizer.utils;

import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import javafx.scene.image.Image;

import java.util.Calendar;

public final class CalendarEventManager {

    private static final Calendar CALENDAR = Calendar.getInstance();

    // Check for Xmas
    public static boolean isXmas() {
        int month = CALENDAR.get(Calendar.MONTH);
        int day = CALENDAR.get(Calendar.DAY_OF_MONTH);
        return month == Calendar.DECEMBER || (month == Calendar.JANUARY && day <= 8);
    }

    // Logo Selector
    public static Image getEventLogo() {
        if (isXmas()) {
            return new Image(Defs.Resources.get(JFXDefs.Resources.Image.Logo.LOGO_XMAS).toString());
        } else {
            return new Image(Defs.Resources.get(JFXDefs.Resources.Image.Logo.LOGO_MAIN).toString());
        }
    }
}
