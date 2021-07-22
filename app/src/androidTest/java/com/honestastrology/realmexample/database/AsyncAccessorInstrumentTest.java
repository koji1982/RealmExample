package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.Document;
import com.honestastrology.realmexample.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static com.honestastrology.realmexample.database.DatabaseTestHelper.*;
import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
public class AsyncAccessorInstrumentTest {
    
    private DBAccessor _asyncAccessor;
    private boolean    _isInitialized = false;
    
//    @Rule
//    public ActivityScenarioRule<MainActivity> _scenarioRule
//            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Before
    public void setup(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Realm.init( context );
        _asyncAccessor = new AsyncAccessor( Persistence.TEMPORARY );
    }
    
    @After
    public void close(){
        _asyncAccessor.close();
    }
    
    @Test
    public void isNullNormallyFalse(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//            
//            assertFalse( _asyncAccessor.isNull() );
//        });
        assertFalse( _asyncAccessor.isNull() );
    }
    
    @Test
    public void getMaxPrimaryNumberFirstReturnsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        
//        });
        //最初のDocument作成時の挙動確認のためsetupから開始する
        
        assertNull( _asyncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void getMaxPrimaryNumberGain(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        _asyncAccessor.create( new Document() );
        //テスト対象
        int firstId = _asyncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY).intValue();
    
        _asyncAccessor.create( new Document( firstId + 1 ) );
        //テスト対象
        int secondId = _asyncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY).intValue();
    
        _asyncAccessor.create( new Document( secondId + 1 ));
        //テスト対象
        int thirdId = _asyncAccessor.getMaxPrimaryNumber(
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
                () -> _asyncAccessor.create( null ) );
    }
    
    @Test
    public void createDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        Document document = createTestDocument( _asyncAccessor );
        
        //テスト対象
        _asyncAccessor.create( document );
    
        Document documentFromDB = _asyncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId());
        assertEquals( document.getId(), documentFromDB.getId() );
    }
    
    @Test
    public void getRealmObjectWithId(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        Document createdDocument = createTestDocument( _asyncAccessor );
    
        //テスト対象
        Document documentInDB = _asyncAccessor.getRealmObject(
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
                () -> _asyncAccessor.readAll( null ));
    }
    
    @Test
    public void readAllDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity );
//        });
        //作成されたオブジェクトとreadAllで取得するオブジェクト
        //を比較するため初期化から始める
//        setupField();
        List<Document> createdList       = setupMultipleDocuments( _asyncAccessor );
        List<Document> createdListSecond = setupMultipleDocuments( _asyncAccessor );
        List<Document> createdListThird  = setupMultipleDocuments( _asyncAccessor );
        createdList.addAll( createdListSecond );
        createdList.addAll( createdListThird );
    
        //テスト対象
        Iterator<Document> iteratorFromDB = _asyncAccessor.readAll( Document.class );
    
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
                ()-> _asyncAccessor.update( null ));
    }
    
    @Test
    public void updateDocument(){
//        _scenarioRule.getScenario().onActivity( activity ->{
//            setupField( activity );
//        });
        Document document = createUnmanagedDocument( _asyncAccessor );
        String updatedTitle = "updated title";
        String updatedBody  = "updated body";
        document.updateTitle( updatedTitle );
        document.updateText( updatedBody );
        Document preUpdateDocument = _asyncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId() );
        assertEquals( document.getId(), preUpdateDocument.getId() );
        assertNotEquals( document.getTitle(), preUpdateDocument.getTitle() );
        assertNotEquals( document.getText(), preUpdateDocument.getText() );
    
        //テスト対象
        _asyncAccessor.update( document );
    
        Document postUpdateDocument = _asyncAccessor.getRealmObject(
                Document.class,Document.PRIMARY_KEY, document.getId() );
        assertEquals( document.getId(), postUpdateDocument.getId() );
        assertEquals( document.getTitle(), postUpdateDocument.getTitle() );
        assertEquals( document.getText(), postUpdateDocument.getText() );
    }
    
    @Test
    public void deleteNullThrowsNull(){
        assertThrows( NullPointerException.class,
                () -> _asyncAccessor.delete( null ));
    }
    
    @Test
    public void deleteDocument(){
        Document targetDocument = createTestDocument( _asyncAccessor );
        int      targetId       = targetDocument.getId();
        Document preTargetFromDB = _asyncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, targetId );
        assertEquals( targetId, preTargetFromDB.getId() );
    
        //テスト対象
        _asyncAccessor.delete( targetDocument );
    
        Document postTargetFromDB = _asyncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, targetId );
        assertNull( postTargetFromDB );
    }
    
}
