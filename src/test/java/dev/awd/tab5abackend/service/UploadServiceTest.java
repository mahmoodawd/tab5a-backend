package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    private UploadServiceImpl uploadService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        uploadService = new UploadServiceImpl();
        ReflectionTestUtils.setField(uploadService, "basePath", tempDir.toString());
    }

    @Test
    void uploadImage_ShouldReturnDbStoragePath_WhenFileIsValid() {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.png", "image/png", "fake image".getBytes()
        );

        String dbPath = uploadService.uploadImage(image, ImageType.CATEGORY);

        assertNotNull(dbPath);
        assertTrue(dbPath.contains(ImageType.CATEGORY.getFolder()));
        assertTrue(dbPath.endsWith(".png"));

        // Verify that the file was actually created
        Path storedFile = tempDir.resolve(dbPath);
        assertTrue(Files.exists(storedFile));
    }
    @Test
    void uploadImage_ShouldReturnChefDbStoragePath_WhenFileTypeIsChef() {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.png", "image/png", "fake image".getBytes()
        );

        String dbPath = uploadService.uploadImage(image, ImageType.CHEF);

        assertNotNull(dbPath);
        assertTrue(dbPath.contains(ImageType.CHEF.getFolder()));
        assertTrue(dbPath.endsWith(".png"));

        // Verify that the file was actually created
        Path storedFile = tempDir.resolve(dbPath);
        assertTrue(Files.exists(storedFile));
    }

    @Test
    void uploadImage_ShouldReturnNull_WhenFileIsNull() {
        String dbPath = uploadService.uploadImage(null, ImageType.CATEGORY);
        assertNull(dbPath);
    }

    @Test
    void uploadImage_ShouldReturnNull_WhenFileIsEmpty() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "image", "empty.png", "image/png", new byte[0]
        );

        String dbPath = uploadService.uploadImage(emptyFile, ImageType.CATEGORY);
        assertNull(dbPath);
    }

    @Test
    void uploadImage_ShouldGenerateUniqueDirectoryPerUpload() {
        MockMultipartFile image1 = new MockMultipartFile("image", "a.png", "image/png", "1".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image", "b.png", "image/png", "2".getBytes());

        String path1 = uploadService.uploadImage(image1, ImageType.CATEGORY);
        String path2 = uploadService.uploadImage(image2, ImageType.CATEGORY);

        // Each should have a unique folder segment (UUID substring)
        assertNotEquals(path1, path2);
    }
}
