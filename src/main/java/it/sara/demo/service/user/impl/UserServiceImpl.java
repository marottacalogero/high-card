package it.sara.demo.service.user.impl;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
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
import java.util.stream.Collectors;

/**
 * Implementazione dei servizi per la gestione degli utenti.
 * Contiene logica per creare, leggere, aggiornare e cancellare utenti
 * utilizzando un repository in memoria (FakeDatabase).
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * Aggiunge un nuovo utente al sistema.
     *
     * @param criteria dati per creare l'utente
     * @return risultato contenente l'ID del nuovo utente e un messaggio di stato
     * @throws GenericException se sono presenti dati incompleti o se il salvataggio fallisce
     */
    @Override
    public AddUserResult addUser(CriteriaAddUser criteria) throws GenericException {

        validateCriteria(criteria);

        User user = new User();
        user.setFirstName(criteria.getFirstName());
        user.setLastName(criteria.getLastName());
        user.setEmail(criteria.getEmail());
        user.setPhoneNumber(criteria.getPhoneNumber());

        if (!userRepository.save(user)) {
            throw new GenericException(500, "Error saving user");
        }

        AddUserResult result = new AddUserResult();
        result.setUserId(user.getGuid());
        result.setMessage("User created successfully");
        return result;
    }

    private void validateCriteria(CriteriaAddUser criteria) throws GenericException {
        if (stringUtil.isNullOrEmpty(criteria.getFirstName())) {
            throw new GenericException(400, "First name is required");
        }
        if (stringUtil.isNullOrEmpty(criteria.getLastName())) {
            throw new GenericException(400, "Last name is required");
        }
        if (stringUtil.isNullOrEmpty(criteria.getEmail())) {
            throw new GenericException(400, "Email is required");
        }
        if (stringUtil.isNullOrEmpty(criteria.getPhoneNumber())) {
            throw new GenericException(400, "Phone is required");
        }
    }

    @Override
    public GetUsersResult getUsers(CriteriaGetUsers criteria) throws GenericException {
        try {
            List<User> allUsers = userRepository.getAll();

            // Filtraggio
            if (criteria != null && !stringUtil.isNullOrEmpty(criteria.getQuery())) {
                String queryLower = criteria.getQuery().toLowerCase();
                allUsers = allUsers.stream()
                        .filter(u -> u.getFirstName().toLowerCase().contains(queryLower) ||
                                u.getLastName().toLowerCase().contains(queryLower))
                        .collect(Collectors.toList());
            }

            // Ordinamento
            if (criteria != null && criteria.getOrder() != null) {
                switch (criteria.getOrder()) {
                    case BY_FIRSTNAME -> allUsers.sort(Comparator.comparing(User::getFirstName));
                    case BY_FIRSTNAME_DESC -> allUsers.sort(Comparator.comparing(User::getFirstName).reversed());
                    case BY_LASTNAME -> allUsers.sort(Comparator.comparing(User::getLastName));
                    case BY_LASTNAME_DESC -> allUsers.sort(Comparator.comparing(User::getLastName).reversed());
                }
            }

            // Paginazione
            int offset = criteria != null ? criteria.getOffset() : 0;
            int limit = criteria != null && criteria.getLimit() > 0 ? criteria.getLimit() : allUsers.size();
            List<User> pagedUsers = allUsers.stream()
                    .skip(offset)
                    .limit(limit)
                    .collect(Collectors.toList());

            // Conversione in UserDTO
            List<UserDTO> userDTOs = pagedUsers.stream()
                    .map(u -> {
                        UserDTO dto = new UserDTO();
                        dto.setGuid(u.getGuid());
                        dto.setFirstName(u.getFirstName());
                        dto.setLastName(u.getLastName());
                        dto.setEmail(u.getEmail());
                        dto.setPhoneNumber(u.getPhoneNumber());
                        return dto;
                    })
                    .collect(Collectors.toList());

            GetUsersResult result = new GetUsersResult();
            result.setUsers(userDTOs);
            result.setMessage("Users retrieved successfully");

            return result;

        } catch (Exception e) {
            log.error("Errore durante il recupero degli utenti", e);
            throw new GenericException(500, "Error retrieving users");
        }
    }

    @Override
    public UpdateUserResult updateUser(String guid, CriteriaUpdateUser criteria) throws GenericException {
        User user = userRepository.getByGuid(guid)
                .orElseThrow(() -> new GenericException(404, "User not found"));

        if (!stringUtil.isNullOrEmpty(criteria.getFirstName())) user.setFirstName(criteria.getFirstName());
        if (!stringUtil.isNullOrEmpty(criteria.getLastName())) user.setLastName(criteria.getLastName());
        if (!stringUtil.isNullOrEmpty(criteria.getEmail())) user.setEmail(criteria.getEmail());
        if (!stringUtil.isNullOrEmpty(criteria.getPhoneNumber())) user.setPhoneNumber(criteria.getPhoneNumber());

        boolean saved = userRepository.save(user);
        if (!saved) throw new GenericException(500, "Error updating user");

        UpdateUserResult result = new UpdateUserResult();
        result.setUserId(user.getGuid());
        result.setMessage("User updated successfully");
        return result;
    }

    @Override
    public DeleteUserResult deleteUser(String guid) throws GenericException {
        boolean removed = userRepository.deleteByGuid(guid);
        if (!removed) throw new GenericException(404, "User not found");

        DeleteUserResult result = new DeleteUserResult();
        result.setMessage("User deleted successfully");
        return result;
    }
}
