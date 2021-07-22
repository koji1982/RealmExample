package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.Document;
import com.honestastrology.realmexample.MainActivity;
import com.honestastrology.realmexample.UIRequestCommand;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.List;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
public class BasicOperatorInstrumentTest {
    
    private DBOperator _dbOperator;
    private boolean    _isInitialized = false;
    
//    @Rule
//    public ActivityScenarioRule<MainActivity> _scenarioRule
//            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Before
    public void setup(){
        Context context = InstrumentationRegistry
                                  .getInstrumentation()
                                  .getTargetContext();
        _dbOperator = DBOperator.getInMemoryInstance(
                context, Persistence.TEMPORARY );
    }
    
    @After
    public void close(){
        _dbOperator.closeAll();
    }
    
    //通常は!isNull
    //NullDBOperatorではisNullとなる
    //またSyncの接続が確立されていない場合もisNullとなる
    @Test
    public void isNullFalseNormally(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//    
//        });
        assertFalse( _dbOperator.isNull() );
    }
    
    @Test
    public void getMaxPrimaryNumberFirstReturnsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//            
//        });
        assertNull( _dbOperator.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void getMaxPrimaryNumberGain(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        createTestDocument( _dbOperator );
        //テスト対象
        int firstId = _dbOperator.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY).intValue();
        createTestDocument( _dbOperator );
        //テスト対象
        int secondId = _dbOperator.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY).intValue();
        //テスト対象
        createTestDocument( _dbOperator );
        int thirdId = _dbOperator.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY ).intValue();
    
        assertEquals( Document.INIT_ID, firstId  );
        assertEquals( firstId  + 1,     secondId );
        assertEquals( secondId + 1,     thirdId  );
    }
    
    @Test
    public void createArgNullThrowsIllegalArg(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//            
//        });
        assertThrows( IllegalArgumentException.class,
                () -> _dbOperator.create( null ) );
    }
    
    @Test
    public void createDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        Document document = new Document();
    
        //テスト対象
        _dbOperator.create( document );
    
        Document documentFromDB = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId());
        assertEquals( document.getId(), documentFromDB.getId() );
    }
    
    @Test
    public void getRealmObjectWithId(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        Document createdDocument = createTestDocument( _dbOperator );
    
        //テスト対象
        Document documentInDB = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, createdDocument.getId());
    
        assertEquals( createdDocument.getId(), documentInDB.getId() );
    }
    
    @Test
    public void readAllNullThrowsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//            
//        });
        assertThrows( NullPointerException.class,
                () -> _dbOperator.readAll( null ));
    }
    
    @Test
    public void readAllDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        List<Document> createdList = setupMultipleDocuments(_dbOperator);
    
        //テスト対象
        Iterator<Document> iteratorFromDB = _dbOperator.readAll( Document.class );
    
        while( iteratorFromDB.hasNext() ){
            assertTrue( createdList.remove( iteratorFromDB.next() ));
        }
        assertTrue( createdList.isEmpty() );
    }
    
    @Test
    public void updateNullThrowsIllegalArg(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//            
//        });
        assertThrows( IllegalArgumentException.class,
                ()-> _dbOperator.update( null ));
    }
    
    @Test
    public void updateDocument(){
//        _scenarioRule.getScenario().onActivity( activity ->{
//            setupField( activity );
//        });
        Document document = createUnmanagedDocument( _dbOperator );
        String updatedTitle = "updated title";
        String updatedBody  = "updated body";
        document.updateTitle( updatedTitle );
        document.updateText( updatedBody );
        Document preUpdateDocument = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId() );
        assertEquals( document.getId(), preUpdateDocument.getId() );
        assertNotEquals( document.getTitle(), preUpdateDocument.getTitle() );
        assertNotEquals( document.getText(), preUpdateDocument.getText() );
    
        //テスト対象
        _dbOperator.update( document );
    
        Document postUpdateDocument = _dbOperator.getRealmObject(
                Document.class,Document.PRIMARY_KEY, document.getId() );
        assertEquals( document.getId(), postUpdateDocument.getId() );
        assertEquals( document.getTitle(), postUpdateDocument.getTitle() );
        assertEquals( document.getText(), postUpdateDocument.getText() );
    }
    
    @Test
    public void deleteNullThrowsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//            
//        });
        assertThrows( NullPointerException.class,
                () -> _dbOperator.delete( null ));
    }
    
    @Test
    public void deleteDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        Document targetDocument = createTestDocument( _dbOperator );
        int      targetId       = targetDocument.getId();
        Document preTargetFromDB = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, targetId );
        assertEquals( targetId, preTargetFromDB.getId() );
    
        //テスト対象
        _dbOperator.delete( targetDocument );
    
        Document postTargetFromDB = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, targetId );
        assertNull( postTargetFromDB );
    }
    
    //ヘルパー関数
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _dbOperator = swapInMemoryOperator( activity );
        _isInitialized = true;
    }
}
