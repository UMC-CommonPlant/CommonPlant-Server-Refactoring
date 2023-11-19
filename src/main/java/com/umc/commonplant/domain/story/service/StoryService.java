package com.umc.commonplant.domain.story.service;

import com.umc.commonplant.domain.comment.dto.CommentDto;
import com.umc.commonplant.domain.comment.service.CommentService;
import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain.keyword.service.KeywordService;
import com.umc.commonplant.domain.likes.service.LikeService;
import com.umc.commonplant.domain.story.dto.StoryDto;
import com.umc.commonplant.domain.story.entity.Story;
import com.umc.commonplant.domain.story.entity.StoryRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.NOT_FOUND_STORY;
import static com.umc.commonplant.global.exception.ErrorResponseStatus.NO_SELECTED_STORY_IMAGE;

@RequiredArgsConstructor
@Service
public class StoryService {
    private final StoryRepository storyRepository;
    private final KeywordService keywordService;
    private final CommentService commentService;
    private final LikeService likeService;

    private final ImageService imageService;

    @Transactional
    public void createStory(User user, StoryDto.createStoryReq req, List<MultipartFile> imageList) {
        Story story = Story.builder().user(user).content(req.getContent()).is_public("PUBLIC").build();
        story = storyRepository.save(story);
        keywordService.createKeywordList(story, req.getKeyword());

        if(imageList.get(0).getSize() < 1)
            throw new BadRequestException(NO_SELECTED_STORY_IMAGE);

        ImageDto.ImageRequest imageDto = new ImageDto.ImageRequest("STORY", story.getStoryIdx());
        for(MultipartFile image : imageList)
        {
            imageService.createImage(image, imageDto);
        }
    }

    @Transactional
    public StoryDto.getStoryRes getStory(User user, Long storyIdx) {
        Story story = storyRepository.findById(storyIdx).orElseThrow(()-> new BadRequestException(NOT_FOUND_STORY));
        List<String> keywords = keywordService.getKeywordListByStory(storyIdx);
        List<String> images = imageService.findImageUrlByCategory(new ImageDto.ImageRequest("STORY", storyIdx));

        List<CommentDto.storyComment> comments = commentService.getCommentByStory(storyIdx);

        int countLike = likeService.countLikeByStory(storyIdx);
        boolean isLike = likeService.isUserLikeStory(user.getUserIdx(), storyIdx);

        StoryDto.getStoryRes res = StoryDto.getStoryRes.builder()
                .storyIdx(story.getStoryIdx()).content(story.getContent()).owner(story.getUser())
                .like(countLike).isLike(isLike)
                .comments(comments)
                .images(images).keywords(keywords).build();
        if(user.getUuid().equals(story.getUser().getUuid()))
            res.setIsOwner(true);

        return res;
    }

// ----- API 외 메서드 -----

    public Story findStoryByIdx(Long storyIdx){
        return storyRepository.findById(storyIdx).orElseThrow(() -> new BadRequestException(NOT_FOUND_STORY));
    }
}
