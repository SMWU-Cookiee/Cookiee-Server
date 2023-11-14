package com.cookiee.cookieeserver.Config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey); //자격증명 생성

        return AmazonS3ClientBuilder
                .standard() //빌더 객체 생성
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) //자격 증명 정보 설정
                .withRegion(region) //지역 설정
                .build(); //최종적으로 S3 클라이언트를 생성하고 반환

    }

}