package me.wisp.kirbean.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Json utility class to create reader objects. The ObjectMapper itself is not
 * modifiable and is only used to create ObjectReaders.
 */
public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a ObjectReader of the given class
     * @param type class
     * @return ObjectReader of given reference type
     */
    public static ObjectReader readerFor(Class<?> type) {
        return mapper.readerFor(type);
    }

    /**
     * Creates a ObjectReader of the given reference. Useful for Collections
     * holding generics
     * @see Json#readerFor(Class)
     */
    public static ObjectReader readerFor(TypeReference<?> reference) {
        return mapper.readerFor(reference);
    }

    /**
     * Creates a ObjectReader of the given class in a list
     * @param type class
     * @return ObjectReader of a list of given class
     */
    public static ObjectReader readerForListOf(Class<?> type) {
        return mapper.readerForListOf(type);
    }

    /**
     * Creates a ObjectReader of the given class in an array
     * @param type class
     * @return ObjectReader of an array of given class
     */
    public static ObjectReader readerForArrayOf(Class<?> type) {
        return mapper.readerForArrayOf(type);
    }

    /**
     * Converts json string into node. Prefer ObjectReaders when possible.
     * This is here only for convenience.
     * @param json json string
     * @return traversable json node
     */
    public static JsonNode readTree(String json) throws JsonProcessingException {
        return mapper.readTree(json);
    }
}
