package com.honestastrology.realmexample;

import android.os.Bundle;
import android.view.View;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.control.Selector;
import com.honestastrology.realmexample.ui.control.UICommand;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener,
                                             LayoutSwitcher {
    
    private Viewer<Document>   _viewer;
    private Selector<Document> _buttonSelector;
    private DBOperator         _dbOperator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //インターフェースを実装したクラスの初期化
        _viewer         = new DocumentViewer( this );
        _dbOperator     = DBOperator.createSimpleOperator(
                                                this,
                                                this,
                                                getString( R.string.async_file_name ),
                                                getString( R.string.sync_id ));
        _buttonSelector = Selector.create( DocumentCommand.values() );
        
        DocumentCommand.READ.execute( _viewer, _dbOperator );
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _dbOperator.closeAll();
    }
    
    /**
     * タッチ操作のイベントをOS側からこのメソッドで受け取っている
     * 
     * EntryPointとなるMainActivity側から操作する場合は
     * ここに処理を記述する
     * */
    @Override
    public void onClick(View view){
        UICommand<Document> clickedCommand 
                = _buttonSelector.selectCommand( view.getId() );
        clickedCommand.execute( _viewer, _dbOperator);
    }
    
    /**
     * */
    @Override
    public void onBackPressed(){
        
    }
    
    /**
     * 起動時に表示される画面を呼び出すコールバック
     * データベースの準備が完了したタイミングで呼ばれることを想定している
     * */
    @Override
    public void showEntryView(){
        //データベースにあるファイルを読み込んでタイトルリストを表示する
        DocumentCommand.READ.execute( _viewer, _dbOperator );
    }
    
    /**
     * 画面遷移のリクエストを受け取るコールバック
     * 
     **/
    @Override
    public void changeContentView(int resourceReference){
        setContentView( resourceReference );
    }
    
    /**
     * 画面遷移時にレイアウト関連のインスタンスをxmlファイルから取り出すメソッド
     * Androidの仕様上、レイアウトxmlファイル読み込み（ setContentView() ）の後に
     * そのファイル内で定義された各レイアウト部品のインスタンスを取得できるようになるため、
     * このメソッドはchangeContentView()（ またはsetContentView() ）の後に呼ばれる必要がある
     * */
    @Override
    public <T extends View> T getViewFromCurrentLayout(int resourceId){
        return findViewById( resourceId );
    }
    
}