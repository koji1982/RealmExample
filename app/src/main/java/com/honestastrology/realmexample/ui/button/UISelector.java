package com.honestastrology.realmexample.ui.button;

import java.util.HashMap;
import java.util.Map;

enum UISelector implements Selector {
    
    INSTANCE;
    
    static Selector getInstance(){
        return INSTANCE;
    }
    
    @Override
    public void onClick(int uiId){
        Operation clickedOperation = _clickSelector.get(uiId);
        clickedOperation.sendRequest();
    }
    
    private final Map<Integer, Operation> _clickSelector;
    
    UISelector(){
        _clickSelector = new HashMap<>();
        for(DBOperation DBOperation : DBOperation.values() ){
            _clickSelector.put(DBOperation.getUIId(), DBOperation);
        }
    }
    
}
