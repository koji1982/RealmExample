package com.honestastrology.realmexample.ui;

import android.content.Context;

import com.honestastrology.realmexample.realm.DBOperator;

public interface Operator {
    
    static Operator create(Context context){
        return new DBOperator(context);
    }
    
    void execute(int uiId);
    
    void closeAll();
    
}
