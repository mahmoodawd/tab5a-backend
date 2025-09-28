package dev.awd.tab5abackend.mapper.resolver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageUrlResolver {

    @Value("${tab5a.images.baseurl}")
    private String baseUrl;
    @Value("${tab5a.images.basepath}")
    private String basePath;

    @ImageMapping
    public String toUrl(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }
        return baseUrl.endsWith("/") ? baseUrl + imagePath : baseUrl + "/" + imagePath;
    }

    public Path toPath(String filename) {
        return Paths.get(basePath, filename);
    }
}
