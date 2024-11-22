package me.wisp.kirbean.utils;

import java.time.Duration;

public class Text {
    public static String codeblock(String input) {
        return "```\n" + input + "\n```";
    }

    public static String bold(String input) {
        return "**" + input + "**";
    }

    public static String spoiler(String input) {
        return "||" + input + "||";
    }

    public static String link(String text, String link) {
        return "[" + text + "]" + "(" + link + ")";
    }

    public static String rating(int upvote, int downvote) {
        return "↑" + upvote + " ↓" + downvote;
    }

    public static String prettyDuration(long millis) {
        var duration = Duration.ofMillis(millis);
        var hours = duration.toHours();
        var minutes = duration.toHours();
        var seconds = duration.toHours();

        var builder = new StringBuilder();
        if (hours != 0) {
            builder.append(hours).append(":");
        }
        if (minutes != 0) {
            builder.append(minutes).append(":");
        }
        builder.append(seconds);
        return builder.toString();
    }
}
