package com.example.suki.modifier;

import com.example.suki.domain.UserState;
import com.example.suki.domain.trait.TraitCategory;

import java.util.List;

public class TraitModifier {
    public void modify(UserState userState, List<TraitCategory> traitList){
        if(traitList.size() > 6) throw new IllegalArgumentException("특성은 최대 6개로 제한됩니다.");

        for(TraitCategory trait : traitList){
            trait.apply(userState);
        }
    }
}
