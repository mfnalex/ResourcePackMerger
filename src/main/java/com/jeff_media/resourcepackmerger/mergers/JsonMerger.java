package com.jeff_media.resourcepackmerger.mergers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff_media.resourcepackmerger.PrettyObjectMapper;
import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import com.jeff_media.resourcepackmerger.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        return PrettyObjectMapper.get().writeValueAsString(map);
    }

    private static final HashSet<String> MERGEABLE_ARRAY_KEYS = new HashSet<String>(List.of("overrides", "sources", "supported_formats"));

    private static Map<String,Object> mergeMaps(Map<String, Object> oldMap, Map<String, Object> newMap) {
        Map<String,Object> map = new HashMap<>(newMap);
        for(Map.Entry<String,Object> entry : oldMap.entrySet()) {
            if(!map.containsKey(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                if(entry.getValue() instanceof Map && newMap.get(entry.getKey()) instanceof Map) {
                    map.put(entry.getKey(), mergeMaps((Map<String, Object>) oldMap.get(entry.getKey()), (Map<String, Object>) newMap.get(entry.getKey())));
                } 
                else if (newMap.get(entry.getKey()) instanceof ArrayList && MERGEABLE_ARRAY_KEYS.contains(entry.getKey())){
                    ArrayList arr = new ArrayList((ArrayList) newMap.get(entry.getKey()));
                    if (entry.getValue() instanceof ArrayList) {
                        arr.addAll((ArrayList) entry.getValue());
                    } else {
                        arr.add(entry.getValue());
                    }
                    map.put(entry.getKey(), arr);
                } else if(entry.getValue() instanceof ArrayList && MERGEABLE_ARRAY_KEYS.contains(entry.getKey())){
                    ArrayList arr = new ArrayList((ArrayList) entry.getValue());
                    arr.add(newMap.get(entry.getKey()));
                    map.put(entry.getKey(), arr);
                } else if(entry.getKey().equals("supported_formats")){
                    map.put(entry.getKey(), new ArrayList(List.of(newMap.get(entry.getKey()), entry.getValue())));
                } else if(entry.getValue() instanceof String && newMap.get(entry.getKey()) instanceof String){
                    String strOld = (String) entry.getValue();
                    String strNew = (String) newMap.get(entry.getKey());
                    if(strOld.equals(strNew)){
                        ;
                    } else if(strOld.replace("minecraft:", "").equals(strNew.replace("minecraft:", ""))){
                        ; /* kind of hacky and not quite correct, but to address and properly ignore differences like:
                          "parent" : "minecraft:block/structure_block"
                        versus
                          "parent" : "block/structure_block", */ 
                    } else {
                        ResourcePackMerger.getLogger().info(String.format("     !Warn: Unsure how to merge key %s\n       preferring %s over %s based on pack order", 
                                                            entry.getKey(), strOld, strNew));
                    }
                } else {
                    ResourcePackMerger.getLogger().info(String.format("     !Warn: Unsure how to merge key %s", entry.getKey()));
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
        try {
            return new ObjectMapper().readValue(json, HashMap.class);
        } catch (JsonParseException exception) {
            ResourcePackMerger.getLogger().warn("Failed to parse JSON: " + exception.getMessage());
            exception.printStackTrace();
            return new HashMap<>();
        }
    }
}
