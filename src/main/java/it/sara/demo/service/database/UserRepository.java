package it.sara.demo.service.database;

import it.sara.demo.service.database.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    public boolean save(User user) {
        user.setGuid(java.util.UUID.randomUUID().toString());
        FakeDatabase.TABLE_USER.add(user);
        return true;
    }

    public Optional<User> getByGuid(String guid) {
        return FakeDatabase.TABLE_USER.stream().filter(u -> u.getGuid().equals(guid)).findFirst();
    }

    public List<User> getAll() {
        return FakeDatabase.TABLE_USER;
    }

    public boolean deleteByGuid(String guid) {
        return FakeDatabase.TABLE_USER.removeIf(u -> u.getGuid().equals(guid));
    }
}
