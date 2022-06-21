package me.wisp.kirbean.utils;

public class Time {

    public static String formatTime(long duration) {
        long seconds = duration / 1000;
        long hours = seconds/3600;
        long minutes = seconds%3600 / 60;
        seconds %= 60;
        if (hours != 0) return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String progressbar(double percentage) {
        StringBuilder bar = new StringBuilder();
        for (int n = 0; n < 12; n++) {
            bar.append(n <= (int) (percentage*10) ? "▇" : "—");
        }
        return bar.toString();
    }
}
