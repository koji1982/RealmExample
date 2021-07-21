package com.honestastrology.realmexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.honestastrology.realmexample.database.DBErrorCallback;
import com.honestastrology.realmexample.ui.control.CommandControl;
import com.honestastrology.realmexample.ui.control.SendCommand;
import com.honestastrology.realmexample.ui.view.DisplayTextChanger;
import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.LayoutType;
import com.honestastrology.realmexample.ui.view.Parts;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.control.RequestCommand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 起動時に呼ばれるクラス。DBOperator, Viewerを保持する。
 * 他クラスからの操作コマンド、またはOSからタッチイベントを
 * 変換した操作コマンドを実行することで
 * DBOperator, Viewerを操作する。
 * 
 * Androidの仕様上、タッチイベント取得の他、
 * レイアウトの変更、テキスト表示、エラーメッセージ表示の機能等、
 * OS側が提供する機能の多くをActivityから行うことになっている為、
 * それらのコールバックインターフェースを実装している。
 * */
public class MainActivity extends AppCompatActivity
                          implements CommandControl<Document>,
                                     LayoutSwitcher,
                                     DisplayTextChanger,
                                     DBErrorCallback {
    
    private DBOperator         _dbOperator;
    private Viewer<Document>   _viewer;
    
    //LayoutSwitcherとして、currentのLayoutTypeを保持する
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
    
    
    
    //「戻る」ボタンが押された時にOSから呼ばれる処理
    @Override
    public void onBackPressed(){
        if( _currentLayoutType.isEntryPage() ){
            finish();
            return;
        }
        UIRequestCommand.READ.execute( _viewer, _dbOperator );
    }
    
    // 画面遷移のリクエストを受け取るコールバック
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
    
    //
    @Override
    public void setConnectType(String newString){
        setTitle( getString( R.string.app_name) + " (" + newString + ")" );
    }
    
    @Override
    public void setSwitcherText(String newString){
        Button connectSwitch = findViewById(R.id.sync_async_button);
        connectSwitch.setText( newString );
    }
    
    @Override
    public void onError(@NonNull String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
}