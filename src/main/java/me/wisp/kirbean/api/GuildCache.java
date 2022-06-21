package me.wisp.kirbean.api;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class GuildCache {
    private final ConcurrentMap<Long, ArrayDeque<JsonNode>> cache = new ConcurrentHashMap<>();
    private final Supplier<ArrayDeque<JsonNode>> supplier;

    public GuildCache(Supplier<ArrayDeque<JsonNode>> supplier) {
       this.supplier = supplier;
    }

    public JsonNode get(long guildId) {
        ArrayDeque<JsonNode> cache = getCache(guildId);
        JsonNode response = cache.pollFirst();
        if (response == null) {
            cache.addAll(supplier.get());
            return cache.pollFirst();
        }
        return response;
    }

    public ArrayDeque<JsonNode> getCache(long guildid) {
        return cache.computeIfAbsent(guildid, id -> supplier.get());
    }
}
