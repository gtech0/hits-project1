package com.inter.hitsproj1.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudienceDto {

    @Pattern(regexp="\\d{3}", message = "Audience number should be 3 digits long")
    private String audienceName;

}
