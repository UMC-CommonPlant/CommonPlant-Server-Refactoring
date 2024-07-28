package com.umc.commonplant.domain.calendar.service;

import com.umc.commonplant.domain.belong.entity.BelongRepository;
import com.umc.commonplant.domain.calendar.dto.CalendarDto;
import com.umc.commonplant.domain.memo.entity.Memo;
import com.umc.commonplant.domain.memo.entity.MemoRepository;
import com.umc.commonplant.domain.memo.service.MemoService;
import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.place.entity.PlaceRepository;
import com.umc.commonplant.domain.place.service.PlaceService;
import com.umc.commonplant.domain.plant.dto.PlantDto;
import com.umc.commonplant.domain.plant.entity.Plant;
import com.umc.commonplant.domain.plant.entity.PlantRepository;
import com.umc.commonplant.domain.plant.service.PlantService;
import com.umc.commonplant.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarService {

    private final PlaceRepository placeRepository;
    private final PlantRepository plantRepository;
    private final MemoRepository memoRepository;
    private final BelongRepository belongRepository;

    private final PlaceService placeService;
    private final PlantService plantService;
    private final MemoService memoService;

    @Transactional(readOnly = true)
    public CalendarDto.getMyCalendarRes getMyCalendar(User user, String year, String month){
        int parsedYear = Integer.parseInt(year);
        int parsedMonth = Integer.parseInt(month);

        YearMonth yearMonth = YearMonth.of(parsedYear, parsedMonth);

        int lengthOfMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDateTime firstDate = startDate.atStartOfDay();
        LocalDateTime lastDate = endDate.atTime(LocalTime.MAX);

        // TODO: 날짜 정보 별로 각 Repo에 boolean으로 접근해서 정보 설정
        // TODO: 식물을 처음 데려온 날
        List<LocalDateTime> createdAtOfPlantList = plantRepository.getDateListOfPlant(firstDate, lastDate);
        List<Boolean> joinPlantList = new ArrayList<>(Collections.nCopies(lengthOfMonth + 1, false));

        for (LocalDateTime createdAt : createdAtOfPlantList) {
            int date = createdAt.toLocalDate().getDayOfMonth();

            joinPlantList.set(date, true);
        }

        // TODO: 월 정보 반환
        List<CalendarDto.getMyCalendarEventRes> getMyCalendarEventList = new ArrayList<>();

        for(int parsedDate = 1; parsedDate <= lengthOfMonth; parsedDate++){
            CalendarDto.getMyCalendarEventRes getMyCalendarEventRes = CalendarDto.getMyCalendarEventRes.builder()
                    .parsedDate(parsedDate)
                    .joinPlant(joinPlantList.get(parsedDate))
                    .prevWatered(false)
                    .nextWatered(false)
                    .writeMemo(false)
                    .build();

            getMyCalendarEventList.add(getMyCalendarEventRes);
        }

        CalendarDto.getMyCalendarRes getMyCalendarRes = CalendarDto.getMyCalendarRes.builder()
                .year(year)
                .month(month)
                .dateList(getMyCalendarEventList)
                .build();

        return getMyCalendarRes;
    }

    @Transactional(readOnly = true)
    public List<CalendarDto.getMyCalendarPlaceListRes> getPlaceNameListByUser(User user){
        List<Place> placeList = belongRepository.getPlaceListByUser(user.getUuid());

        List<CalendarDto.getMyCalendarPlaceListRes> placeNameList = new ArrayList<>();
        for(Place p: placeList){
            CalendarDto.getMyCalendarPlaceListRes placeName = new CalendarDto.getMyCalendarPlaceListRes(p.getName());
            placeNameList.add(placeName);
        }

        return placeNameList;
    }

    @Transactional(readOnly = true)
    public List<CalendarDto.getMyCalendarPlantListRes> getPlantListByPlace(User user, String placeCode){
        Place plantPlace = placeService.getPlaceByCode(placeCode);

        List<Plant> plants = plantRepository.findAllByPlace(plantPlace);

        List<CalendarDto.getMyCalendarPlantListRes> plantList = new ArrayList<>();

        for(Plant plant: plants){
            plantList.add(new CalendarDto.getMyCalendarPlantListRes(plant));
        }

        return plantList;
    }

    @Transactional(readOnly = true)
    public List<CalendarDto.getMyCalendarMemoRes> getAllMemoByPlant(Long plantIdx) {
        List<Memo> existingMemo = memoRepository.findByPlantIdx(plantIdx);
        List<Memo> sortedMemos = existingMemo.stream()
                .sorted(Comparator.comparing(Memo::getCreatedAt).reversed())
                .collect(Collectors.toList());

        List<CalendarDto.getMyCalendarMemoRes> memoResponseList = new ArrayList<>();

        for(Memo memo : sortedMemos) {
            CalendarDto.getMyCalendarMemoRes getMemoRes = CalendarDto.getMyCalendarMemoRes.builder()
                    .memo_idx(memo.getMemoIdx())
                    .content(memo.getContent())
                    .imgUrl(memo.getUser().getImgUrl())
                    .writer(memo.getUser().getName())
                    .created_at(memo.getCreatedAt())
                    .build();

            memoResponseList.add(getMemoRes);
        }

        return memoResponseList;
    }

//    // TODO: 날짜 별 식물 불러오기
//
//    @Transactional(readOnly = true)
//    public List<PlaceDto.getMyCalendarPlaceListRes> getMyCalendarPlaceList(User user) {
//        return placeService.getPlaceNameListByUser(user);
//    }
//
//    @Transactional(readOnly = true)
//    public List<PlantDto.getMyCalendarPlantListRes> getMyCalendarPlantList(User user, String placeCode) {
//        return plantService.getPlantListByPlace(user, placeCode);
//    }
//
//    @Transactional(readOnly = true)
//    public List<MemoDto.GetAllMemo> getMyCalendarMemoList(Long plantIdx){
//        return memoService.getAllMemoByPlant(plantIdx);
//    }
}
