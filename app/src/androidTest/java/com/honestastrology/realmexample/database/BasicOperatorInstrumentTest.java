package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static org.junit.Assert.*;

public class BasicOperatorInstrumentTest {
    
    private DBOperator _dbOperator;
    private boolean    _isInitialized = false;
    
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
        assertFalse( _dbOperator.isNull() );
    }
    
    @Test
    public void getMaxPrimaryNumberFirstReturnsNull(){
        assertNull( _dbOperator.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void getMaxPrimaryNumberGain(){
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
        assertThrows( IllegalArgumentException.class,
                () -> _dbOperator.create( null ) );
    }
    
    @Test
    public void createDocument(){
        Document document = new Document();
    
        //テスト対象
        _dbOperator.create( document );
    
        Document documentFromDB = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId());
        assertEquals( document.getId(), documentFromDB.getId() );
    }
    
    @Test
    public void getRealmObjectWithId(){
        Document createdDocument = createTestDocument( _dbOperator );
    
        //テスト対象
        Document documentInDB = _dbOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, createdDocument.getId());
    
        assertEquals( createdDocument.getId(), documentInDB.getId() );
    }
    
    @Test
    public void readAllNullThrowsNull(){
        assertThrows( NullPointerException.class,
                () -> _dbOperator.readAll( null ));
    }
    
    @Test
    public void readAllDocument(){
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
        assertThrows( IllegalArgumentException.class,
                ()-> _dbOperator.update( null ));
    }
    
    @Test
    public void updateDocument(){
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
        assertThrows( NullPointerException.class,
                () -> _dbOperator.delete( null ));
    }
    
    @Test
    public void deleteDocument(){
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
}
