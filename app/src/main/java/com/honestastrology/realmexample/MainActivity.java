package com.honestastrology.realmexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.honestastrology.realmexample.database.DBErrorCallback;
import com.honestastrology.realmexample.ui.view.DisplayTextChanger;
import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.LayoutType;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.control.Selector;
import com.honestastrology.realmexample.ui.control.Command;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener,
                                             LayoutSwitcher,
                                             DisplayTextChanger,
                                             DBErrorCallback {
    
    private Viewer<Document>   _viewer;
    private Selector<Document> _buttonSelector;
    private DBOperator         _dbOperator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //MVC各インターフェースを実装したクラスの初期化
        _viewer         = new DocumentViewer( this );
        _dbOperator     = DBOperator.createSimpleOperator(
                                                this,
                                                getString( R.string.async_file_name ),
                                                getString( R.string.sync_id ),
                                                this);
        _buttonSelector = Selector.create( DocumentCommand.values() );
        //データを読込み、起動画面を表示する
        DocumentCommand.READ.execute( _viewer, _dbOperator );
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _dbOperator.closeAll();
    }
    
    // タッチ操作のイベントをOSからこのメソッドで受け取っている
    // EntryPointとなるMainActivity側から操作する場合は
    // ここに処理を記述する
    @Override
    public void onClick(View view){
        Command<Document> clickedCommand 
                = _buttonSelector.selectCommand( view.getId() );
        clickedCommand.execute( _viewer, _dbOperator);
    }
    
    //「戻る」ボタンが押された時にOSから呼ばれる処理
    @Override
    public void onBackPressed(){
        
        LayoutType<Document> pageType = _viewer.getCurrentPageType();
        if( pageType.isEntryPage() ){
            finish();
            return;
        }
        
        _viewer.transitViewPage( pageType.getPrevPageType() );
    }
    
    // 画面遷移のリクエストを受け取るコールバック
    @Override
    public void changeContentView(int resourceReference){
        setContentView( resourceReference );
    }
    
    
    // 画面遷移時にレイアウト関連のインスタンスをxmlファイルから取り出すメソッド
    // Androidの仕様上、レイアウトxmlファイル読み込み（ setContentView() ）の後に
    // そのファイル内で定義された各レイアウト部品のインスタンスを取得できるようになるため、
    // このメソッドはchangeContentView()（ またはsetContentView() ）の後に呼ばれる必要がある
    @Override
    public <T extends View> T getViewFromCurrentLayout(int resourceId){
        return findViewById( resourceId );
    }
    
    //
    @Override
    public void changeTitle(String newString){
        setTitle( getString( R.string.app_name) + " (" + newString + ")" );
    }
    
    @Override
    public void changeSwitcher(String newString){
        Button connectSwitch = findViewById(R.id.sync_async_button);
        connectSwitch.setText( newString );
    }
    
    @Override
    public void onError(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
}