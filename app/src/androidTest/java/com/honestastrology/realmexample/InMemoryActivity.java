package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;

public class InMemoryActivity extends MainActivity {
    @Override
    protected DBOperator createDBOperator(){
        return DBOperator.getInMemorySyncInstance(
                                    this,
                                    getString(R.string.sync_id),
                                    this,
                                    ()->{} );
    }
}
