package com.hart.overwatch.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


@Service
public class CsvFileService {

    private static final String FILE_DELETION_QUEUE = "file_deletion_queue";

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public CsvFileService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void writeCsv(List<?> data, FileWriter writer) throws Exception {
        if (data == null || data.isEmpty()) {
            return;
        }

        Class<?> clazz = data.get(0).getClass();

        Field[] fields = clazz.getDeclaredFields();
        writer.write(String.join(",", getFieldNames(fields)));
        writer.write("\n");

        for (Object row : data) {
            writer.write(String.join(",", getFieldValues(row, fields)));
            writer.write("\n");
        }
    }

    private String[] getFieldNames(Field[] fields) {
        return Arrays.stream(fields).map(Field::getName).toArray(String[]::new);
    }

    private String[] getFieldValues(Object row, Field[] fields) throws IllegalAccessException {
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(row);
            values[i] = value != null ? value.toString() : "";
        }
        return values;
    }

    public Path generateCsvFile(String fileName, List<FullStripePaymentIntentDto> data)
            throws IOException {

        Path filePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writeCsv(data, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        queueFileForDeletion(filePath.toString());
        return filePath;
    }

    private void queueFileForDeletion(String filePath) {
        redisTemplate.opsForList().leftPush(FILE_DELETION_QUEUE, filePath);
    }

    public void processFileDeletions() {
        String filePath = redisTemplate.opsForList().rightPop(FILE_DELETION_QUEUE);

        if (filePath != null) {
            try {
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    Files.delete(path);
                    System.out.println("Deleted file: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + filePath);
                e.printStackTrace();
            }
        }
    }
}
