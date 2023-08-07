package com.umc.commonplant.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.dto.ImagesDto;
import com.umc.commonplant.domain.image.entity.Image;
import com.umc.commonplant.domain.image.entity.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static String bucketName = "commonplantbucket";
    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;

    @Transactional
    public String saveImage(MultipartFile multipartFile, ImageDto.ImageRequest request) {
        String originalName = multipartFile.getOriginalFilename();
        String filename = getFileName(originalName);

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();

            Image image = Image.builder()
                    .imgUrl(accessUrl)
                    .category(request.getCategory())
                    .category_idx(request.getCategory_idx())
                    .build();

            imageRepository.save(image);

            return image.getImgUrl();

        } catch(IOException e) {
            return null;
        }
    }

    @Transactional
    public List<String> saveImages(ImagesDto multipartFiles, ImageDto.ImageRequest request) {
        List<String> resultList = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles.getImages()) {
            String value = saveImage(multipartFile, request);
            resultList.add(value);
        }

        return resultList;
    }

    public static String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
    }

    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    public static String getFileName(String originName) {
        return UUID.randomUUID() + "." + extractExtension(originName);
    }

}
