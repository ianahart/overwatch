package com.hart.overwatch.amazon;


import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import com.hart.overwatch.advice.BadRequestException;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class AmazonService {

    private S3Client s3Client;

    @Value("${amazon.s3.default-bucket}")
    private String defaultRegion;

    @Value("${amazon.aws.access-key-id}")
    private String accessKeyId;

    @Value("${amazon.aws.access-key-secret}")
    private String secretKey;

    @PostConstruct
    private void initialize() {
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKeyId, secretKey));
        this.s3Client = S3Client.builder().region(Region.US_EAST_1)
                .credentialsProvider(credentialsProvider).build();
    }

    private File convertMultipartFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convFile);

        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convFile;
    }

    private String createFilename(String folder, String filename) {
        UUID uuid = UUID.randomUUID();
        return folder + uuid + filename;
    }

    private URL getURL(String bucketName, String filename) {
        try {
            GetUrlRequest request =
                    GetUrlRequest.builder().bucket(bucketName).key(filename).build();

            return this.s3Client.utilities().getUrl(request);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
            throw new BadRequestException("Trouble getting object URL");
        }
    }

    private HashMap<String, String> wrapContents(String objectUrl, String filename) {
        HashMap<String, String> contents = new HashMap<>();

        contents.put("objectUrl", objectUrl);
        contents.put("filename", filename);

        return contents;
    }

    public HashMap<String, String> putS3Object(String bucketName, String objectKey,
            MultipartFile file) {
        try {

            if (file.getSize() > 2 * 1024 * 1024) {
                throw new BadRequestException("File size cannot exceed 2MB");
            }

            String filename = createFilename("overwatch/", objectKey);
            PutObjectRequest putOb = PutObjectRequest.builder().bucket(bucketName)
                    .acl("public-read").key(filename).build();

            File myFile = convertMultipartFile(file);
            this.s3Client.putObject(putOb, RequestBody.fromFile(myFile));
            System.out.println("Successfully placed " + filename + " into bucket " + bucketName);
            URL objectUrl = getURL(bucketName, filename);

            myFile.delete();

            return wrapContents(objectUrl.toString(), filename);

        } catch (S3Exception | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            throw new BadRequestException("Trouble uploading file to s3");
        }
    }

    public void deleteBucketObject(String bucketName, String objectName) {

        try {

            DeleteObjectRequest deleteObjectRequest =
                    DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build();
            this.s3Client.deleteObject(deleteObjectRequest);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        System.out.println("Done!");

    }

}
