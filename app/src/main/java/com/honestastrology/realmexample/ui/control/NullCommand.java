package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

import io.realm.RealmObject;

class NullCommand<E extends RealmObject> implements Command<E> {
    
    @Override
    public int getUIId(){
        return INVALID_UI_ID;
    }
    
    @Override
    public void execute(Viewer<E> viewer, DBOperator dbOperator){
        
    }
    
}
