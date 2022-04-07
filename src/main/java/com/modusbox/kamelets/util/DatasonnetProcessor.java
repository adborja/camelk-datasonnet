package com.modusbox.kamelets.util;

import com.datasonnet.Mapper;
import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.MediaTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasonnetProcessor {
    private static final Logger log = LoggerFactory.getLogger(DatasonnetProcessor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String template;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void process(final Exchange ex) {
        log.info("Received exchange message: {}", ex.getMessage());
        log.info("Received exchange message body: {}", ex.getMessage().getBody());
        log.info("Received exchange message body class: {}", ex.getMessage().getBody().getClass());
        var jsonNodeBody = ex.getMessage().getBody(JsonNode.class);
        var script = new String(Base64.decodeBase64(template));
        var mapper = new Mapper(script);
        var transformed = mapper
                .transform(new DefaultDocument<>(jsonNodeBody.asText(), MediaTypes.APPLICATION_JSON));
        ex.getMessage().setBody(transformed);
    }
}
