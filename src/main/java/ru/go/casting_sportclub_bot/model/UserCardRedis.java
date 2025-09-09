package ru.go.casting_sportclub_bot.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash(value = "UserCards")
@Data
public class UserCardRedis {
    @Id
    private Long tgid;

    private Faculties faculty;
    private String phone;
    private Integer age;
    private String name;
    private String surname;
    private String course;
    private String eventmaking;
    private String eventpart;
}
