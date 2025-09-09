package ru.go.casting_sportclub_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CastingSportclubBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CastingSportclubBotApplication.class, args);
    }

}
