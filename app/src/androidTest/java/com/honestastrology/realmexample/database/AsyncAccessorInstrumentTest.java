package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

import static com.honestastrology.realmexample.database.DatabaseTestHelper.*;
import static org.junit.Assert.*;

public class AsyncAccessorInstrumentTest {
    
    private RealmAccessor _asyncAccessor;
    private boolean    _isInitialized = false;
    
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
        assertFalse( _asyncAccessor.isNull() );
    }
    
    @Test
    public void getMaxPrimaryNumberFirstReturnsNull(){
        //最初のDocument作成時の挙動確認のためsetupから開始する
        
        assertNull( _asyncAccessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void getMaxPrimaryNumberGain(){
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
        assertThrows( IllegalArgumentException.class,
                () -> _asyncAccessor.create( null ) );
    }
    
    @Test
    public void createDocument(){
        Document document = createTestDocument( _asyncAccessor );
        
        //テスト対象
        _asyncAccessor.create( document );
    
        Document documentFromDB = _asyncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId());
        assertEquals( document.getId(), documentFromDB.getId() );
    }
    
    @Test
    public void getRealmObjectWithId(){
        Document createdDocument = createTestDocument( _asyncAccessor );
    
        //テスト対象
        Document documentInDB = _asyncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, createdDocument.getId());
    
        assertEquals( createdDocument.getId(), documentInDB.getId() );
    }
    
    @Test
    public void readAllNullThrowsNull(){
        assertThrows( NullPointerException.class,
                () -> _asyncAccessor.readAll( null ));
    }
    
    @Test
    public void readAllDocument(){
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
        assertThrows( IllegalArgumentException.class,
                ()-> _asyncAccessor.update( null ));
    }
    
    @Test
    public void updateDocument(){
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
