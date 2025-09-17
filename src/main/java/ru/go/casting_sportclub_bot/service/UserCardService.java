package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.Choice;
import ru.go.casting_sportclub_bot.model.Faculties;
import ru.go.casting_sportclub_bot.model.Usercard;
import ru.go.casting_sportclub_bot.repository.UserCardRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    public void saveUserCard(long id)    {
        String key = "UserCards:"+id;
        String key2 = "UserSelections:"+id;
        Usercard u = new Usercard();
        u.setTgid(id);
        u.setPhone((String) redisTemplate.opsForHash().get(key,"phone"));
        u.setName((String) redisTemplate.opsForHash().get(key,"name"));
        u.setSurname((String) redisTemplate.opsForHash().get(key,"surname"));
        u.setAge(Integer.valueOf((String) redisTemplate.opsForHash().get(key,"age")));
        u.setFaculty(Faculties.valueOf((String) redisTemplate.opsForHash().get(key,"faculties")));
        u.setEventmaking((String) redisTemplate.opsForHash().get(key,"eventmaking"));
        u.setEventpart((String) redisTemplate.opsForHash().get(key,"eventpart"));
        u.setCourse((String) redisTemplate.opsForHash().get(key,"course"));
        Set<Object> set = redisTemplate.opsForSet().members(key2);
        Set<Choice> c = new HashSet<>();
        for (Object o:set)
        {
            c.add(Choice.valueOf(o.toString()));
        }
        u.setChoices(c);
        u.setRegdate(LocalDate.now());
        userCardRepository.save(u);
        redisTemplate.delete(key);
        redisTemplate.delete(key2);
    }
}
