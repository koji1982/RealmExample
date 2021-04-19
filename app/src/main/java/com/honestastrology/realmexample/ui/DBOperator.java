package com.honestastrology.realmexample.ui;

import android.content.Context;

import com.honestastrology.realmexample.realm.Database;
import com.honestastrology.realmexample.ui.button.Selector;

class DBOperator implements Operator {
    
    private Database _asyncDB;
    private Database _syncDB;
    
    private Selector _uiSelector;
    
    DBOperator(Context context){
        _asyncDB = Database.initAsync(context);
        _syncDB  = Database.initSync(context);
    }
    
    @Override
    public void execute(int uiId){
        
    }
    
    @Override
    public void closeAll(){
        _asyncDB.close();
        _syncDB.close();
    }
    
}
