package com.umc.commonplant.domain.friend.service;

import com.umc.commonplant.domain.belong.entity.BelongRepository;
import com.umc.commonplant.domain.friend.dto.FriendDto;
import com.umc.commonplant.domain.friend.entity.Friend;
import com.umc.commonplant.domain.friend.entity.FriendRepository;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.service.UserService;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.*;



@RequiredArgsConstructor
@Service
public class FriendService {
    private final UserService userService;
    private final FriendRepository friendRepository;
    private final BelongRepository belongRepository;

    public List<String> sendFriendRequest(String senderName, List<String>receiverList, String placeCode){
        User sender = userService.getUserByName(senderName);

        // Exception
        // 친구 요청이 수락되었는지 확인
        // TODO: 에러코드로 요청을 이미 보낸 경우 예외처리하기
        /*if(!"PENDING".equals(FriendDto.sendFriendReq.status())){
        }*/
        for(String name : receiverList){
            User receiver = userService.getUserByName(name); // 서비스 사용자인지 확인
            if(belongRepository.countUserOnPlace(receiver.getUuid(), placeCode) > 0) // 장소에 이미 초대 됐는지 확인
                throw new BadRequestException(IS_USER_ON_PLACE);
        }

        // 요청 보내기(여러명)
        for(String name : receiverList){
            Friend friend = Friend.builder()
                    .sender(sender.getName())
                    .receiver(name)
                    .placeCode(placeCode)
                    .status("PENDING")
                    .build();
            friendRepository.save(friend);
        }
        // TODO : 요청 보낸 리스트 정렬
        return receiverList;
    }
}
