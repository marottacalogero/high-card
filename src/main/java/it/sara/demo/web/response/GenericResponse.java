package it.sara.demo.web.response;

import it.sara.demo.dto.StatusDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GenericResponse {
    private StatusDTO status;

    public static GenericResponse success(String message) {
        GenericResponse returnValue = new GenericResponse();
        returnValue.setStatus(new StatusDTO(200, message != null ? message : "Success", UUID.randomUUID().toString()));
        return returnValue;
    }
}
