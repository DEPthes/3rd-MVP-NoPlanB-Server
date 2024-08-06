package com.noplanb.domain.s3.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AwsFileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public String savePhoto(MultipartFile multipartFile, String dirName, Long memberId) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile, dirName, memberId);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName, Long memberId) {
        String fileName = dirName + memberId  + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        System.out.println("fileName = " + fileName);
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        } else {
        log.error("Failed to create new file: " + convertFile.getAbsolutePath());
    }
        return Optional.empty();
    }

    public void createDir(String bucketName, String folderName) {
        amazonS3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }
    public void deleteImage(String dirName, String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);

        if (fileName != null) {
            try {
                fileName = URLDecoder.decode(fileName, "UTF-8");
            } catch (Exception e) {
                log.error("파일명 디코딩 중 오류 발생", e);
                return;
            }

            // 디렉토리명과 파일명을 조합하여 전체 객체 키 생성
            String objectKey = dirName + "/" + fileName;

            // S3 파일 확인
            if (isS3FileExists(objectKey)) {
                amazonS3Client.deleteObject(bucket, objectKey);
                log.info("S3 이미지 삭제가 완료되었습니다. 파일명: {}", objectKey);

            } else {
                log.warn("S3에 해당 파일이 존재하지 않습니다. 파일명: {}", objectKey);
            }

        } else {
            log.warn("유효하지 않은 S3 이미지 URL입니다. URL: {}", imageUrl);
        }

    }

    // S3에 해당 파일이 존재하는지 확인
    private boolean isS3FileExists(String fileName) {
        try {
            ObjectMetadata objectMetadata = amazonS3Client.getObjectMetadata(bucket, fileName);
            return true;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                return false; // 파일이 존재하지 않음
            } else {
                throw e; // 다른 예외는 다시 던짐
            }
        }
    }

    private String extractFileNameFromUrl(String imageUrl) {
        try {
            return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            return null;
        }
    }
}
