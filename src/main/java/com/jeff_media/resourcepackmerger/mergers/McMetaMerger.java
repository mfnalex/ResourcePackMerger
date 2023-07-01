package com.jeff_media.resourcepackmerger.mergers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff_media.resourcepackmerger.PrettyObjectMapper;
import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;

public class McMetaMerger {

    public static void apply(File file, String description, ResourcePackVersion version) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<String, Object> map;
            Map<String, Object> pack;
            try {
                map = (Map<String, Object>) new ObjectMapper().readValue(file, Map.class);
                pack = (Map<String, Object>) map.get("pack");
            } catch (Throwable ignored) {
                map = new java.util.HashMap<>();
                pack = new java.util.HashMap<>();
            }
            pack.put("description", description);
            pack.put("pack_format", version.getFormat());
            map.put("pack", pack);
            String json = PrettyObjectMapper.get().writeValueAsString(map);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(json);
            }
        } catch (Throwable ignored) {

        }
    }

}
