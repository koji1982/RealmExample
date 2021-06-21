package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

import io.realm.RealmObject;

class NullReceiveCommand<E extends RealmObject> implements ReceiveCommand<E> {
    
    @Override
    public int getUIId(){
        return INVALID_UI_ID;
    }
    
    @Override
    public void execute(Viewer<E> viewer, DBOperator dbOperator){
        
    }
    
}
