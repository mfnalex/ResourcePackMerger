package com.jeff_media.resourcepackmerger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class PrettyObjectMapper {

    public static final ObjectWriter get() {
        return new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

}
