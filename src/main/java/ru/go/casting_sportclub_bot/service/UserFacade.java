package ru.go.casting_sportclub_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.go.casting_sportclub_bot.model.AdminState;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.model.Choice;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserFacade {
    UserCardService userCardService;
    UserStateService userStateService;
    AdminPanelService  adminPanelService;

    public UserFacade(UserCardService userCardService, UserStateService userStateService, AdminPanelService adminPanelService) {
        this.userCardService = userCardService;
        this.userStateService = userStateService;
        this.adminPanelService = adminPanelService;
    }

    public boolean hasUser(Long id)
    {
        return (userStateService.hasUser(id) || userCardService.hasUser(id));
    }

    public void newState(long userID, BotStatus botStatus) {
        userStateService.newState(userID,botStatus);
    }

    public BotStatus checkStatus(long id)
    {
        return userStateService.checkStatus(id);
    }

    public void addProperty(long userID, String field, String f)
    {
        userCardService.addProperty(userID,field,f);
    }

    public void saveUserCard(long userID) {
        userCardService.saveUserCard(userID);
    }

    public long checkRegs(Choice choice)
    {
        return userCardService.checkRegs(choice);
    }
    public boolean isAdmin(Long id)
    {
        return adminPanelService.checkAdmin(id);
    }

    public void newState(long userID, AdminState adminState) {
        adminPanelService.setState(userID,adminState);
    }

    public AdminState checkAdminState(long userID) {
        return adminPanelService.checkState(userID);
    }

    public long checkNotes() {
        return userCardService.countRegs();
    }

    public long checkTodayNotes() {
        return userCardService.countTodayRegs();
    }
}
