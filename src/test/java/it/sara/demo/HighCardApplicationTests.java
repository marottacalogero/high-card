package it.sara.demo;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.exception.GenericException;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HighCardApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;
    @Test
    void contextLoads() {
    }

    @Test
    void addUser_successfully() throws GenericException {
        CriteriaAddUser criteria = new CriteriaAddUser();
        criteria.setFirstName("Mario");
        criteria.setLastName("Rossi");
        criteria.setEmail("mario.rossi@example.com");
        criteria.setPhoneNumber("1234567890");

        AddUserResult result = userService.addUser(criteria);

        assertNotNull(result.getUserId());
        assertEquals("User created successfully", result.getMessage());
    }

    @Test
    void getUsers_returnsUsers() throws GenericException {
        CriteriaAddUser criteria = new CriteriaAddUser();
        criteria.setFirstName("Luca");
        criteria.setLastName("Bianchi");
        criteria.setEmail("luca.bianchi@example.com");
        criteria.setPhoneNumber("987654321");

        userService.addUser(criteria);

        CriteriaGetUsers searchCriteria = new CriteriaGetUsers();
        searchCriteria.setQuery("Luca");

        GetUsersResult result = userService.getUsers(searchCriteria);

        assertFalse(result.getUsers().isEmpty());
        assertEquals("Luca", result.getUsers().get(0).getFirstName());
    }

    @Test
    void getUsers_withPaginationAndOrder() throws GenericException {
        // aggiungi più utenti
        for (int i = 0; i < 5; i++) {
            CriteriaAddUser c = new CriteriaAddUser();
            c.setFirstName("Name" + i);
            c.setLastName("Last" + i);
            c.setEmail("user" + i + "@example.com");
            c.setPhoneNumber("12345" + i);
            userService.addUser(c);
        }

        CriteriaGetUsers criteria = new CriteriaGetUsers();
        criteria.setOffset(0);
        criteria.setLimit(2);
        criteria.setOrder(CriteriaGetUsers.OrderType.BY_FIRSTNAME_DESC);

        GetUsersResult result = userService.getUsers(criteria);

        assertEquals(2, result.getUsers().size());
    }

    @Test
    void updateUser_successfully() throws GenericException {
        CriteriaAddUser addCriteria = new CriteriaAddUser();
        addCriteria.setFirstName("Filippo");
        addCriteria.setLastName("Bianchi");
        addCriteria.setEmail("filippo.bianchi@example.com");
        addCriteria.setPhoneNumber("1234567890");
        AddUserResult addResult = userService.addUser(addCriteria);

        CriteriaUpdateUser updateCriteria = new CriteriaUpdateUser();
        updateCriteria.setFirstName("Luigi");
        UpdateUserResult updateResult = userService.updateUser(addResult.getUserId(), updateCriteria);

        assertNotNull(updateResult.getUserId());
        assertEquals("User updated successfully", updateResult.getMessage());
    }

    @Test
    void deleteUser_successfully() throws GenericException {
        CriteriaAddUser addCriteria = new CriteriaAddUser();
        addCriteria.setFirstName("Anna");
        addCriteria.setLastName("Bianchi");
        addCriteria.setEmail("anna.bianchi@example.com");
        addCriteria.setPhoneNumber("2222222222");
        AddUserResult addResult = userService.addUser(addCriteria);

        DeleteUserResult deleteResult = userService.deleteUser(addResult.getUserId());

        assertEquals("User deleted successfully", deleteResult.getMessage());

        assertThrows(GenericException.class, () -> userService.deleteUser(addResult.getUserId()));
    }

    @Test
    void healthCheck_success() throws GenericException {
        StatusDTO status = statusService.healthCheck();

        assertNotNull(status.getTraceId());
        assertEquals(200, status.getCode());
        assertEquals("OK", status.getMessage());
    }

    @Test
    void validateUser_success() throws GenericException {
        User user = new User();
        user.setFirstName("Mario");
        user.setEmail("mario.rossi@example.com");

        StatusDTO status = statusService.validateUser(user);

        assertEquals(200, status.getCode());
        assertEquals("User is valid", status.getMessage());
    }
}
