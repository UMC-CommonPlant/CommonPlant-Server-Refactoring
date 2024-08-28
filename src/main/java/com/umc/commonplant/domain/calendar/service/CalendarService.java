package com.umc.commonplant.domain.calendar.service;

import com.umc.commonplant.domain.belong.entity.BelongRepository;
import com.umc.commonplant.domain.calendar.dto.CalendarDto;
import com.umc.commonplant.domain.memo.dto.MemoDto;
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
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    private final AuditReader auditReader;

    private List<LocalDateTime> getWateredDateByPlantIdx(Long plantIdx) {
        List wateredDateHistory = auditReader.createQuery()
                .forRevisionsOfEntity(Plant.class, false, true)
                .add(AuditEntity.id().eq(plantIdx))
                //.add(AuditEntity.property("wateredDate").hasChanged())
                .addProjection(AuditEntity.property("wateredDate"))
                .getResultList();

        return wateredDateHistory;
    }

    private List<LocalDateTime> getLastWateredDateByPlantIdx(Long plantIdx) {
        List lastWateredDate = auditReader.createQuery()
                .forRevisionsOfEntity(Plant.class, false, true)
                .add(AuditEntity.id().eq(plantIdx))
                .addProjection(AuditEntity.property("wateredDate"))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setMaxResults(1)
                .getResultList();

        return lastWateredDate;
    }

    @Transactional(readOnly = true)
    public CalendarDto.getMyCalendarRes getMyCalendar(User user, String year, String month){
        int parsedYear = Integer.parseInt(year);
        int parsedMonth = Integer.parseInt(month);

        if(parsedMonth < 1 || parsedMonth > 12) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_MONTH_VALUE);
        }

        YearMonth yearMonth = YearMonth.of(parsedYear, parsedMonth);

        int lengthOfMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDateTime firstDate = startDate.atStartOfDay();
        LocalDateTime lastDate = endDate.atTime(LocalTime.MAX);

        // TODO: 유저가 속한 장소 리스트
        List<Place> placeListByUser = placeService.getPlaceListByUser(user);
        List<PlantDto.getMyCalendarPlantListRes> myCalendarPlantList = new ArrayList<>();

        for(Place place : placeListByUser) {
            String placeCode = place.getCode();
            myCalendarPlantList.addAll(plantService.getPlantListByPlace(user, placeCode));
        }
        
        // TODO: 날짜 정보 별로 각 Repo에 boolean으로 접근해서 정보 설정
        // TODO: 식물을 처음 데려온 날
        // List<LocalDateTime> createdAtOfPlantList = plantRepository.getDateListOfPlant(firstDate, lastDate);
        List<Boolean> joinPlantList = new ArrayList<>(Collections.nCopies(lengthOfMonth + 1, false));
        List<LocalDateTime> createdAtOfPlantList = new ArrayList<>();
        List<MemoDto.GetAllMemo> memoList = new ArrayList<>();

        for(PlantDto.getMyCalendarPlantListRes plantOfPlace : myCalendarPlantList) {
            Long plantIdx = plantOfPlace.getPlantIdx();

            Plant plant = plantRepository.findByPlantIdx(plantIdx)
                    .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.NOT_FOUND_PLANT));;

            LocalDateTime createdAt = plant.getCreatedAt();
            createdAtOfPlantList.add(createdAt);

            List<MemoDto.GetAllMemo> memoOfPlantList = memoService.getAllMemoByPlant(plantIdx);
            memoList.addAll(memoOfPlantList);
        }

        for (LocalDateTime createdAt : createdAtOfPlantList) {
            YearMonth joinPlantYearMonth = YearMonth.from(createdAt);
            int date = createdAt.toLocalDate().getDayOfMonth();

            if (joinPlantYearMonth.equals(yearMonth)) {
                joinPlantList.set(date, true);
            }
        }

        // TODO: 물을 준 날
        List<LocalDateTime> wateredDateOfPlantList = new ArrayList<>();

        List<LocalDateTime> lastWateredDateofPlantList = new ArrayList<>();
        List<Integer> waterCycleOfPlantList = new ArrayList<>();

        for(PlantDto.getMyCalendarPlantListRes plantOfPlace: myCalendarPlantList) {
            Long plantIdx = plantOfPlace.getPlantIdx();

            Plant plant = plantRepository.findByPlantIdx(plantIdx)
                    .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.NOT_FOUND_PLANT));;

            List<LocalDateTime> wateredDateRevisionList = getWateredDateByPlantIdx(plantIdx);
            wateredDateOfPlantList.addAll(wateredDateRevisionList);

            List<LocalDateTime> lastWateredDateRevisionList = getLastWateredDateByPlantIdx(plantIdx);
            lastWateredDateofPlantList.addAll(lastWateredDateRevisionList);

            waterCycleOfPlantList.add(plant.getWaterCycle());
        }

        List<Boolean> prevWateredList = new ArrayList<>(Collections.nCopies(lengthOfMonth + 1, false));
        List<Boolean> nextWateredList = new ArrayList<>(Collections.nCopies(lengthOfMonth + 1, false));
        List<LocalDateTime> nextWateredDateList = new ArrayList<>();

        for(LocalDateTime prevWatered : wateredDateOfPlantList) {
            YearMonth prevWateredYearMonth = YearMonth.from(prevWatered);
            int date = prevWatered.toLocalDate().getDayOfMonth();

            if(prevWateredYearMonth.equals(yearMonth)) {
                prevWateredList.set(date, true);
            }
        }

        for(int i = 0; i < lastWateredDateofPlantList.size(); i++) {
            LocalDateTime nextWatered = lastWateredDateofPlantList.get(i);
            Calendar calendar = Calendar.getInstance();
            // calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
            Date lastWateredDate = Timestamp.valueOf(nextWatered);
            calendar.setTime(lastWateredDate);

            Calendar limit = Calendar.getInstance();
            limit.add(Calendar.MONTH, 2);
            limit.set(Calendar.DAY_OF_MONTH, limit.getActualMaximum(Calendar.DAY_OF_MONTH));
            // limit.add(Calendar.MONTH, 2);

            int waterCycle = waterCycleOfPlantList.get(i);

            while(calendar.before(limit)) {
                calendar.add(Calendar.DAY_OF_MONTH, waterCycle);

                if(calendar.before(limit)) {
                    Date tempDate = calendar.getTime();
                    LocalDateTime tempLDTime = new Timestamp(tempDate.getTime()).toLocalDateTime();
                    nextWateredDateList.add(tempLDTime);
                }
            }

        }

        for(LocalDateTime nextWatered : nextWateredDateList) {
            YearMonth nextWateredYearMonth = YearMonth.from(nextWatered);
            int date = nextWatered.toLocalDate().getDayOfMonth();

            if(nextWateredYearMonth.equals(yearMonth)) {
                nextWateredList.set(date, true);
            }
        }

        // TODO: 메모 있는 날
        List<Boolean> writeMemoList = new ArrayList<>(Collections.nCopies(lengthOfMonth + 1, false));
        List<LocalDateTime> createdDateOfMemoList = new ArrayList<>();

        for(MemoDto.GetAllMemo memo : memoList) {
            LocalDateTime writeMemo = memo.getCreated_at();

            createdDateOfMemoList.add(writeMemo);
        }

        for(LocalDateTime writeMemo : createdDateOfMemoList) {
            YearMonth writeMemoYearMonth = YearMonth.from(writeMemo);
            int date = writeMemo.toLocalDate().getDayOfMonth();

            if(writeMemoYearMonth.equals(yearMonth)) {
                writeMemoList.set(date, true);
            }
        }

        // TODO: 월 정보 반환
        List<CalendarDto.getMyCalendarEventRes> getMyCalendarEventList = new ArrayList<>();

        for(int parsedDate = 1; parsedDate <= lengthOfMonth; parsedDate++){
            CalendarDto.getMyCalendarEventRes getMyCalendarEventRes = CalendarDto.getMyCalendarEventRes.builder()
                    .parsedDate(parsedDate)
                    .joinPlant(joinPlantList.get(parsedDate))
                    .prevWatered(prevWateredList.get(parsedDate))
                    .nextWatered(nextWateredList.get(parsedDate))
                    .writeMemo(writeMemoList.get(parsedDate))
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
