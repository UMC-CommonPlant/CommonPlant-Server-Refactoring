package com.umc.commonplant.global.utils.openAPI;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.commonplant.domain.weather.entity.Weather;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WeatherDeserializer extends JsonDeserializer<WeatherResult.items> {

    private final ObjectMapper objectMapper;

    public WeatherDeserializer()
    {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WeatherResult.items deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);
        JsonNode responseNode = node.findValue("response");

        JsonNode itemNode = responseNode.get("body").get("items").get("item");
        List<WeatherResult.item> items = Arrays.stream(objectMapper.treeToValue(itemNode, WeatherResult.item[].class)).collect(Collectors.toList());

        //TMX(최고온도), TMN(최저온도), REH(시간별 습도)
        List<WeatherResult.item> map = new ArrayList<>();
        for(WeatherResult.item item : items){
            if(item.getCategory().equals("TMX"))
                map.add(item);
            if(item.getCategory().equals("TMN"))
                map.add(item);
            if(item.getCategory().equals("REH")) {
                map.add(item);
            }
        }
        return new WeatherResult.items(map);
    }
}
