package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${tab5a.images.basepath}")
    private String basePath;

    @Override
    public String uploadImage(MultipartFile imageFile, ImageType imageType) {
        log.info("Image upload initiated. imageType={}", imageType);

        if (imageFile == null || imageFile.isEmpty()) {
            log.warn("Upload skipped - null or empty file. imageType={}", imageType);
            return null;
        }

        log.debug("Image file details. imageType={}, originalFilename={}, size={} bytes, contentType={}",
                imageType, imageFile.getOriginalFilename(), imageFile.getSize(), imageFile.getContentType());

        try {
            log.debug("Generating unique identifier for image. imageType={}", imageType);
            String uuidPart = UUID.randomUUID()
                    .toString().replace("-", "").substring(0, 16);
            log.debug("Generated UUID: {}. imageType={}", uuidPart, imageType);

            log.debug("Constructing storage directory. imageType={}, uuid={}", imageType, uuidPart);
            Path dir = Paths.get(basePath, imageType.getFolder(), uuidPart);
            log.debug("Storage directory path: {}. imageType={}", dir, imageType);

            log.debug("Creating directory structure. path={}", dir);
            Files.createDirectories(dir);
            log.debug("Directory created successfully. path={}", dir);

            log.debug("Determining file extension. contentType={}", imageFile.getContentType());
            String extension = getExtensionFromContentType(imageFile.getContentType());
            log.debug("File extension determined: {}. contentType={}", extension, imageFile.getContentType());

            String filename = uuidPart + extension;
            log.debug("Generated filename: {}. imageType={}", filename, imageType);

            Path targetPath = dir.resolve(filename);
            log.debug("Transferring file to storage. targetPath={}, size={} bytes",
                    targetPath, imageFile.getSize());

            imageFile.transferTo(targetPath);

            String storagePath = imageType.getFolder() + "/" + uuidPart + "/" + filename;
            log.info("Image uploaded successfully. imageType={}, storagePath={}, size={} bytes, filename={}",
                    imageType, storagePath, imageFile.getSize(), filename);

            return storagePath;

        } catch (IOException e) {
            log.error("Failed to upload image. imageType={}, originalFilename={}, error={}",
                    imageType, imageFile.getOriginalFilename(), e.getMessage(), e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private String getExtensionFromContentType(String contentType) {
        log.debug("Extracting extension from content type: {}", contentType);

        if (contentType == null) {
            log.warn("Content type is null, returning empty extension");
            return "";
        }

        String mimeType = contentType.split(";")[0].trim().toLowerCase();
        log.debug("Parsed MIME type: {}", mimeType);

        String extension = switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> {
                log.warn("Unsupported MIME type, returning empty extension. mimeType={}", mimeType);
                yield "";
            }
        };

        log.debug("Extension resolved: {}. mimeType={}", extension, mimeType);
        return extension;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing UploadService. basePath={}", basePath);

        try {
            Path baseDir = Paths.get(basePath);
            log.debug("Creating base directory if not exists. path={}", baseDir);

            if (!Files.exists(baseDir)) {
                Files.createDirectories(baseDir);
                log.info("Base directory created. path={}", baseDir);
            } else {
                log.debug("Base directory already exists. path={}", baseDir);
            }

            log.info("UploadService initialized successfully. basePath={}", basePath);
        } catch (IOException e) {
            log.error("Failed to initialize UploadService. basePath={}, error={}",
                    basePath, e.getMessage(), e);
            throw new RuntimeException("Could not create base upload directory", e);
        }
    }
}