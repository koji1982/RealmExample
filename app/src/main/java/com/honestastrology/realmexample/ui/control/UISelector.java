package com.honestastrology.realmexample.ui.control;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

class UISelector<T extends RealmObject> implements Selector<T> {
    
    UISelector(Command<T>[] uiCommandValues){
        _clickSelector = new HashMap<>();
        for(Command<T> uiCommand: uiCommandValues ){
            _clickSelector.put(uiCommand.getUIId(), uiCommand);
        }
    }
    
    @Override
    public Command<T> selectCommand(int uiId){
        return _clickSelector.containsKey( uiId ) ?
                                        _clickSelector.get( uiId )
                                        : Command.getNullInstance();
    }
    
    private final Map<Integer, Command<T>> _clickSelector;
    
    
}
