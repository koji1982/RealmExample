package com.honestastrology.realmexample;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DocumentInstrumentTest {
    
    private DBOperator _dbOperator;
    private boolean    _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void updateTitleManagedArgNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document document = createTestDocument( _dbOperator );
            
            //テスト対象
            document.updateTitle( null );
            
            //更新したDocumentにもDB内のDocumentにもnullが入っていることを確認
            assertNull( document.getTitle() );
            Document fromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, document.getId());
            assertNull( fromDB.getTitle() );
        });
    }
    
    @Test
    public void updateTitleManaged(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document targetDocument = createTestDocument( _dbOperator );
            int targetId = targetDocument.getId();
            assertEquals( Document.EMPTY_STRING, targetDocument.getTitle() );
            String updateString = "update string";
            
            //テスト対象
            targetDocument.updateTitle( updateString );
            
            //メソッドを実行したDocumentとDB内のDocumentの両方が更新されていることを確認
            assertEquals( updateString, targetDocument.getTitle() );
            Document targetFromDB = _dbOperator.getRealmObject(
                                Document.class, Document.PRIMARY_KEY, targetId);
            assertEquals( updateString, targetFromDB.getTitle() );
        });
    }
    
    @Test
    public void updateTitleUnmanagedArgNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document document = createUnmanagedDocument( _dbOperator );
        
            //テスト対象
            document.updateTitle( null );
        
            //更新したDocumentにnullが入っていることを確認
            assertNull( document.getTitle() );
            //DB内のDocumentは更新されていないことを確認
            Document fromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, document.getId());
            assertEquals( Document.EMPTY_STRING, fromDB.getTitle() );
        });
    }
    
    @Test
    public void updateTitleUnmanaged(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document targetDocument = createUnmanagedDocument( _dbOperator );
            int targetId = targetDocument.getId();
            assertEquals( Document.EMPTY_STRING, targetDocument.getTitle() );
            String updateString = "update string";
        
            //テスト対象
            targetDocument.updateTitle( updateString );
        
            //メソッドを実行したDocumentだけが更新されていて
            //DB内のDocumentが更新されていないことを確認
            assertEquals( updateString, targetDocument.getTitle() );
            Document targetFromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, targetId);
            assertNotEquals( updateString, targetFromDB.getTitle() );
        });
    }
    
    @Test
    public void updateTextManagedArgNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document document = createTestDocument( _dbOperator );
        
            //テスト対象
            document.updateText( null );
        
            //更新したDocumentにもDB内のDocumentにもnullが入っていることを確認
            assertNull( document.getText() );
            Document fromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, document.getId());
            assertNull( fromDB.getText() );
        });
    }
    
    @Test
    public void updateTextManaged(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document targetDocument = createTestDocument( _dbOperator );
            int targetId = targetDocument.getId();
            assertEquals( Document.EMPTY_STRING, targetDocument.getText() );
            String updateString = "update string";
        
            //テスト対象
            targetDocument.updateText( updateString );
        
            //メソッドを実行したDocumentとDB内のDocumentの両方が更新されていることを確認
            assertEquals( updateString, targetDocument.getText() );
            Document targetFromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, targetId);
            assertEquals( updateString, targetFromDB.getText() );
        });
    }
    
    @Test
    public void updateTextUnmanagedArgNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document document = createUnmanagedDocument( _dbOperator );
        
            //テスト対象
            document.updateText( null );
            
            //更新したDocumentにnullが入っていることを確認
            assertNull( document.getText() );
            //DB内のDocumentは更新されていないことを確認
            Document fromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, document.getId() );
            assertEquals( Document.EMPTY_STRING, fromDB.getText() );
        });
    }
    
    @Test
    public void updateTextUnmanaged(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document targetDocument = createUnmanagedDocument( _dbOperator );
            int targetId = targetDocument.getId();
            assertEquals( Document.EMPTY_STRING, targetDocument.getText() );
            String updateString = "update string";
        
            //テスト対象
            targetDocument.updateText( updateString );
        
            //メソッドを実行したDocumentだけが更新されていて
            //DB内のDocumentが更新されていないことを確認
            assertEquals( updateString, targetDocument.getText() );
            Document targetFromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, targetId);
            assertNotEquals( updateString, targetFromDB.getText() );
        });
    }
    
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _dbOperator = swapInMemoryOperator( activity );
        _isInitialized = true;
    }
}
