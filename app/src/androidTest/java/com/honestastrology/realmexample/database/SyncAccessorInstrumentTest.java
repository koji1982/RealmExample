package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.realm.Realm;

import static com.honestastrology.realmexample.database.DatabaseTestHelper.*;
import static org.junit.Assert.*;

public class SyncAccessorInstrumentTest {
    
    private RealmAccessor _syncAccessor;
    private boolean        _isInitialized = false;
    
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
        assertFalse( _syncAccessor.isNull() );
    }
    
    @Test
    public void getMaxPrimaryNumberFirstReturnsNull(){
            assertNull( _syncAccessor.getMaxPrimaryNumber(
                    Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void getMaxPrimaryNumberGain(){
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
        assertThrows( IllegalArgumentException.class,
                () -> _syncAccessor.create( null ) );
    }
    
    @Test
    public void createDocument(){
        Document document = new Document();
    
        //テスト対象
        _syncAccessor.create( document );
    
        Document documentFromDB = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId());
        assertEquals( document.getId(), documentFromDB.getId() );
    }
    
    @Test
    public void getRealmObjectWithId(){
        Document createdDocument = createTestDocument( _syncAccessor );
    
        //テスト対象
        Document documentInDB = _syncAccessor.getRealmObject(
                Document.class, Document.PRIMARY_KEY, createdDocument.getId());
    
        assertEquals( createdDocument.getId(), documentInDB.getId() );
    }
    
    @Test
    public void readAllNullThrowsNull(){
        assertThrows( NullPointerException.class,
                () -> _syncAccessor.readAll( null ));
    }
    
    @Test
    public void readAllDocument(){
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
        assertThrows( IllegalArgumentException.class,
                ()-> _syncAccessor.update( null ));
    }
    
    @Test
    public void updateDocument(){
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
        assertThrows( NullPointerException.class,
                () -> _syncAccessor.delete( null ));
    }
    
    @Test
    public void deleteDocument(){
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
    
}
