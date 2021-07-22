package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.honestastrology.realmexample.Document;
import com.honestastrology.realmexample.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.honestastrology.realmexample.database.DatabaseTestHelper.*;
import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
public class SyncAccessorInstrumentTest {
    
    private DBAccessor     _syncAccessor;
    private boolean        _isInitialized = false;
    private CountDownLatch _latch;
    
//    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule;
//            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Before
    public void setup(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Realm.init( context );
        _syncAccessor = new SyncAccessor( Persistence.TEMPORARY );
    }
    
    @After
    public void close(){
        _syncAccessor.close();
    }
    
    @Test
    public void isNullNormallyFalse() {
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            });
//        });
        assertFalse( _syncAccessor.isNull() );
    }
    
    @Test
    public void getMaxPrimaryNumberFirstReturnsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            });
            assertNull( _syncAccessor.getMaxPrimaryNumber(
                    Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void getMaxPrimaryNumberGain(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            });
//        });
        _syncAccessor.create( new Document() );
        //テスト対象
        int firstId = _syncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY).intValue();
    
        _syncAccessor.create( new Document( firstId + 1 ) );
        //テスト対象
        int secondId = _syncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY).intValue();
    
        _syncAccessor.create( new Document( secondId + 1 ));
        //テスト対象
        int thirdId = _syncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY ).intValue();
    
        assertEquals( Document.INIT_ID, firstId  );
        assertEquals( firstId  + 1,     secondId );
        assertEquals( secondId + 1,     thirdId  );
    }
    
    @Test
    public void createArgNullThrowsIllegalArg(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        assertThrows( IllegalArgumentException.class,
                () -> _syncAccessor.create( null ) );
    }
    
    @Test
    public void createDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        Document document = new Document();
    
        //テスト対象
        _syncAccessor.create( document );
    
        Document documentFromDB = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId());
        assertEquals( document.getId(), documentFromDB.getId() );
    }
    
    @Test
    public void getRealmObjectWithId(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        Document createdDocument = createTestDocument( _syncAccessor );
    
        //テスト対象
        Document documentInDB = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, createdDocument.getId());
    
        assertEquals( createdDocument.getId(), documentInDB.getId() );
    }
    
    @Test
    public void readAllNullThrowsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        assertThrows( NullPointerException.class,
                () -> _syncAccessor.readAll( null ));
    }
    
    @Test
    public void readAllDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//    
//        });
        List<Document> createdList = setupMultipleDocuments( _syncAccessor );
    
        //テスト対象
        Iterator<Document> iteratorFromDB = _syncAccessor.readAll( Document.class );
    
        while( iteratorFromDB.hasNext() ){
            assertTrue( createdList.remove( iteratorFromDB.next() ));
        }
        assertTrue( createdList.isEmpty() );
    }
    
    @Test
    public void updateNullThrowsIllegalArg(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        assertThrows( IllegalArgumentException.class,
                ()-> _syncAccessor.update( null ));
    }
    
    @Test
    public void updateDocument(){
//        _scenarioRule.getScenario().onActivity( activity ->{
//            setupField( activity, () -> {
//            } );
//        });
        Document document = createUnmanagedDocument( _syncAccessor );
        String updatedTitle = "updated title";
        String updatedBody  = "updated body";
        document.updateTitle( updatedTitle );
        document.updateText( updatedBody );
        Document preUpdateDocument = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId() );
        assertEquals( document.getId(), preUpdateDocument.getId() );
        assertNotEquals( document.getTitle(), preUpdateDocument.getTitle() );
        assertNotEquals( document.getText(), preUpdateDocument.getText() );
    
        //テスト対象
        _syncAccessor.update( document );
    
        Document postUpdateDocument = _syncAccessor.getRealmObject(
                Document.class,Document.PRIMARY_KEY, document.getId() );
        assertEquals( document.getId(), postUpdateDocument.getId() );
        assertEquals( document.getTitle(), postUpdateDocument.getTitle() );
        assertEquals( document.getText(), postUpdateDocument.getText() );
    }
    
    @Test
    public void deleteNullThrowsNull(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        assertThrows( NullPointerException.class,
                () -> _syncAccessor.delete( null ));
    }
    
    @Test
    public void deleteDocument(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            setupField( activity, () -> {
//            } );
//        });
        Document targetDocument = createTestDocument( _syncAccessor );
        int      targetId       = targetDocument.getId();
        Document preTargetFromDB = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, targetId );
        assertEquals( targetId, preTargetFromDB.getId() );
    
        //テスト対象
        _syncAccessor.delete( targetDocument );
    
        Document postTargetFromDB = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, targetId );
        assertNull( postTargetFromDB );
    }
    
    //ヘルパー関数
    private void setupField(MainActivity activity,
                            DBOperator.SyncConnectedCallback callback){
        _latch = new CountDownLatch( 1 ) ;
        if( !_isInitialized ) {
            _syncAccessor = extractSyncAccessor( activity );
            _isInitialized = true;
        } else {
            callback.run();
        }
    }
    
    private void awaitCallback(){
        try {
            _latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
