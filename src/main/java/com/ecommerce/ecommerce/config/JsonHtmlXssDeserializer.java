package com.ecommerce.ecommerce.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

public class JsonHtmlXssDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String value = p.getValueAsString();
        return value != null ? HtmlUtils.htmlEscape(value) : null;
    }
}