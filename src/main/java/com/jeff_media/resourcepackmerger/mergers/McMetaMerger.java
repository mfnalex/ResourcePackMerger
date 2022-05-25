package com.jeff_media.resourcepackmerger.mergers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;

public class McMetaMerger {

    public static void apply(File file, String description, ResourcePackVersion version) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<String, Object> map = (Map<String, Object>) new ObjectMapper().readValue(file, Map.class);
            Map<String, Object> pack = (Map<String, Object>) map.get("pack");
            pack.put("description", description);
            pack.put("pack_format", version.getFormat());
            map.put("pack", pack);
            String json = new ObjectMapper().writeValueAsString(map);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(json);
            }
        }
    }

}
