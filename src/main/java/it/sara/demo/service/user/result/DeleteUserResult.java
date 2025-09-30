package it.sara.demo.service.user.result;

import it.sara.demo.service.result.GenericResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserResult extends GenericResult {
    private String userId;
    private String message;
}
