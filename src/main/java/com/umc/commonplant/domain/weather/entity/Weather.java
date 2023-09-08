package com.umc.commonplant.domain.weather.entity;

import com.umc.commonplant.global.utils.openAPI.WeatherResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "weather")
@NoArgsConstructor
@Entity
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_idx")
    private Long weatherIdx;

    private String nx;
    private String ny;

    private String fcstDate; //예보일자

    private String TMX; //max temp
    private String TMN; //min temp

    private String REH00;
    private String REH01;
    private String REH02;
    private String REH03;
    private String REH04;
    private String REH05;
    private String REH06;
    private String REH07;
    private String REH08;
    private String REH09;
    private String REH10;
    private String REH11;
    private String REH12;
    private String REH13;
    private String REH14;
    private String REH15;
    private String REH16;
    private String REH17;
    private String REH18;
    private String REH19;
    private String REH20;
    private String REH21;
    private String REH22;
    private String REH23;

    @Builder
    public Weather(WeatherResult.items req) {
        this.nx = req.getItem().get(0).getNx().toString();
        this.ny = req.getItem().get(0).getNy().toString();
        this.fcstDate = req.getItem().get(0).getFcstDate();

        for(WeatherResult.item item : req.getItem()) {
            if(item.getCategory().equals("TMX"))
                this.TMX = item.getFcstValue();
            if(item.getCategory().equals("TMN"))
                this.TMN = item.getFcstValue();
            if(item.getCategory().equals("REH")){
                switch (item.getFcstTime()){
                    case "0000": this.REH00 = item.getFcstValue(); break;
                    case "0100": this.REH01 = item.getFcstValue(); break;
                    case "0200": this.REH02 = item.getFcstValue(); break;
                    case "0300": this.REH03 = item.getFcstValue(); break;
                    case "0400": this.REH04 = item.getFcstValue(); break;
                    case "0500": this.REH05 = item.getFcstValue(); break;
                    case "0600": this.REH06 = item.getFcstValue(); break;
                    case "0700": this.REH07 = item.getFcstValue(); break;
                    case "0800": this.REH08 = item.getFcstValue(); break;
                    case "0900": this.REH09 = item.getFcstValue(); break;
                    case "1000": this.REH10 = item.getFcstValue(); break;
                    case "1100": this.REH11 = item.getFcstValue(); break;
                    case "1200": this.REH12 = item.getFcstValue(); break;
                    case "1300": this.REH13 = item.getFcstValue(); break;
                    case "1400": this.REH14 = item.getFcstValue(); break;
                    case "1500": this.REH15 = item.getFcstValue(); break;
                    case "1600": this.REH16 = item.getFcstValue(); break;
                    case "1700": this.REH17 = item.getFcstValue(); break;
                    case "1800": this.REH18 = item.getFcstValue(); break;
                    case "1900": this.REH19 = item.getFcstValue(); break;
                    case "2000": this.REH20 = item.getFcstValue(); break;
                    case "2100": this.REH21 = item.getFcstValue(); break;
                    case "2200": this.REH22 = item.getFcstValue(); break;
                    case "2300": this.REH23 = item.getFcstValue(); break;
                }
            }
        }
    }



}
