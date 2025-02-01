package com.hart.overwatch.amazon;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AmazonServiceTest {

    @InjectMocks
    private AmazonService amazonService;

    @Mock
    S3Utilities s3Utilities;

    @Mock
    S3Client s3Client;

    @Mock
    MultipartFile multipartFile;

    @Mock
    URL mockUrl;

    private File tempFile;



    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void AmazonService_PutS3Object_ReturnHashMap() throws IOException {
        String bucketName = "test-bucket";
        String objectKey = "testFile.txt";
        String expectedUrl = "https://s3.amazonaws.com/test-bucket/testFile.txt";

        MultipartFile multipartFile = mock(MultipartFile.class);
        byte[] fileContent = "test file content".getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn("testFile.txt");
        when(multipartFile.getSize()).thenReturn((long) fileContent.length);
        when(multipartFile.getBytes()).thenReturn(fileContent);

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        when(s3Client.utilities()).thenReturn(s3Utilities);

        when(s3Utilities.getUrl(any(GetUrlRequest.class)))
                .thenReturn(URI.create(expectedUrl).toURL());

        HashMap<String, String> result =
                amazonService.putS3Object(bucketName, objectKey, multipartFile);

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(s3Client.utilities()).getUrl(any(GetUrlRequest.class));

        Assertions.assertThat(result).containsKey("objectUrl");
        Assertions.assertThat(result).containsKey("filename");
        Assertions.assertThat(result.get("objectUrl")).isEqualTo(expectedUrl);
    }

}


