package com.honestastrology.realmexample.ui.view;

import android.view.View;

public interface LayoutSwitcher {
    
    /** 画面遷移のリクエストを受け取るコールバック **/
    void changeContentView(LayoutType newLayoutType);
    /**
     * 画面遷移時にレイアウト関連のインスタンスをxmlファイルから取り出すメソッド
     * Androidの仕様上、レイアウトxmlファイル読み込み（ setContentView() ）の後に
     * そのファイル内で定義された各レイアウト部品のインスタンスを取得できるようになるため、
     * このメソッドはchangeContentView()（ またはsetContentView() ）の後に呼ばれる必要がある
     * */
    <T extends View> T getParts(Parts targetParts);
    
}
