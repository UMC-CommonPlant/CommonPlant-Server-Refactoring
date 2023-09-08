package com.umc.commonplant.domain.weather.service;

import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.weather.entity.Weather;
import com.umc.commonplant.domain.weather.entity.WeatherRepository;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.utils.openAPI.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.GET_HUMIDITY_FAIL;

@RequiredArgsConstructor
@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final OpenApiService openApiService;

    public PlaceDto.getWeatherRes getPlaceWeather(PlaceDto.getPlaceGridRes req) {
        Weather weather = getWeather(req.getNx(), req.getNy());
        String humidity = getNowHumidity(weather);
        return PlaceDto.getWeatherRes.builder().maxTemp(weather.getTMX()).minTemp(weather.getTMN()).humidity(humidity).build();
    }

    @Transactional
    public Weather getWeather(String gridX, String gridY) {
        Weather weather = weatherRepository.findByGrid(gridX, gridY, openApiService.getNowDate());
        if(weather != null)
            return weather;
        weather = weatherRepository.save(openApiService.getWeather(gridX, gridY));
        return weather;
    }

    public String getNowHumidity(Weather req){
        int nowTime = openApiService.getNowTime();
        switch (nowTime)
        {
            case 0: return req.getREH00();
            case 1: return req.getREH01();
            case 2: return req.getREH02();
            case 3: return req.getREH03();
            case 4: return req.getREH04();
            case 5: return req.getREH05();
            case 6: return req.getREH06();
            case 7: return req.getREH07();
            case 8: return req.getREH08();
            case 9: return req.getREH09();
            case 10: return req.getREH10();
            case 11: return req.getREH11();
            case 12: return req.getREH12();
            case 13: return req.getREH13();
            case 14: return req.getREH14();
            case 15: return req.getREH15();
            case 16: return req.getREH16();
            case 17: return req.getREH17();
            case 18: return req.getREH18();
            case 19: return req.getREH19();
            case 20: return req.getREH20();
            case 21: return req.getREH21();
            case 22: return req.getREH22();
            case 23: return req.getREH23();
        }
        throw new BadRequestException(GET_HUMIDITY_FAIL);
    }


}
