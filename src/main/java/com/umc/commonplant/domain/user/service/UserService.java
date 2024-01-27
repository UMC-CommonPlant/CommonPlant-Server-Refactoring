package com.umc.commonplant.domain.user.service;

import com.google.cloud.grpc.BaseGrpcServiceException;
import com.umc.commonplant.domain.Jwt.JwtService;
import com.umc.commonplant.domain.image.service.ImageService;
import com.umc.commonplant.domain.user.dto.UserDto;
import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.repository.UserRepository;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.utils.UuidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.umc.commonplant.global.exception.ErrorResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ImageService imageService;

    public User getUser(String uuid){ // User 조회
        return userRepository.findByUuid(uuid).orElseThrow(() -> new BadRequestException((NOT_FOUND_USER)));
    }
    public User saveUser(UserDto.join req){
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .build();
        return userRepository.save(user);
    }
    public String joinUser(UserDto.join req, MultipartFile image){

        if(userRepository.countUserByEmail(req.getEmail(), req.getProvider()) > 0){
            throw new BadRequestException(EXIST_USER);
        }else{
            //join
            String uuid = UuidUtil.generateType1UUID();
            String imageUrl = imageService.saveImage(image);

            User user = User.builder()
                    .name(req.getName())
                    .imgUrl(imageUrl)
                    .uuid(uuid)
                    .email(req.getEmail())
                    .provider(req.getProvider()).
                    build();
            userRepository.save(user);

            return jwtService.createToken(user.getUuid());
        }
    }
    @Transactional(readOnly = true)
    public boolean checkNameDuplication(String name){
        boolean nameDuplicate = userRepository.existsByname(name);
        if(nameDuplicate)
            throw new BadRequestException(EXIST_NAME);
        if(name.length() < 2 || name.length() > 10)
            throw new BadRequestException(NOT_VALID_LENGTH);
        return nameDuplicate;
    }

    public User getUserByName(String name){
        return userRepository.findByname(name).orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
    }

}
