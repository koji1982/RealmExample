package com.honestastrology.realmexample.ui.layout;

import android.app.Activity;
import android.view.View;

import io.realm.RealmObject;

public interface LayoutSwitcher<E extends RealmObject> {
    /**
     * Androidではレイアウト関連のインスタンス生成にActivityが必要になる場合が
     * 多いので、このインターフェースを実装したクラスのinitialize()内で作れるものは
     * 
     * */
    LayoutSwitcher<E> initialize(Activity activity);
    
    void registerActivity(Activity activity);
    
    void registerViewPage(ViewPage<E> viewPage);
    
    void setNewLayout(LayoutType layoutType);
    
    View findViewById(int resourceId);
    
}
