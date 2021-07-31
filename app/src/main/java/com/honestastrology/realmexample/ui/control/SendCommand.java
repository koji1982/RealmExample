package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.database.DBOperator;

import io.realm.RealmObject;

/** CommandControlへ渡されてそこで実行される操作コマンド。
 * 引数としてRealmObjectをDBへ渡す必要がある操作で使用する  */
public interface SendCommand<E extends RealmObject> {
    
    public void execute(DBOperator operator, E realmObject);
    
}
