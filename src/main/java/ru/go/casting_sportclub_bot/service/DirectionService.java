package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.Choice;

import java.util.HashSet;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DirectionService {
    RedisTemplate<String, Object> redisTemplate;

    public DirectionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private String key(Long chatId) {
        return "UserSelections:" + chatId;
    }

    public void addSelection(Long chatId, Choice value) {
        redisTemplate.opsForSet().add(key(chatId), value.name());
    }

    public void removeSelection(Long chatId, Choice value) {
        redisTemplate.opsForSet().remove(key(chatId), value.name());
    }

    public Set<Choice> getSelections(Long chatId) {
        Set<Object> ob= redisTemplate.opsForSet().members(key(chatId));
        Set<Choice> c = new HashSet<>();
        for (Object d:ob)
        {
            c.add(Choice.valueOf(d.toString()));
        }
        return c;
    }
}
