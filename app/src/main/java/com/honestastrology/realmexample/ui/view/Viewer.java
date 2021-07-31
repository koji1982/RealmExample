package com.honestastrology.realmexample.ui.view;

import com.honestastrology.realmexample.database.ConnectType;

import java.util.Iterator;

import io.realm.RealmObject;

/** CommandControlに引数として渡され、そのメソッド内で
 *  表示画面の切り替えと、切り替え時のRealmObjectの受け渡しを行う  */
public interface Viewer<E extends RealmObject> {
    
    public void showList(Iterator<E> iterator);
    
    public void show(E realmObject);
    
    public void updateDisplayString(ConnectType ConnectType);
    
}
