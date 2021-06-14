package com.honestastrology.realmexample.ui.view;

import com.honestastrology.realmexample.database.ConnectType;

import java.util.Iterator;
import java.util.List;

import io.realm.RealmObject;

/**
 * ビューの切り替えと、切り替え時のRealmObjectの受け渡しを行うインターフェース
 * */
public interface Viewer<E extends RealmObject> {
    
    public void transitViewPage(LayoutType<E> layoutType);
    
    public void setContents(Iterator<E> contents);
    
    public List<E> getContents();
    
    public void setSelectedContent(E selectedContent);
    
    public E getSelectedContent();
    /** DB接続切り替え時のViewer側の処理 */
    public void updateConnectString(ConnectType ConnectType);
    
    public E confirmUpdate();
    
    public LayoutType<E> getCurrentPageType();
    
}
