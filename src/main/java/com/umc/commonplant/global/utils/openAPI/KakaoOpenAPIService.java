package com.umc.commonplant.global.utils.openAPI;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOpenAPIService {
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
}
