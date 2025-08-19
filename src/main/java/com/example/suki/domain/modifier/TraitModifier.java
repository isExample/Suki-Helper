package com.example.suki.domain.modifier;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.trait.TraitCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraitModifier {
    public void modify(UserState userState, List<TraitCategory> traitList){
        for(TraitCategory trait : traitList){
            trait.apply(userState);
        }
    }
}
