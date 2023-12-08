package com.java.java_proj.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.IImageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class FirebaseFileService implements IImageService {

    String BUCKET_NAME = "spring-60dd1.appspot.com";
    String IMAGE_URL = "https://storage.googleapis.com/spring-60dd1.appspot.com/%s";

    public void init() {

        // initialize Firebase
        try {

            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(BUCKET_NAME)
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception ex) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot connect to Firebase Storage.");
        }
    }

    @Override
    public String getImageUrl(String name) {
        return String.format(IMAGE_URL, name);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        validateUploadedFile(file);

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(file.getOriginalFilename());

        bucket.create(name, file.getBytes(), file.getContentType());

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public void delete(String name) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket();

        if (name.isEmpty()) {
            throw new IOException("Invalid file name.");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("File not found");
        }

        blob.delete();
    }

    private void validateUploadedFile(MultipartFile file) {
        if (!(validateExtension(file) && validateFileSize(file)))
            throw new HttpException(HttpStatus.BAD_REQUEST, "File is invalid. Please check and upload again.");
    }

    private Boolean validateExtension(MultipartFile file) {
        //  allowed upload file/extension: image, pdf, ppt, video, xls
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        List<String> validExtension = Arrays.asList("jpeg", "png", "jpg", "pdf", "ppt", "mp4", "mkv", "xls");

        return validExtension.contains(extension);
    }

    private Boolean validateFileSize(MultipartFile file) {
        // <= 25mb
        return (file.getSize() <= 25000000);
    }


}