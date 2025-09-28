package dev.awd.tab5abackend.mapper.resolver;

import dev.awd.tab5abackend.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserResolver {
    public User fromUUID(UUID uuid) {
        return null;
    }
}
