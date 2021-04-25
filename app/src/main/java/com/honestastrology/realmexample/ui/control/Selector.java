package com.honestastrology.realmexample.ui.control;

import io.realm.RealmObject;

public interface Selector<E extends RealmObject> {
    
    public static <T extends RealmObject>
    Selector<T> create(UICommand<T>[] uiCommandValues){
        return new UISelector<>(uiCommandValues);
    }
    
    public UICommand<E> selectCommand(int uiId);
    
}
