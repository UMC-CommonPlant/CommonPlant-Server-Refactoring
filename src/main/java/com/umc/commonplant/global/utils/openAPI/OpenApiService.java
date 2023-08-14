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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    // 날씨 API

//    private final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?";
//    private final String dataType = "pageNo=1&dataType=JSON";
//    private final String serviceKey = "&serviceKey=NCQVHIPVNbjiBU6M6d8NBB0UKhwS299XrWuFSw7N1bVxkj6mHLDNNbJi4ZhEH0IgKev0UyTOHPXXVV4dezZDpw==";
//    // private final String defaultQueryParam = "&MobileOS=ETC&MobileApp=AppTest&_type=json";
//    private final String numOfRows = "&numOfRows=10";
//    private final String baseDate = "&base_date=";
//    private final String baseTime = "&base_time=";
//    private final String nx = "&nx=";
//    private final String ny = "&ny=";
//
//    public String makeUrl(String x, String y) throws UnsupportedEncodingException {
//
//        String date = "20230202";
//        String time = "0600";
//
//        return BASE_URL + dataType + baseDate + date + baseTime + time + nx + x + serviceKey + numOfRows +ny + y;
//    }
//
//
//    public ResponseEntity<?> fetch(String url) throws UnsupportedEncodingException {
//        System.out.println(url);
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
//        ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//        System.out.println(resultMap.getBody());
//        System.out.println(resultMap.toString());
//
//        return resultMap;
//
    public PlaceInfo.GeoInfo getTime(){
        PlaceInfo.GeoInfo gi = null;
        System.out.println("test");

        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");//년월일 받아오는 부분
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH"); //현재시간 받아오는 부분
        Calendar cal = Calendar.getInstance(); //현재시간을 받아온다.

        gi.setNowDate(dateSdf.format(cal.getTime())); //날짜 세팅
        gi.setNowTime(timeSdf.format(cal.getTime())); //시간 세팅

        /* * 하루 전체의 기상예보를 받아오려면 전날 23시에 266개의 날씨정보를 호출해와야 한다. * 따라서 호출 값은 현재 날짜보다 1일전으로 세팅해줘야 한다. * */
        cal.add(Calendar.DATE,-1); //1일전 날짜를 구하기 위해 현재 날짜에서 -1 시켜주는 부분
        gi.setCallDate(dateSdf.format(cal.getTime())); //1일전 값으로 호출값 생성

        System.out.println("date : " + gi.getNowDate());
        System.out.println("time : " + gi.getNowTime());
        System.out.println("Call Date" + gi.getCallDate());
        return gi;
    }
}
