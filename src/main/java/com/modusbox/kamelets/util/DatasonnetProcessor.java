package com.modusbox.kamelets.util;

import com.datasonnet.Mapper;
import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.MediaTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;

public class DatasonnetProcessor {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String template;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void process(final Exchange ex) {
        var jsonNodeBody = ex.getMessage().getBody(JsonNode.class);
        var script = new String(Base64.decodeBase64(template));
        var mapper = new Mapper(script);
        var transformed = mapper
                .transform(new DefaultDocument<>(jsonNodeBody.asText(), MediaTypes.APPLICATION_JSON));
        ex.getMessage().setBody(transformed);
    }
}
