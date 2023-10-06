package com.inter.hitsproj1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse {
    private final Date timestamp;
    private final int status;
    private final String message;
    private List<ValidationError> errors;

    private record ValidationError(Date timestamp, String field, String message) { }

    public void addValidationError(Date timestamp, String field, String message){
        if(Objects.isNull(errors)){
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(timestamp, field, message));
    }
}
