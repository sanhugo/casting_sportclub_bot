package ru.go.casting_sportclub_bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RedisHash(value = "UserCards")
@Getter
@Setter
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
