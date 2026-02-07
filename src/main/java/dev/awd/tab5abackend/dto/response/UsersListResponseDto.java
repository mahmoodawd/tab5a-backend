package dev.awd.tab5abackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersListResponseDto {
    private int count;
    private Date lastUpdate;
    private List<UserResponseDto> users;
}
