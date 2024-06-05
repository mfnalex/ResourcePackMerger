package com.jeff_media.resourcepackmerger.data;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jeff_media.resourcepackmerger.PrettyObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonSerialize(using = Config.ConfigSerializer.class)
@JsonDeserialize(using = Config.ConfigDeserializer.class)
public class Config {

    public final String description;
    public final boolean isNewlyGenerated;
    public final String outputFile;
    public final String overrideIcon;
    public final List<String> resourcePackFiles;
    public final ResourcePackVersion resourcePackVersion;
    public final ZipCompression compression;

    public Config(String description, String outputFile, List<String> resourcePackFiles, ResourcePackVersion resourcePackVersion, ZipCompression compression, String overrideIcon) {
        this.description = description;
        this.outputFile = outputFile;
        this.resourcePackFiles = resourcePackFiles;
        this.resourcePackVersion = resourcePackVersion;
        this.compression = compression;
        this.isNewlyGenerated = false;
        this.overrideIcon = overrideIcon;
    }

    public Config() {
        this.description = "My Custom Resource Pack";
        this.outputFile = new File(System.getProperty("user.home"), "merged-resourcepack.zip").getAbsolutePath();
        this.resourcePackFiles = Collections.emptyList();
        this.resourcePackVersion = ResourcePackVersion.values()[ResourcePackVersion.values().length-1];
        this.compression = ZipCompression.NORMAL;
        this.isNewlyGenerated = true;
        this.overrideIcon = "";
    }

    public void saveFile() throws IOException {
        String json = PrettyObjectMapper.get().writeValueAsString(this);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(),false))) {
            writer.write(json);
        }
    }

    public static File getFile() {
        String dataFolder = System.getenv("APPDATA");
        if (dataFolder == null) dataFolder = System.getProperty("user.home");
        File folder = new File(dataFolder, "JEFF Media GbR");
        File file = new File(folder, "resource-pack-merger.conf");
        return file;
    }

    public static Config fromFile() throws IOException {
        try {
            File file = getFile();
            if (!file.exists()) return new Config();
            return new ObjectMapper().readValue(file, Config.class);
        } catch (Throwable t) {
            throw new IOException("Could not deserialize config file",t);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, outputFile, resourcePackFiles, resourcePackVersion, overrideIcon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(description, config.description) && Objects.equals(outputFile, config.outputFile) && Objects.equals(resourcePackFiles, config.resourcePackFiles) && resourcePackVersion == config.resourcePackVersion && Objects.equals(overrideIcon, config.overrideIcon);
    }

    public static class ConfigSerializer extends StdSerializer<Config> {

        public ConfigSerializer() {
            this(null);
        }

        public ConfigSerializer(Class<Config> t) {
            super(t);
        }

        @Override
        public void serialize(Config config, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("description", config.description);
            jsonGenerator.writeStringField("outputFile", config.outputFile);
            jsonGenerator.writeArrayFieldStart("resourcePackFiles");
            for(String fileName : config.resourcePackFiles) {
                jsonGenerator.writeObject(fileName);
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeNumberField("format", config.resourcePackVersion.getFormat());
            jsonGenerator.writeStringField("compression",config.compression.name());
            jsonGenerator.writeStringField("overrideIcon",config.overrideIcon);
            jsonGenerator.writeEndObject();
        }
    }

    public static class ConfigDeserializer extends StdDeserializer<Config> {

        public ConfigDeserializer() {
            this(null);
        }

        public ConfigDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Config deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String description = node.get("description").asText();
            String outputFile = node.get("outputFile").asText();
            List<String> resourcePackFiles = new ArrayList<>();
            node.get("resourcePackFiles").elements().forEachRemaining(element -> resourcePackFiles.add(element.asText()));
            ResourcePackVersion format = ResourcePackVersion.byFormat(node.get("format").asInt());
            ZipCompression compression = ZipCompression.valueOf(node.get("compression").asText());
            String overrideIcon = node.get("overrideIcon").asText();
            return new Config(description, outputFile, resourcePackFiles, format, compression, overrideIcon);
        }
    }

}
