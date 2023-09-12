package com.teamip.heyhello.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    @Value("${secret.s3.host.url}")
    public String s3HostUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile == null){
            throw new IllegalArgumentException("사진 없이는 등록이 불가능합니다.");
        }
        UUID uuid = UUID.randomUUID();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        amazonS3.putObject(bucket, uuid.toString(), multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, uuid.toString()).toString();
    }

    public void deleteImage(String imageUrl)  {
        String subsUrl = imageUrl.substring(s3HostUrl.length());
        amazonS3.deleteObject(bucket, subsUrl);
    }
    public String getDefaultImageUrl(String imageUrl)  {
        return s3HostUrl + imageUrl + ".png";
    }
}
