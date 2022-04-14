package com.modusbox.kamelets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractPropertiesProcessor {
    private static final Logger log = LoggerFactory.getLogger(ExtractPropertiesProcessor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    private String property;

    public void setProperty(final String property) {
        this.property = property;
    }

    public void process(final Exchange ex) throws JsonProcessingException {
        log.debug("property json: {}", property);
        var jsonNode = MAPPER.readTree(property);
        log.debug("JsonNode: {}", jsonNode);
        var iterator = jsonNode.fields();
        iterator.forEachRemaining(node -> {
            log.info("setting property: {} -> {}", node.getKey(), node.getValue().asText(""));
            ex.getProperties().put(node.getKey(), node.getValue().asText(""));
        });
        log.info("exchange props: {}", ex.getProperties() );
    }
}
