package me.wisp.kirbean.audio.lyrics;

import java.util.List;

public record LyricsData(String title, String url, String author, List<String> lyrics) {
}
