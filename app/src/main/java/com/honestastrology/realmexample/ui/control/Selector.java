package com.honestastrology.realmexample.ui.control;

import io.realm.RealmObject;

public interface Selector<E extends RealmObject> {
    
    public static <T extends RealmObject>
    Selector<T> create(Command<T>[] uiCommandValues){
        return new UISelector<>(uiCommandValues);
    }
    
    public Command<E> selectCommand(int uiId);
    
}
