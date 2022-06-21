package me.wisp.kirbean.audio.lyrics;

import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.utils.Text;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LyricsClient {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final String GENIUS = "https://genius.com/api/search?q=";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0";

    public CompletableFuture<LyricsData> getLyrics(String search) {
        return CompletableFuture.supplyAsync(() -> {
            String url = getPageUrl(search);
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document document = null;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new LyricsData(
                    getTitle(document).ownText(),
                    url,
                    getAuthor(document).ownText(),
                    Text.chunk(getLyrics(document).wholeText(), 2048)
            );
        }, executorService);
    }

    private String getPageUrl(String search) {
        return HTTPClient.getWithQuery(GENIUS, sanitizeQuery(search))
                .get("response")
                .get("hits")
                .get(0)
                .get("result")
                .get("url")
                .asText();
    }

    // removes parenthesis and everything inside them, hacky
    private String sanitizeQuery(String search) {
        return search.replaceAll("\\(.*\\)", "");
    }

    private Element getTitle(Document document) {
        return document.selectFirst("span[class^=SongHeaderVariantdesktop__HiddenMask]");
    }

    private Element getAuthor(Document document) {
        return document.selectFirst("a[class*=SongHeaderVariantdesktop__Artist]");
    }

    private Element getLyrics(Document document) {
        return document.selectFirst("div[class^=Lyrics__Container]");
    }
}