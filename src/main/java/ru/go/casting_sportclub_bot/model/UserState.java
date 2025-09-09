package ru.go.casting_sportclub_bot.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value="State")
@Getter
@Setter
public class UserState {
    @Id
    private Long id;
    private BotStatus status;
}
