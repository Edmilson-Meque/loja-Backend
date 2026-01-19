package com.ecommerce.ecommerce.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

public class JsonHtmlXssSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value != null) {
            String encoded = HtmlUtils.htmlEscape(value);
            gen.writeString(encoded);
        } else {
            gen.writeNull();
        }
    }
}
