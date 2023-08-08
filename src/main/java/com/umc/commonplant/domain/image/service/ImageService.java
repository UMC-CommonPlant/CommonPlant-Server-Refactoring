package com.umc.commonplant.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.entity.Image;
import com.umc.commonplant.domain.image.entity.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static String bucketName = "commonplantbucket";
    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;

    //여러 파일 - S3와 DB Table에 저장
    @Transactional
    public List<String> createImages(ImageDto.ImagesRequest multipartFiles, ImageDto.ImageRequest request) {
        List<String> resultList = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles.getImages()) {
            String value = createImage(multipartFile, request);
            resultList.add(value);
        }

        return resultList;
    }

    //단일 파일 - S3와 DB Table에 저장
    @Transactional
    public String createImage(MultipartFile multipartFile, ImageDto.ImageRequest request) {
        Image image = new Image();

        String imageUrl = saveImage(multipartFile);
        image = Image.builder()
                .imgUrl(imageUrl)
                .category(request.getCategory())
                .category_idx(request.getCategory_idx())
                .build();

        imageRepository.save(image);

        return image.getImgUrl();
    }

    //단일 파일 - S3에만 저장, imageURL 반환
    @Transactional
    public String saveImage(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        String filename = getFileName(originalName);
        String imageUrl = null;

        try {
            InputStream inputStream = multipartFile.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(inputStream.available());

            amazonS3Client.putObject(bucketName, filename, inputStream, objectMetadata);

            imageUrl = amazonS3Client.getUrl(bucketName, filename).toString();

            inputStream.close();

        } catch(IOException e) {

        }

        return imageUrl;
    }

    @Transactional
    public List<String> findImageUrlByCategory(ImageDto.ImageRequest request) {
        List<String> imageUrls = imageRepository.findUrlsByCategoryAndCategoryIdx(request.getCategory(), request.getCategory_idx());

        return imageUrls;
    }

    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
    }

    public String getFileName(String originName) {
        return UUID.randomUUID() + "." + extractExtension(originName);
    }

}
