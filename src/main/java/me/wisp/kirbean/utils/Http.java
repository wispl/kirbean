package me.wisp.kirbean.utils;

import net.dv8tion.jda.api.JDA;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utility class to simplify http get requests with JDA's internal
 * OkHttp client. The client should hopefully stay exposed in
 * future versions. The methods here are all synchronous and blocking!
 */
public class Http {
    private static final Logger logger = LoggerFactory.getLogger(Http.class);

    /**
     * Same as {@link Http#execute(JDA jda, Request request)} but with string url
     * @see Http#execute(JDA jda, Request request)
     */
    public static String execute(JDA jda, String url) {
        var request = new Request.Builder().url(url).build();
        return execute(jda, request);
    }

    /**
     * Same as {@link Http#execute(JDA jda, Request request)} but with http url
     * @see Http#execute(JDA jda, Request request)
     */
    public static String execute(JDA jda, HttpUrl url) {
        var request = new Request.Builder().url(url).build();
        return execute(jda, request);
    }

    /**
     * Sends a get request to the given url using JDA's internal client.
     * This is a BLOCKING action and must be done in another thread thread.
     * @param jda JDA client
     * @param request OKHttp request
     * @return body of get request as a string
     */
    public static String execute(JDA jda, Request request) {
        try {
            return jda.getHttpClient()
                    .newCall(request)
                    .execute()
                    .body()
                    .string();
        } catch (IOException e) {
            logger.error("Http client error: " + e.getMessage());
            return null;
        }
    }
}
