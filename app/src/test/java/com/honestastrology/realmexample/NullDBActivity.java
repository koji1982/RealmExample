package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;

/**単体テスト用にRealmを止めたMainActivity */
class NullDBActivity extends MainActivity {
    @Override
    protected DBOperator createDBOperator(){
        return DBOperator.getNullInstance();
    }
}
