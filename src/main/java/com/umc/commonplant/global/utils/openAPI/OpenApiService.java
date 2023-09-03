package com.umc.commonplant.global.utils.openAPI;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.commonplant.domain.weather.entity.Weather;
import com.umc.commonplant.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.OBJECT_MAPPER_FAIL;

@RequiredArgsConstructor
@Service
public class OpenApiService {
    private final TransLocalPoint transLocalPoint;

    @Value("${value.kakaoAPI.apiKey}")
    private String apiKey;

    @Value("${value.kakaoAPI.apiUrl}")
    private String apiUrl;

    // kakaoMapAPI - restAPI
    // 주소 위도 경도 변환
    public String getKakaoApiFromAddress(String reqAddr) {
        String jsonString = null;

        try {
            reqAddr = URLEncoder.encode(reqAddr, "UTF-8");
            String addUrl = apiUrl + "?query=" + reqAddr;
            URL url = new URL(addUrl);

            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer docJson = new StringBuffer();
            String line;
            while ((line=rd.readLine()) != null) {
                docJson.append(line);
            }
            jsonString = docJson.toString();
            rd.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    public HashMap<String, String> getXYMapfromJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> XYMap = new HashMap<String, String>();

        try {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>(){};
            Map<String, Object> jsonMap = mapper.readValue(jsonString, typeRef);

            @SuppressWarnings("unchecked")
            List<Map<String, String>> docList =  (List<Map<String, String>>) jsonMap.get("documents");

            Map<String, String> adList = (Map<String, String>) docList.get(0);
            XYMap.put("x",adList.get("x"));
            XYMap.put("y", adList.get("y"));

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return XYMap;
    }

    public HashMap<String, String> getGridXYFromAddress(String address)
    {
        String jsonString = getKakaoApiFromAddress(address);
        HashMap<String, String> xyMap = getXYMapfromJson(jsonString);

        String y = xyMap.get("x");
        String x = xyMap.get("y");

        double lat = Double.parseDouble(x);
        double lng = Double.parseDouble(y);

        PlaceInfo.LatXLngY rs =transLocalPoint.convertGRID_GPS(0, lat, lng);

        int grid_x = (int)rs.getX();
        int grid_y = (int)rs.getY();

        HashMap<String, String> xy = new HashMap<String, String>();
        xy.put("x", Integer.toString(grid_x));
        xy.put("y", Integer.toString(grid_y));
        return xy;
    }

    //===================================
    // 기상청 날씨 API

    private final ObjectMapper objectMapper;

    @Value("${value.openAPI.baseUrl}")
    private String BASE_URL;
    @Value("${value.openAPI.serviceKey}")
    private String serviceKey;
    private final String dataType = "&pageNo=1&dataType=JSON";
    private final String numOfRows = "&numOfRows=400";
    private final String baseDate = "&base_date=";
    private final String baseTime = "&base_time=";
    private final String nx = "&nx=";
    private final String ny = "&ny=";

    public String makeUrl(String x, String y){

        String yestDate = getYestDate();
        String time = "2300";

        return BASE_URL + serviceKey + numOfRows + dataType + baseDate + yestDate + baseTime + time + nx + x +ny + y;
    }


    public Weather openAPIfetch(String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        WeatherResult.items response;

        try{
            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            response = objectMapper.readValue(result.getBody(), WeatherResult.items.class);
        }catch (IOException e){
            throw new BadRequestException(OBJECT_MAPPER_FAIL);
        }

        Weather weather = new Weather(response);
        System.out.println(ToStringBuilder.reflectionToString(weather));
        return weather;
    }

    public Weather getWeather(String nx, String ny){
        Weather weather = openAPIfetch(makeUrl(nx, ny));
        return weather;
    }

    public String getYestDate()
    {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE,-1);

        return dateSdf.format(cal.getTime());
    }

    public String getNowDate()
    {
        LocalDateTime now = LocalDateTime.now();
        String currentDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return currentDate;
    }

    public int getNowTime()
    {
        LocalDateTime now = LocalDateTime.now();

        int currentHour = now.getHour();
        System.out.println("date : "+currentHour);

        return currentHour;
    }
}
