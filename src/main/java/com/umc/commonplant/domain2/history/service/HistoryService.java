package com.umc.commonplant.domain2.history.service;

import com.umc.commonplant.domain2.history.entity.History;
import com.umc.commonplant.domain2.history.entity.HistoryRepository;
import com.umc.commonplant.domain2.info.entity.Info;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    public void searchInfo(Info info) {
        try {
            Optional<History> OptionalHistory = historyRepository.findByInfo(info).stream().findFirst();

            History history;
            if (OptionalHistory.isPresent()) {
                history = OptionalHistory.get();
                history.setCount(history.getCount()+1);
            } else {
                history = new History();
                history.setInfo(info);
                history.setCount(1);
            }
            historyRepository.save(history);
        } catch (OptimisticLockException e) {

        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }

    }

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 1 * *")
    public void autoDelete() {
        historyRepository.deleteAll();
    }
}
