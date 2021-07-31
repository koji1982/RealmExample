package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

import io.realm.RealmObject;

/** CommandControlへ渡されてそこで実行される操作コマンド。
 * 実装クラスではDBOperatorとViewerへの処理を記述する  */
public interface RequestCommand<E extends RealmObject> {
    
    public void execute(Viewer<E> viewer, DBOperator _dbOperator);
    
}
