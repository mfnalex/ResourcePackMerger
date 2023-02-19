package com.jeff_media.resourcepackmerger.data;

import java.util.Map;

public class Replacer {

    private final Map<String,String> map = new java.util.HashMap<>();

    public Replacer put(final String key, final String value) {
        map.put(key, value);
        return this;
    }

    public String apply(String input) {
        for(final Map.Entry<String,String> entry : map.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }

}
