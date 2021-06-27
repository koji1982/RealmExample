package com.honestastrology.realmexample;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class EditPageInstTest {
    //Activity関連のInstrumentedTestは基本的に、
    //ActivityScenarioのonActivity()のパラメータ
    //ActivityAction.perform()メソッドのスコープ内で
    //セットアップ、テストメソッドの実行、アサーションを行う
    //参照
    //https://developer.android.com/guide/components/activities/testing
    //https://developer.android.com/reference/androidx/test/core/app/ActivityScenario.ActivityAction
    private MainActivity _mainActivity;
    private EditPage     _editPage;
    private DBOperator   _dbOperator;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
                            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void showDocumentChangeTextTest(){
        _scenarioRule.getScenario().onActivity(activity -> {
            setupField( activity );
            //確認用テキストとDocumentインスタンスの用意
            String titleTestText = "title test text";
            String bodyTestText  = "body test text";
            Document document = createTestDocument();
            //テスト対象メソッド、比較のため最初に空文字のDocumentを引数として渡す
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
    
    @Test
    public void setDocumentBodyTextTest(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //確認用テキストとDocumentインスタンスの用意
            String displayTestText = "display test text";
            Document document = createTestDocument();
            document.updateText( displayTestText );
            //テスト対象メソッド
            _editPage.showDocument( document );
            
            TextView actualBody = activity.getParts( PartsDefine.BODY_TEXT );
            assertEquals( displayTestText, actualBody.getText().toString() );
        });
    }
    
    //ヘルパー関数
    private void setupField(MainActivity activity){
        _dbOperator = DBOperator.getInMemoryInstance( activity, activity );
        try {
            Field field = activity.getClass().getDeclaredField("_dbOperator");
            field.setAccessible( true );
            field.set(activity, _dbOperator );
        } catch (Exception e){
        }
        Viewer<Document> viewer = new DocumentViewer( activity );
        _editPage = new EditPage(activity, viewer);
    }
    
    //ヘルパー関数
    private Document createTestDocument(){
        Document document = new Document( 0 );
        _dbOperator.create( document );
        return _dbOperator.getRealmObject(
                                Document.class,
                                Document.PRIMARY_KEY,
                                0);
    }
}
