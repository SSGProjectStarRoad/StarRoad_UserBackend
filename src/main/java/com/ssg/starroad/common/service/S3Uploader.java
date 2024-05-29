package com.ssg.starroad.common.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드
     * 파일명을 인코딩 하여 UUID로 변경하여 업로드
     *
     * @param multipartFile
     * @param dirName
     */
    public String upload(MultipartFile multipartFile, String dirName) {
        // 파일명과 확장자 분리
        String originalFileName = multipartFile.getOriginalFilename();
        int lastIndex = originalFileName.lastIndexOf('.');
        String fileName = originalFileName.substring(0, lastIndex);
        String extension = originalFileName.substring(lastIndex + 1);

        // 파일명 URL 인코딩
        String encodedFileName = null;
        encodedFileName = Base64.getUrlEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));

        // S3에 저장될 파일명 구성
        String s3FileName = dirName + "/" + UUID.randomUUID() + "_" + encodedFileName + "." + extension;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentLength(multipartFile.getInputStream().available());
            objectMetadata.setContentType(multipartFile.getContentType());
            // 권한 공개
            objectMetadata.setHeader("x-amz-acl", "public-read");

            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objectMetadata);
        } catch (IOException e) {
            log.error("S3 upload fail : " + fileName, e);
            return String.format("S3 upload fail: %s", multipartFile.getOriginalFilename());
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    /**
     * S3에 파일 여러개 업로드
     * 파일명을 인코딩 하여 UUID로 변경하여 업로드
     *
     * @param multipartFile
     * @param dirName
     */
    public List<String> upload(MultipartFile[] multipartFile, String dirName) {
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            // 파일명과 확장자 분리
            String originalFileName = file.getOriginalFilename();
            int lastIndex = originalFileName.lastIndexOf('.');
            String fileName = originalFileName.substring(0, lastIndex);
            String extension = originalFileName.substring(lastIndex + 1);

            // 파일명 URL 인코딩
            String encodedFileName = null;
            encodedFileName = Base64.getUrlEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));

            // S3에 저장될 파일명 구성
            String s3FileName = dirName + "/" + UUID.randomUUID() + "_" + encodedFileName + "." + extension;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            try {
                objectMetadata.setContentLength(file.getInputStream().available());
//                objectMetadata.setContentType(file.getContentType());
                // 파일 타입 체크후 이미지로 변경
                if (file.getContentType().contains("image")) {
                    objectMetadata.setContentType("image/jpeg");
                } else {
                    objectMetadata.setContentType(file.getContentType());
                }

                // 권한 공개
                objectMetadata.setHeader("x-amz-acl", "public-read");

                amazonS3.putObject(bucket, s3FileName, file.getInputStream(), objectMetadata);
            } catch (IOException e) {
                log.error("S3 upload fail : " + file.getOriginalFilename(), e);
                urlList.add(String.format("S3 upload fail: %s", file.getOriginalFilename()));
                continue;
            }
            urlList.add(amazonS3.getUrl(bucket, s3FileName).toString());
        }

        return urlList;
    }

    public void delete(String fileName) {
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (AmazonServiceException e) {
            log.error("S3 delete fail : fileName", e);
            throw new IllegalArgumentException("S3 delete fail");
        }
    }
}
