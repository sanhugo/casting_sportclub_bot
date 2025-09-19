package ru.go.casting_sportclub_bot.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Admin")
@Data
public class AdminRedis {
    @Id
    private Long tgid;
    private AdminState state;
}
