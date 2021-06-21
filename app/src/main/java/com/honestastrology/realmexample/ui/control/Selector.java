package com.honestastrology.realmexample.ui.control;

import io.realm.RealmObject;

public interface Selector<E extends RealmObject> {
    
    public static <T extends RealmObject>
    Selector<T> create(ReceiveCommand<T>[] uiReceiveCommandValues){
        return new UISelector<>(uiReceiveCommandValues);
    }
    
    public ReceiveCommand<E> selectCommand(int uiId);
    
}
