package com.example.suki.domain.User;

import org.springframework.stereotype.Component;

@Component
public class UserStateFactory {
    public UserState create(UserContext context) {
        UserState userState = new UserState(context.day());

        if(context.inactiveList() != null){
            context.inactiveList().forEach(userState::deactivatePlace);
        }
        if(context.activeList() != null){
            context.activeList().forEach(userState::activatePlace);
        }

        return userState;
    }

}
