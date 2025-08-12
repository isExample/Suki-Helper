package com.example.suki.modifier;

import com.example.suki.domain.UserState;
import com.example.suki.domain.trait.TraitCategory;

import java.util.List;

public class TraitModifier {
    public void modify(UserState userState, List<TraitCategory> traitList){
        for(TraitCategory trait : traitList){
            trait.apply(userState);
        }
    }
}
