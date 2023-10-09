package com.umc.commonplant.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.umc.commonplant.domain.image.dto.ImageDto;
import com.umc.commonplant.domain.image.entity.Image;
import com.umc.commonplant.domain.image.entity.ImageRepository;
import com.umc.commonplant.global.exception.BadRequestException;
import com.umc.commonplant.global.exception.ErrorResponseStatus;
import com.umc.commonplant.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;

    public List<String> createImages(ImageDto.ImagesRequest multipartFiles, ImageDto.ImageRequest request) {
        if(multipartFiles.getImages().isEmpty()) throw new BadRequestException(ErrorResponseStatus.NO_SELECT_IMAGE);

        List<String> resultList = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles.getImages()) {
            String value = createImage(multipartFile, request);
            resultList.add(value);
        }

        return resultList;
    }

    public String createImage(MultipartFile multipartFile, ImageDto.ImageRequest request) {
        if(multipartFile.isEmpty()) throw new BadRequestException(ErrorResponseStatus.NO_SELECT_IMAGE);

        Image image;

        String imageUrl = saveImage(multipartFile);
        image = Image.builder()
                .imgUrl(imageUrl)
                .category(request.getCategory())
                .category_idx(request.getCategory_idx())
                .build();

        try{
            imageRepository.save(image);
        } catch (Exception e){
            throw new GlobalException(ErrorResponseStatus.DATABASE_ERROR);
        }

        return image.getImgUrl();
    }

    public String saveImage(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) throw new BadRequestException(ErrorResponseStatus.REQUEST_ERROR);

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

        } catch(Exception e) {
            throw new GlobalException(ErrorResponseStatus.IMAGE_UPLOAD_FAIL);
        }

        return imageUrl;
    }

    public List<String> findImageUrlByCategory(ImageDto.ImageRequest request) {
        List<String> imageUrls = imageRepository.findUrlsByCategoryAndCategoryIdx(request.getCategory(), request.getCategory_idx());

        return imageUrls;
    }

    public void deleteFileInDatabase(ImageDto.ImageRequest request) {
        List<String> imageUrls = findImageUrlByCategory(request);

        try {
            for(String imgUrl : imageUrls) {
                imageRepository.deleteByImgUrl(imgUrl);
                deleteFileInS3(imgUrl);
            }

        } catch(Exception e) {
            throw new GlobalException(ErrorResponseStatus.IMAGE_DELETE_FAIL);
        }
    }

    public void deleteFileInS3(String imageUrl) {
        String splitStr = ".com/";
        int lastIndex = imageUrl.lastIndexOf(splitStr);
        if(lastIndex == -1) throw new BadRequestException(ErrorResponseStatus.INVALID_IMAGE_URL);

        try {
            String fileName = imageUrl.substring(lastIndex + splitStr.length());
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (Exception e) {
            throw new GlobalException(ErrorResponseStatus.IMAGE_DELETE_FAIL);
        }
    }

    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
    }

    public String getFileName(String originName) {
        return UUID.randomUUID() + "." + extractExtension(originName);
    }

}
