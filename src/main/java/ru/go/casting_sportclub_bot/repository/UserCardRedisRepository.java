package ru.go.casting_sportclub_bot.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.go.casting_sportclub_bot.model.UserCardRedis;

@Repository
public interface UserCardRedisRepository extends KeyValueRepository<UserCardRedis,Long> {
}
