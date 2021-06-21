package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;

class InMemoryDBActivity extends MainActivity {
    @Override
    protected DBOperator createDBOperator(){
        return DBOperator.getInMemoryInstance( this, this );
    }
}
