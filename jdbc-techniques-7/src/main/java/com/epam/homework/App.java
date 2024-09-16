package com.epam.homework;

import com.epam.homework.config.AppConfig;
import com.epam.homework.model.File;
import com.epam.homework.repository.DatabaseInit;
import com.epam.homework.repository.FileRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class App {
    final static long MAX_SIZE = 198 * 1024 * 1024;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        var databaseInit = context.getBean(DatabaseInit.class);

        databaseInit.createTable();

        var filePath = "file.txt";
        var fileContent = createFile(filePath);

        var file = new File(null, filePath, "text", (long)fileContent.length, fileContent);

        var dao = context.getBean(FileRepository.class);

        var fileWithId = dao.save(file);

        System.out.printf("Generated file with name %s and id %d\n", fileWithId.name(), fileWithId.id());

        var retrieved = dao.getById(fileWithId.id());

        System.out.printf("Successfully retrieved file with id %d and name [%s]\n", retrieved.id(), retrieved.name());

    }

    public static byte[] createFile(String path) {
        byte[] buffer = "test".getBytes(StandardCharsets.UTF_8);
        byte[] content = new byte[(int) MAX_SIZE + 1000];
        try (FileOutputStream fos = new FileOutputStream(path)) {
            long written = 0;
            while(written < MAX_SIZE) {
                fos.write(buffer);
                written += buffer.length;
                System.arraycopy(buffer, 0, content, (int)written, buffer.length);
            }

            System.out.printf("Successfully wrote file %s\n", path);

            return content;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Could not write file...", ex);
        }
    }
}
