package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.Choice;

import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DirectionService {
    RedisTemplate<String, Choice> redisTemplate;

    public DirectionService(RedisTemplate<String, Choice> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private String key(Long chatId) {
        return "UserSelections:" + chatId;
    }

    public void addSelection(Long chatId, Choice value) {
        redisTemplate.opsForSet().add(key(chatId), value);
    }

    public void removeSelection(Long chatId, Choice value) {
        redisTemplate.opsForSet().remove(key(chatId), value);
    }

    public Set<Choice> getSelections(Long chatId) {
        return redisTemplate.opsForSet().members(key(chatId));
    }
}
