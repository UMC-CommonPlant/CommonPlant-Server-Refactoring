package com.umc.commonplant.domain.keyword.service;

import com.umc.commonplant.domain.keyword.entity.Keyword;
import com.umc.commonplant.domain.keyword.entity.KeywordRepository;
import com.umc.commonplant.domain.story.entity.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public void createKeywordList(Story story, List<String> keywordList) {
        List<Keyword> keywords = keywordList.stream().map(k -> new Keyword(story, k)).collect(Collectors.toList());
        keywordRepository.saveAll(keywords);
    }

    public List<String> getKeywordListByStory(Long storyIdx) {
        return keywordRepository.findAllByStoryIdx(storyIdx);
    }
}
