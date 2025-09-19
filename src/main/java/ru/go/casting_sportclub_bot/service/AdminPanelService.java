package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.AdminState;
import ru.go.casting_sportclub_bot.repository.AdminRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminPanelService {
    AdminRepository adminRepository;
    RedisTemplate<String,Long> redisTemplate;

    public AdminPanelService(AdminRepository adminRepository, RedisTemplate<String, Long> redisTemplate) {
        this.adminRepository = adminRepository;
        this.redisTemplate = redisTemplate;
    }
    public boolean checkAdmin(Long id)
    {
        String key = "Admin:"+id;
        if (redisTemplate.hasKey(key))
            {
            return true;
            }
        else if (adminRepository.existsById(id))
        {
            redisTemplate.opsForHash().put(key,"state", AdminState.MENU);
            return true;
        }
        return false;
    }

    public void setState(long userID, AdminState adminState) {
        String key = "Admin:"+userID;
        redisTemplate.opsForHash().put(key,"state", adminState);
    }
    public AdminState checkState(long userID)
    {
        String key = "Admin:"+userID;
        Object o = redisTemplate.opsForHash().get(key,"state");
        if(o==null)
        {
            return AdminState.MENU;
        }
        else
        {
            return AdminState.valueOf(o.toString());
        }
    }
}
