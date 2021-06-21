package com.honestastrology.realmexample.ui.view;

import com.honestastrology.realmexample.database.ConnectType;

import java.util.Iterator;

import io.realm.RealmObject;

/**
 * ビューの切り替えと、切り替え時のRealmObjectの受け渡しを行うインターフェース
 * */
public interface Viewer<E extends RealmObject> {
    
    /** DB接続切り替え時のViewer側の処理 */
    public void setConnectString(ConnectType ConnectType);
    
    public void show(Iterator<E> iterator);
    
    public void show(E realmObject);
    
}
