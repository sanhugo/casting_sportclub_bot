package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.model.Faculties;
import ru.go.casting_sportclub_bot.model.UserState;
import ru.go.casting_sportclub_bot.repository.UserStateRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserStateService {
    UserStateRepository userStateRepository;
    RedisTemplate<String,Object> redisTemplate;

    public UserStateService(UserStateRepository userStateRepository, RedisTemplate<String, Object> redisTemplate) {
        this.userStateRepository = userStateRepository;
        this.redisTemplate = redisTemplate;
    }
    public void newState(Long id, BotStatus botStatus)
    {
        String key = "State:"+id;
        if (redisTemplate.hasKey(key))
        {
            UserState us = new UserState();
            us.setId(id);
            us.setStatus(botStatus);
            userStateRepository.save(us);
        }
        else
        {
            redisTemplate.opsForHash().put(key,"status",botStatus.name());
        }
    }

    public boolean hasUser(Long id) {
        String key = "State:"+id;
        return redisTemplate.hasKey(key);
    }
    public BotStatus checkStatus(long id)
    {
        Object o = redisTemplate.opsForHash().get("State:"+id,"status");
        if (o!=null)
            return BotStatus.valueOf(redisTemplate.opsForHash().get("State:"+id,"status").toString());
        else
            return BotStatus.CONTACT;
    }

    public void addProperty(long userID, String field, String f) {
        String key = "UserCards:"+userID;
        redisTemplate.opsForHash().put(key,field,f);
    }
}
