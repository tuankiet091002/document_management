package com.java.java_proj.services.templates;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public interface IImageService {

    String getImageUrl(String name);

    String save(MultipartFile file) throws IOException;

    String save(BufferedImage bufferedImage, String originalFileName) throws IOException;

    void delete(String name) throws IOException;

    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    default String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + getExtension(originalFileName);
    }

    default byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {

            ImageIO.write(bufferedImage, format, stream);

            stream.flush();

            return stream.toByteArray();

        }
    }

}
