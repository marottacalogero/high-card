package it.sara.demo.service.user.impl;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.dto.UserDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.StatusService;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.criteria.CriteriaUpdateUser;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.DeleteUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.service.user.result.UpdateUserResult;
import it.sara.demo.service.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatusServiceImpl implements StatusService {

    /**
     * Verifica che l'applicazione sia attiva.
     *
     * @return uno StatusDTO con codice 200 se tutto è ok
     * @throws GenericException se il controllo fallisce
     */
    @Override
    public StatusDTO healthCheck() throws GenericException {
        try {
            StatusDTO status = new StatusDTO();
            status.setCode(200);
            status.setMessage("OK");
            status.setTraceId(UUID.randomUUID().toString());
            return status;
        } catch (Exception e) {
            throw new GenericException(500, "Health check failed");
        }
    }

    /**
     * Valida i dati essenziali di un utente.
     *
     * @param user da validare
     * @return uno StatusDTO con codice 200 se valido
     * @throws GenericException se mancano campi obbligatori
     */
    @Override
    public StatusDTO validateUser(User user) throws GenericException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new GenericException(400, "Email is required");
        }
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new GenericException(400, "First name is required");
        }

        StatusDTO status = new StatusDTO();
        status.setCode(200);
        status.setMessage("User is valid");
        status.setTraceId(UUID.randomUUID().toString());
        return status;
    }
}