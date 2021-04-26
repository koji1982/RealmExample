package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.dbaccess.DBOperator;
import com.honestastrology.realmexample.ui.layout.Viewer;

import io.realm.RealmObject;

/**
 * このインターフェースを実装したクラスで
 * Viewerからのリクエスト、DBOperatorから返される結果を処理する
 * 
 * @param <E>
 */

public interface UICommand<E extends RealmObject> {
    
    public int getUIId();
    
    public void execute(Viewer<E> viewer, DBOperator _dbOperator);
    
}
