package com.honestastrology.realmexample;

import android.os.Bundle;
import android.view.View;

import com.honestastrology.realmexample.ui.layout.Viewer;
import com.honestastrology.realmexample.dbaccess.DBOperator;
import com.honestastrology.realmexample.ui.control.Selector;
import com.honestastrology.realmexample.ui.control.UICommand;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener {
    
    private Viewer<Document>   _viewer;
    private Selector<Document> _buttonSelector;
    private DBOperator         _dbOperator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //インターフェースを実装したクラスの初期化
        _viewer         = new DocumentViewer(this);
        _dbOperator     = DBOperator.createSimpleOperator(this);
        _buttonSelector = Selector.create(DocumentCommand.values());
        //現在データベースにあるファイルを読み込む
        DocumentCommand.READ.execute(_viewer, _dbOperator);
        //起動画面を表示する
        _viewer.transitViewPage( DisplayLayout.TITLE_LIST );
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _dbOperator.closeAll();
    }
    
    /**
     * EntryPointとなるMainActivity側から操作する場合は
     * ここに処理を記述する
     * 
     * 他のクラスの状態を取得したい場合等、
     * 別のクラス側から操作する場合は
     * こことは別に、そのクラスにリスナー・インターフェースを実装する
     * */
    @Override
    public void onClick(View view){
        UICommand<Document> clickedCommand 
                = _buttonSelector.selectCommand( view.getId() );
        clickedCommand.execute( _viewer, _dbOperator);
    }
    
}