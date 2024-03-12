package com.umc.commonplant.domain.calendar.service;

import com.umc.commonplant.domain.memo.dto.MemoDto;
import com.umc.commonplant.domain.memo.service.MemoService;
import com.umc.commonplant.domain.place.dto.PlaceDto;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.plant.service.PlantService;
import com.umc.commonplant.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarService {

    private final PlaceService placeService;
    private final PlantService plantService;
    private final MemoService memoService;

    // TODO: 날짜 별 식물 불러오기

    @Transactional(readOnly = true)
    public List<PlaceDto.getMyCalendarPlaceListRes> getMyCalendarPlaceList(User user) {
        return placeService.getPlaceNameListByUser(user);
    }

    @Transactional(readOnly = true)
    public List<PlantDto.getMyCalendarPlantListRes> getMyCalendarPlantList(User user, String placeCode) {
        return plantService.getPlantListByPlace(user, placeCode);
    }

    @Transactional(readOnly = true)
    public List<MemoDto.GetAllMemo> getMyCalendarMemoList(Long plantIdx){
        return memoService.getAllMemoByPlant(plantIdx);
    }
}
