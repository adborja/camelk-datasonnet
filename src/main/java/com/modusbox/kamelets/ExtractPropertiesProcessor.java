package com.modusbox.kamelets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;

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

    public void process(final Exchange exchange)  {
        try {
            log.info("property json: {}", property);
            var jsonNode = MAPPER.readTree(property);
            log.info("JsonNode: {}", jsonNode.getClass());
            System.out.println(jsonNode.getClass());
            var iterator = jsonNode.fields();
            iterator.forEachRemaining(node -> {
                log.info("setting property: {} -> {}", node.getKey(), node.getValue().asText(""));
                exchange.getProperties().put(node.getKey(), node.getValue().asText(""));
                if (exchange.getContext().getPropertiesComponent() != null) {
                    var optProps = Optional.ofNullable(exchange.getContext()
                            .getPropertiesComponent()
                            .getLocalProperties());
                    optProps.ifPresent(properties -> {
                        properties.setProperty(node.getKey(), node.getValue().asText());
                        log.info("property {} added to camelcontext", node.getKey());
                    });
                }
            });
            log.info("exchange props: {}", exchange.getProperties() );
        } catch (JsonProcessingException ex) {
            log.error("error processing property: {}", ex.getMessage());
        }
    }
}
