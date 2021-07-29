package com.honestastrology.realmexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.honestastrology.realmexample.database.*;
import com.honestastrology.realmexample.ui.control.*;
import com.honestastrology.realmexample.ui.view.*;

/**
 * 起動時に呼ばれるクラス。DBOperator, Viewerを保持する。
 * 他クラスからの操作コマンド、またはOSからタッチイベントを
 * 変換した操作コマンドを実行することで
 * DBOperator, Viewerを操作する。
 * 
 * Androidの仕様上、タッチイベント取得の他、
 * レイアウトの変更、テキスト表示、エラーメッセージ表示の機能等、
 * OS側が提供する機能の多くをActivityから行うことになっている為、
 * それらを使用するためのコールバックインターフェースを実装している。
 * */
public class MainActivity extends AppCompatActivity
                          implements CommandControl<Document>,
                                     LayoutSwitcher,
                                             LabelDisplay,
                                     DBErrorCallback {
    
    private DBOperator         _dbOperator;
    private Viewer<Document>   _viewer;
    
    private LayoutType         _currentLayoutType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //このMainActivityが、DBOperatorとViewerの参照を保持しコントロールを行う
        _viewer         = new DocumentViewer( this );
        _dbOperator     = createDBOperator();
        
        //データを読込み、起動画面を表示する
        UIRequestCommand.READ.execute( _viewer, _dbOperator );
    }
    
    //In-Memory DB, Null DBと入れ替える場合のために
    //生成メソッドとして抽出
    protected DBOperator createDBOperator(){
        return DBOperator.createBasicOperator(
                this,
                getString( R.string.async_file_name ),
                getString( R.string.sync_id ),
                this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _dbOperator.closeAll();
    }
    
    @Override
    public void request(@NonNull RequestCommand<Document> command){
        command.execute(_viewer, _dbOperator);
    }
    
    @Override
    public void send(@NonNull SendCommand<Document> command,
                     @NonNull Document              sendTarget ){
        command.execute( _dbOperator, sendTarget );
    }
    
    @Override
    public void onBackPressed(){
        if( _currentLayoutType.isEntryPage() ){
            finish();
            return;
        }
        UIRequestCommand.READ.execute( _viewer, _dbOperator );
    }
    
    @Override
    public void changeContentView(LayoutType newLayoutType){
        _currentLayoutType = newLayoutType;
        setContentView( newLayoutType.getResourceRef() );
    }
    
    // 画面遷移時にレイアウト関連のインスタンスをxmlファイルから取り出すメソッド
    // Androidの仕様上、レイアウトxmlファイル読み込み（ setContentView() ）の後に
    // そのファイル内で定義された各レイアウト部品のインスタンスを取得できるようになるため、
    // このメソッドはchangeContentView()（ またはsetContentView() ）の後に呼ばれる必要がある
    @Override
    public <T extends View> T getParts(Parts targetParts){
        return findViewById( targetParts.getResourceId() );
    }
    
    @Override
    public void updateLabel(String newString){
        setTitle( getString( R.string.app_name) + " (" + newString + ")" );
    }
    
    @Override
    public void updateButton(String newString){
        Button connectSwitchButton = findViewById(R.id.sync_async_button);
        connectSwitchButton.setText( newString );
    }
    
    @Override
    public void onError(@NonNull String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
}