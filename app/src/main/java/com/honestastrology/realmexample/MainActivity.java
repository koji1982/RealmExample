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
        //Android側で定義されているレイアウトの初期化
        setContentView(R.layout.activity_main);
        //インターフェースを実装した自コード側の初期化
        _viewer         = new DocumentViewer(this);
        _buttonSelector = Selector.create(DocumentCommand.values());
        _dbOperator     = DBOperator.createSimpleOperator(this);
        //現在データベースにあるファイルを表示する
        DocumentCommand.READ.execute(_viewer, _dbOperator);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _dbOperator.closeAll();
    }
    
    @Override
    public void onClick(View view){
        UICommand<Document> clickedCommand 
                = _buttonSelector.selectCommand( view.getId() );
        clickedCommand.execute( _viewer, _dbOperator);
    }
    
}