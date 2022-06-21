package me.wisp.kirbean.utils;

import java.util.ArrayList;
import java.util.List;

public class Text {

    public static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    // might be a better way
    public static List<String> chunk(String text, int chunkSize) {
        int length = text.length();
        List<String> chunks = new ArrayList<>(length + chunkSize - 1 / chunkSize);
        for (int i = 0; i < length; i+=chunkSize) {
            chunks.add(text.substring(i, Math.min(length, i + chunkSize)));
        }

        return chunks;
    }

    public static String rating(int upvote, int downvote) {
        return "↑ " + upvote + "  ↓ " + downvote;
    }
}
