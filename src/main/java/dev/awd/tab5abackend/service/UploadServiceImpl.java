package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
    @Value("${tab5a.images.basepath}")
    private String basePath;

    @Override
    public String uploadImage(MultipartFile imageFile, ImageType imageType) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            Path dir = Paths.get(basePath, imageType.getFolder(), uuidPart);
            Files.createDirectories(dir);

            String extension = getExtensionFromContentType(imageFile.getContentType());
            String filename = uuidPart + extension;
            Path targetPath = dir.resolve(filename);
            imageFile.transferTo(targetPath);

            return imageType.getFolder() + "/" + uuidPart + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return "";
        }
        String mimeType = contentType.split(";")[0].trim().toLowerCase();

        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> "";
        };
    }
}
