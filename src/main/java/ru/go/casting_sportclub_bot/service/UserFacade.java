package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.BotStatus;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserFacade {
    UserCardService userCardService;
    UserStateService userStateService;

    public UserFacade(UserCardService userCardService, UserStateService userStateService) {
        this.userCardService = userCardService;
        this.userStateService = userStateService;
    }

    public boolean hasUser(Long id)
    {
        return (userStateService.hasUser(id) || userCardService.hasUser(id));
    }

    public void newState(long userID, BotStatus botStatus) {
        userStateService.newState(userID,botStatus);
    }
}
