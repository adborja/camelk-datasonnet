package com.modusbox.kamelets.util;

import com.datasonnet.Mapper;
import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.MediaTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DatasonnetProcessor {
    private static final Logger log = LoggerFactory.getLogger(DatasonnetProcessor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String template;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void process(final Exchange ex) throws Exception {
        var script = new String(Base64.decodeBase64(template));
        var jsonNodeBody = MAPPER.valueToTree(ex.getMessage().getBody());
        var mapper = new Mapper(script);
        var transformed = mapper
                .transform(new DefaultDocument<>(jsonNodeBody.toString(), MediaTypes.APPLICATION_JSON));
        ex.setProperty(Exchange.CONTENT_TYPE, transformed.getMediaType());
        var content = transformed.getContent();
        var contentMap = MAPPER.readValue(content, Map.class);
        ex.getMessage().setBody(contentMap);
    }
}
