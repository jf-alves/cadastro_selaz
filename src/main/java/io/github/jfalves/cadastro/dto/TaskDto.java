package io.github.jfalves.cadastro.dto;

import io.github.jfalves.cadastro.enums.EnumStatus;
import io.github.jfalves.cadastro.model.UserModel;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String title;
    private String description;
    @NotBlank
    private EnumStatus status;
    private UserModel user;
}
