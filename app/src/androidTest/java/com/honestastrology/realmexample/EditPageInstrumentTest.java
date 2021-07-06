package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.honestastrology.realmexample.ReflectionHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EditPageInstrumentTest {
    //Activity関連のInstrumentedTestは基本的に、
    //ActivityScenarioのonActivity()のパラメータ
    //ActivityAction.perform()メソッドのスコープ内で
    //セットアップ、テストメソッドの実行、アサーションを行う
    //参照
    //https://developer.android.com/guide/components/activities/testing
    //https://developer.android.com/reference/androidx/test/core/app/ActivityScenario.ActivityAction
    
    private EditPage     _editPage;
    private DBOperator   _dbOperator;
    
    private boolean      _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
                            = new ActivityScenarioRule<>(MainActivity.class);
    
    //titleとbodyの表示をテスト
    @Test
    public void showDocumentChangeText(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //確認用テキストとDocumentインスタンスの用意
            String titleTestText = "title test text";
            String bodyTestText  = "body test text";
            Document document = createTestDocument( _dbOperator );
            //テスト対象メソッド、比較のため最初に空文字のDocumentを引数として渡しnotEqualを確認
            _editPage.showDocument( document );
            TextView actualTitle = activity.getParts( PartsDefine.TITLE_TEXT );
            TextView actualBody  = activity.getParts( PartsDefine.BODY_TEXT );
            assertNotEquals( titleTestText, actualTitle.getText().toString() );
            assertNotEquals( bodyTestText, actualBody.getText().toString() );
            //titleとbodyの文字列を更新する
            document.updateTitle( titleTestText );
            document.updateText( bodyTestText );
            //テスト対象メソッド、文字列を更新したDocumentを引数として渡す
            _editPage.showDocument( document );
            //titleとbodyが更新されていることを確認
            actualTitle = activity.getParts( PartsDefine.TITLE_TEXT );
            actualBody  = activity.getParts( PartsDefine.BODY_TEXT );
            assertEquals( titleTestText, actualTitle.getText().toString() );
            assertEquals( bodyTestText, actualBody.getText().toString() );
        });
    }
    
    //saveボタンでデータベースが更新されていることをテスト
    @Test
    public void onClickSaveButton(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //確認用テキストとDocumentインスタンスの用意
            Document document = createTestDocument( _dbOperator );
            int targetId = document.getId();
            _editPage.showDocument( document );
            String saveTestTitle = "save test title";
            String saveTestBody  = "save test body";
            
            //Editorに文字列を配置して、ボタンをクリックする前にはnotEqualであることを確認
            TextView titleText = activity.getParts( PartsDefine.TITLE_TEXT );
            TextView bodyText  = activity.getParts( PartsDefine.BODY_TEXT );
            titleText.setText( saveTestTitle );
            bodyText.setText( saveTestBody );
            Document preTestDocument
                    = _dbOperator.getRealmObject( Document.class, "_id", targetId );
            assertNotEquals( saveTestTitle, preTestDocument.getTitle() );
            assertNotEquals( saveTestBody, preTestDocument.getText() );
            
            //テスト対象メソッド
            Button saveButton = activity.getParts( PartsDefine.UPDATE_BUTTON );
            saveButton.callOnClick();
            
            //データベース内のDocumentが変更されていることを確認
            Document postTestDocument
                    = _dbOperator.getRealmObject(Document.class, "_id", targetId);
            assertEquals( saveTestTitle, postTestDocument.getTitle() );
            assertEquals( saveTestBody, postTestDocument.getText() );
        });
    }
    
    //BackButtonでDialogを表示せずそのまま画面遷移する場合のテスト
    @Test
    public void onClickBackButtonNoChange(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //EditPage画面を表示
            activity.request( RequestCommand.CREATE );
            //backButtonクリック前はnullであることを確認
            assertNull( activity.findViewById(R.id.main_frame_layout) );
            
            //テスト対象
            Button backButton = activity.getParts( PartsDefine.BACK_BUTTON );
            backButton.callOnClick();
            
            //クリック後にViewが有効になっていることを確認
            assertTrue( activity.findViewById(R.id.main_frame_layout).isEnabled() );
        });
    }
    
    @Test
    public void onClickBackButtonWithChangedTitle(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //EditPage画面を表示して、titleを変更する
            activity.request( RequestCommand.CREATE );
            String saveTestTitle = "save test title";
            TextView titleText = activity.getParts( PartsDefine.TITLE_TEXT );
            titleText.setText( saveTestTitle );
            //backButtonクリック前はfalseであることを確認
            assertFalse( getBackConfirmDialog().isShowing() );
        
            //テスト対象
            Button backButton = activity.getParts( PartsDefine.BACK_BUTTON );
            backButton.callOnClick();
        
            //クリック後にDialogが有効になっていることを確認
            assertTrue(  getBackConfirmDialog().isShowing() );
        });
    }
    
    @Test
    public void onClickBackButtonWithChangedBody(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //EditPage画面を表示して、titleを変更する
            activity.request( RequestCommand.CREATE );
            String saveTestBody = "save test body";
            TextView bodyText = activity.getParts( PartsDefine.BODY_TEXT );
            bodyText.setText( saveTestBody );
            //backButtonクリック前はfalseであることを確認
            assertFalse( getBackConfirmDialog().isShowing() );
        
            //テスト対象
            Button backButton = activity.getParts( PartsDefine.BACK_BUTTON );
            backButton.callOnClick();
        
            //クリック後にDialogが有効になっていることを確認
            assertTrue(  getBackConfirmDialog().isShowing() );
        });
    }
    
    @Test
    public void onClickBackButtonWithChanged_title_body(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //EditPage画面を表示して、titleを変更する
            activity.request( RequestCommand.CREATE );
            String saveTestTitle = "save test title";
            String saveTestBody = "save test body";
            TextView titleText = activity.getParts( PartsDefine.TITLE_TEXT );
            TextView bodyText = activity.getParts( PartsDefine.BODY_TEXT );
            titleText.setText( saveTestTitle );
            bodyText.setText( saveTestBody );
            //backButtonクリック前はfalseであることを確認
            assertFalse( getBackConfirmDialog().isShowing() );
        
            //テスト対象
            Button backButton = activity.getParts( PartsDefine.BACK_BUTTON );
            backButton.callOnClick();
        
            //クリック後にDialogが有効になっていることを確認
            assertTrue(  getBackConfirmDialog().isShowing() );
        });
    }
    
    
    //ヘルパー関数
    //ActivityAction.perform()内でなければ、
    //xmlリソースとデータベースに同時にアクセスできないため、
    //@Beforeではなくヘルパー関数としてテスト実行前に呼び出す
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _dbOperator = swapInMemoryOperator( activity );
        _editPage   = extractEditPage( activity );
        
        _isInitialized = true;
    }
    
    //ヘルパー関数
    private AlertDialog getBackConfirmDialog(){
        try {
            Field field_dialogHolder = _editPage.getClass().getDeclaredField("_backConfirmDialog");
            field_dialogHolder.setAccessible( true );
            BackConfirmDialog dialogHolder
                    = (BackConfirmDialog)field_dialogHolder.get(_editPage);
            Field field_dialog = dialogHolder.getClass().getDeclaredField("_confirmDialog");
            field_dialog.setAccessible( true );
            return (AlertDialog)field_dialog.get( dialogHolder );
        } catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
}
