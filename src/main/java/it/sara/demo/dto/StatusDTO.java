package it.sara.demo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object che rappresenta lo stato di un'operazione.
 *
 * Contiene:
 * code: codice numerico (es. 200 OK, 400 errore validazione, 500 errore interno)
 * message: descrizione dell'esito
 * traceId: identificativo univoco
 */
@Getter
@Setter
public class StatusDTO {
    private int code;
    private String message;
    private String traceId;

    public StatusDTO(int code, String message, String traceId) {
        this.code = code;
        this.message = message;
        this.traceId = traceId;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public String getTraceId() { return traceId; }


}
