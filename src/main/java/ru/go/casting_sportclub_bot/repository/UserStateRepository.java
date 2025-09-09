package ru.go.casting_sportclub_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.go.casting_sportclub_bot.model.UserState;

@Repository
public interface UserStateRepository extends CrudRepository<UserState,Long> {
}
