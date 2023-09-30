package com.umc.commonplant.domain.likes.service;

import com.umc.commonplant.domain.likes.entity.Likes;
import com.umc.commonplant.domain.likes.entity.LikeRepository;
import com.umc.commonplant.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void likeStory(User user, Long storyIdx){
        Likes like;
        like = likeRepository.findHeartByStory(user.getUserIdx(), storyIdx);
        if(like==null){
            like = Likes.builder().user(user).category("STORY").categoryIdx(storyIdx).build();
            likeRepository.save(like);
        }else{
            likeRepository.delete(like);
        }
    }

    public boolean isUserLikeStory(Long userIdx, Long storyIdx){
        if(likeRepository.findHeartByStory(userIdx, storyIdx)!=null)
            return true;
        return false;
    }

    public int countLikeByStory(Long storyIdx){
        return likeRepository.countLikeByStory(storyIdx);
    }


}
