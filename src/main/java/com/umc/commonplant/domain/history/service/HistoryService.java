package com.umc.commonplant.domain.history.service;

import com.umc.commonplant.domain.history.entity.History;
import com.umc.commonplant.domain.history.entity.HistoryRepository;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public void searchInfo(String name) {

        History history = null;

        Optional<History> OptionalHistory = historyRepository.findByName(name)
                .stream()
                .findFirst()
                .or(() -> historyRepository.findByScientificName(name).stream().findFirst());

        if (OptionalHistory.isPresent()) {
            history = OptionalHistory.get();
            //기존거에 ++
        } else {
            //일단 조회 (infoRepository 사용? ㄴㄴ.. 아니면 join?)
            //없으니까 새로 추가
        }

    }

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 1 * *")
    public void autoDelete() {
        historyRepository.deleteAll();
    }
}
