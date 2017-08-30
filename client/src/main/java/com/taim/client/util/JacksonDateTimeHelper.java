package com.taim.client.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by dragonliu on 2017/8/28.
 */
public class JacksonDateTimeHelper {
    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private ObjectMapper objectMapper;

    public JacksonDateTimeHelper(){
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(DateTime.class, new DateTimeSerializer());
        module.addDeserializer(DateTime.class, new DatetimeDeserializer());
        objectMapper.registerModule(module);
    }

    public ObjectMapper getMapper(){
        return objectMapper;
    }


    private static class DateTimeSerializer extends JsonSerializer<DateTime> {
        @Override
        public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            //jgen.writeString(value.toString(dateFormatter));
//            String s = "\"year\": "+value.year().get()
//                    + "\"dayOfMonth\": "+value.dayOfMonth().get()
//                    + "\"dayOfWeek\": "+value.dayOfWeek().get()
//                    + "\"era\": "+value.era().get()
//                    + "\"dayOfYear\": "+value.dayOfYear().get()
//                    + "\"weekOfWeekyear\": "+value.weekOfWeekyear().get()
//                    + "\"millisOfSecond\": "+value.weekOfWeekyear().get();
//            jgen.writeString(s);
            jgen.writeString(dateFormatter.print(value));

        }
    }
    private static class DatetimeDeserializer extends JsonDeserializer<DateTime> {

        @Override
        public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            //String s = node.asText();
            long mills = node.path("millis").asLong();
            DateTime parse = new DateTime(mills);
            //DateTime parse = DateTime.parse(s, dateFormatter);
            return parse;
        }
    }
}
