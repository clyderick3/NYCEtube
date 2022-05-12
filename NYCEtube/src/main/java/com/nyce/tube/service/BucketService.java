package com.nyce.tube.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.parameters.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;


import java.net.URL;
import java.time.Instant;

@Service
public class BucketService {
    // static String key = "sora1.mp4";


    public String getUrl(String key) {
        // public static void main(String[] args){
//        public String preSignedUrlGenerator(String keyName) {
            Regions clientRegion = Regions.US_EAST_1;
            String bucketName = "zcw-cohort8zero";
            String objectKey = "videoapp/" + key + ".mp4";

            try {
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

                // Set the presigned URL to expire after one hour.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = Instant.now().toEpochMilli();
                expTimeMillis += 604799999;
                expiration.setTime(expTimeMillis);

                // Generate the presigned URL.
                System.out.println("Generating pre-signed URL.");
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
                URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
                System.out.println("Pre-Signed URL: " + url.toString());
               return url.toString();
            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }

            return ("Video cannot be found");
        }

        
    // public static void UploadFileExample3(String[] args) {
    //             String bucketName = "zcw-cohort8zero";
    //             String folderName = "videoapp";
                 
    //             String fileName = "Java Logo.png";
    //             String filePath = "D:/Images/" + fileName;
    //             String key = folderName + "/" + fileName;
                 
    //             S3Client client = S3Client.builder().build();
                 
    //             PutObjectRequest request = PutObjectRequest.builder()
    //                             .bucket(bucketName)
    //                             .key(key)
    //                             .acl("public-read")
    //                             .build();
                 
    //             client.putObject(request, RequestBody.fromFile(new File(filePath)));
                 
    //             S3Waiter waiter = client.waiter();
    //             HeadObjectRequest requestWait = HeadObjectRequest.builder().bucket(bucketName).key(key).build();
                 
    //             WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
                 
    //             waiterResponse.matched().response().ifPresent(System.out::println);
                 
    //             System.out.println("File " + fileName + " was uploaded.");     
    //         }
        


}
