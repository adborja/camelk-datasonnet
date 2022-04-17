package com.modusbox.kamelets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.support.ResourceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class ExtractPropertiesProcessor {
    private static final Logger log = LoggerFactory.getLogger(ExtractPropertiesProcessor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    private String property;

    private String secrets;

    private String[] secretsArray;

    public void setSecrets(String secrets) {
        this.secrets = secrets;
        this.secretsArray = secrets.split(",");
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    public void process(final Exchange exchange) throws IOException {
        try {
            log.info("secrets: {}", secretsArray[0]);
            var scheme = ResourceHelper.getScheme(secretsArray[0]);
            log.info("scheme: {}", scheme);
            var is = ResourceHelper.resolveResourceAsInputStream(exchange.getContext(), secretsArray[0]);
            log.info("--> IS: {}", is);
            log.info("property json: {}", property);
            var jsonNode = MAPPER.readTree(property);
            System.out.println(jsonNode.getClass());
            var iterator = jsonNode.fields();
            iterator.forEachRemaining(node -> {
                log.info("setting property: {} -> {}", node.getKey(), node.getValue().asText(""));
                exchange.getProperties().put(node.getKey(), node.getValue().asText(""));
                if (exchange.getContext().getGlobalOptions() != null) {
                    exchange.getContext().getGlobalOptions().put(node.getKey(), node.getValue().asText());
                    log.info("property {} added to camelcontext", node.getKey());
                }
            });
            log.info("exchange props: {}", exchange.getProperties() );
        } catch (JsonProcessingException ex) {
            log.error("error processing property: {}", ex.getMessage());
        }
    }
}
