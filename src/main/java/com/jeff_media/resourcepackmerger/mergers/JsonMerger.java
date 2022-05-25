package com.jeff_media.resourcepackmerger.mergers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff_media.resourcepackmerger.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonMerger {

    public static void merge(File existingFile, File newFile) throws IOException {
        Map<String,Object> oldMap = convertJsonToMap(existingFile);
        Map<String,Object> newMap = convertJsonToMap(newFile);
        Map<String,Object> mergedMap = mergeMaps(oldMap, newMap);

        String mergedJson = convertMapToJson(mergedMap);
        Files.write(existingFile.toPath(), Collections.singletonList(mergedJson), StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

//    public static String merge(String oldJson, String newJson) throws JsonProcessingException {
//        Map<String,Object> oldMap = convertJsonToMap(oldJson);
//        Map<String,Object> newMap = convertJsonToMap(newJson);
//        return new ObjectMapper().writeValueAsString(mergeMaps(oldMap, newMap));
//    }

    private static String convertMapToJson(Map<String,Object> map) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(map);
    }

    private static Map<String,Object> mergeMaps(Map<String, Object> oldMap, Map<String, Object> newMap) {
        Map<String,Object> map = new HashMap<>(newMap);
        for(Map.Entry<String,Object> entry : oldMap.entrySet()) {
            if(!map.containsKey(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                if(entry.getValue() instanceof Map && newMap.get(entry.getKey()) instanceof Map) {
                    newMap.put(entry.getKey(), mergeMaps((Map<String, Object>) oldMap.get(entry.getKey()), (Map<String, Object>) newMap.get(entry.getKey())));
                }
            }
        }
        return map;
    }

    private static Map<String, Object> convertJsonToMap(File file) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
        ) {
            return convertJsonToMap(reader.lines().collect(Collectors.joining(System.lineSeparator())));
        }
    }

    private static Map<String, Object> convertJsonToMap(String json) throws JsonProcessingException {
            return new ObjectMapper().readValue(json, HashMap.class);
    }
}
