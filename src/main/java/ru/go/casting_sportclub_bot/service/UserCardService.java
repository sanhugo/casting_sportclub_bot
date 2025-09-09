package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.UserCard;
import ru.go.casting_sportclub_bot.repository.UserCardRedisRepository;
import ru.go.casting_sportclub_bot.repository.UserCardRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserCardService {
    UserCardRepository userCardRepository;
    RedisTemplate<String,Object> redisTemplate;

    public UserCardService(UserCardRepository userCardRepository, RedisTemplate<String, Object> redisTemplate) {
        this.userCardRepository = userCardRepository;
        this.redisTemplate = redisTemplate;
    }

    public boolean hasUser(Long id) {
        String key = "UserCard:"+id;
        return redisTemplate.hasKey(key) || userCardRepository.existsByTgid(id);
    }

    @Async
    public void saveUserCard(long id)
    {
        String key = "UserCard:"+id;
        UserCard u = new UserCard();
        u.setTgid(id);
        u.setPhone((String) redisTemplate.opsForHash().get(key,"phone"));
        u.setName((String) redisTemplate.opsForHash().get(key,"name"));
        u.setSurname((String) redisTemplate.opsForHash().get(key,"surname"));
        u.setAge(Integer.parseInt(redisTemplate.opsForHash().get(key,"age").toString()));

        
        userCardRepository.save(u);
    }
}
