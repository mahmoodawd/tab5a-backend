package dev.awd.tab5abackend.mapper.resolver;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileResolver {
    public String toPath(MultipartFile file) {
        return "";
    }
}
