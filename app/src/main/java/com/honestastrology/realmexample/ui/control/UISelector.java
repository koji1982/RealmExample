package com.honestastrology.realmexample.ui.control;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

class UISelector<T extends RealmObject> implements Selector<T> {
    
    UISelector(UICommand<T>[] uiCommandValues){
        _clickSelector = new HashMap<>();
        for(UICommand<T> uiCommand: uiCommandValues ){
            _clickSelector.put(uiCommand.getUIId(), uiCommand);
        }
    }
    
    @Override
    public UICommand<T> selectCommand(int uiId){
        return _clickSelector.get(uiId);
    }
    
    private final Map<Integer, UICommand<T>> _clickSelector;
    
    
}
