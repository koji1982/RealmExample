package com.honestastrology.realmexample.ui.control;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

class UISelector<T extends RealmObject> implements Selector<T> {
    
    UISelector(ReceiveCommand<T>[] uiReceiveCommandValues){
        _clickSelector = new HashMap<>();
        for(ReceiveCommand<T> uiReceiveCommand : uiReceiveCommandValues){
            _clickSelector.put(uiReceiveCommand.getUIId(), uiReceiveCommand);
        }
    }
    
    @Override
    public ReceiveCommand<T> selectCommand(int uiId){
        return _clickSelector.containsKey( uiId ) ?
                                        _clickSelector.get( uiId )
                                        : ReceiveCommand.getNullInstance();
    }
    
    private final Map<Integer, ReceiveCommand<T>> _clickSelector;
    
    
}
