package com.ssg.starroad.common.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        // 파일을 byte로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Thumbnailator를 사용하여 이미지 리사이징
            Thumbnails.of(multipartFile.getInputStream())
                    .size(800, 800)
                    .outputFormat(extension)
                    .toOutputStream(outputStream);

            byte[] resizedImageBytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(resizedImageBytes);


            objectMetadata.setContentLength(resizedImageBytes.length);
            objectMetadata.setContentType(multipartFile.getContentType());
            // 권한 공개
            objectMetadata.setHeader("x-amz-acl", "public-read");

            amazonS3.putObject(bucket, s3FileName, inputStream, objectMetadata);
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
            // Separate file name and extension
            String originalFileName = file.getOriginalFilename();
            int lastIndex = originalFileName.lastIndexOf('.');
            String fileName = originalFileName.substring(0, lastIndex);
            String extension = originalFileName.substring(lastIndex + 1);

            // URL encode the file name
            String encodedFileName = Base64.getUrlEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));

            // Construct the file name to be stored in S3
            String s3FileName = dirName + "/" + UUID.randomUUID() + "_" + encodedFileName + "." + extension;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                if (file.getContentType().contains("image")) {
                    // Resize the image using Thumbnailator
                    Thumbnails.of(file.getInputStream())
                            .size(800, 800)
                            .outputFormat(extension)
                            .toOutputStream(outputStream);

                    byte[] resizedImageBytes = outputStream.toByteArray();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(resizedImageBytes);

                    objectMetadata.setContentLength(resizedImageBytes.length);
                    objectMetadata.setContentType("image/jpeg");
                    // Set public-read permission
                    objectMetadata.setHeader("x-amz-acl", "public-read");

                    amazonS3.putObject(bucket, s3FileName, inputStream, objectMetadata);
                } else {
                    // Convert non-image file to a basic image (JPEG) and resize
                    BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.setColor(Color.WHITE); // Set background to white
                    g2d.fillRect(0, 0, 800, 800);
                    g2d.setColor(Color.BLACK); // Set text color to black
                    g2d.drawString("Converted to Image", 50, 400);
                    g2d.dispose();

                    ImageIO.write(bufferedImage, "jpg", outputStream);
                    byte[] imageBytes = outputStream.toByteArray();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

                    objectMetadata.setContentLength(imageBytes.length);
                    objectMetadata.setContentType("image/jpeg");
                    objectMetadata.setHeader("x-amz-acl", "public-read");

                    amazonS3.putObject(bucket, s3FileName, inputStream, objectMetadata);
                }
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
