package com.jaus.albertogiunta.justintrain_oraritreni.networking;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class DateTimeAdapter implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Timestamp format
        // "2012-06-11T20:06+00200" format
        return DateTime.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(DateTime time, Type typeOfT, JsonSerializationContext context) {
        return new JsonPrimitive(time.toString("yyyy-MM-dd'T'HH:mmZ"));
    }
}
