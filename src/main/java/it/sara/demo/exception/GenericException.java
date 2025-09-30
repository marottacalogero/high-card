package it.sara.demo.exception;

import it.sara.demo.dto.StatusDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericException extends Exception {

    public static final StatusDTO GENERIC_ERROR =
            new StatusDTO(500, "Generic error", null);

    private final StatusDTO status;

    // costruzione da StatusDTO già pronto
    public GenericException(StatusDTO status) {
        this.status = status;
    }

    // costruzione da code+message (genera anche il traceId)
    public GenericException(int code, String message) {
        this.status = new StatusDTO(code, message, java.util.UUID.randomUUID().toString());
    }

    public StatusDTO getStatus() {
        return status;
    }
}
