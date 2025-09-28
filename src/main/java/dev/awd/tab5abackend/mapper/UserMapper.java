package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.UserRequestDto;
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

    @Mapping(target = "avatar", source = "avatar", qualifiedBy = ImageMapping.class)
    UserResponseDto userToUserResponseDto(User user);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User userRequestDtoToUser(UserRequestDto userRequestDto);
}
