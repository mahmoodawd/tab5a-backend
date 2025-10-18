package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String uploadImage(MultipartFile imageFile, ImageType imageType);
}
