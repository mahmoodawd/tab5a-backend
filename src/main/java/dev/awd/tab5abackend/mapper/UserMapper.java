package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.response.UserResponseDto;
import dev.awd.tab5abackend.mapper.resolver.ImageMapping;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.mapper.resolver.MultipartFileResolver;
import dev.awd.tab5abackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class, MultipartFileResolver.class})
public interface UserMapper {

    @Mapping(target = "fullName", source = "name")
    @Mapping(target = "avatarUrl", source = "avatar", qualifiedBy = ImageMapping.class)
    UserResponseDto userToUserResponseDto(User user);
}
