package ru.go.casting_sportclub_bot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.go.casting_sportclub_bot.model.UserState;

public interface UserStateRepository extends CrudRepository<UserState,Long> {
}
