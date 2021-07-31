package com.honestastrology.realmexample;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static com.honestastrology.realmexample.DBSendCommand.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DBSendCommandInstrumentTest {
    
    private DBOperator _dbOperator;
    private boolean    _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void executeUpdateNullThrowsNullIllegal(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            assertThrows( NullPointerException.class,
                    () -> UPDATE.execute( null, new Document() ) );
            assertThrows( IllegalArgumentException.class,
                    () -> UPDATE.execute( _dbOperator, null) );
            assertThrows( NullPointerException.class,
                    () -> UPDATE.execute( null, null ) );
        });
    }
    
    @Test
    public void executeUpdateNullOperatorNotUpdate(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document unmanagedDocument = createUnmanagedDocument( _dbOperator );
            int    id       = unmanagedDocument.getId();
            String updatedTitle = "updated title";
            String updatedBody  = "updated text";
            unmanagedDocument.updateTitle( updatedTitle );
            unmanagedDocument.updateText( updatedBody );
            
            //テスト対象
            UPDATE.execute( DBOperator.getNullInstance(), unmanagedDocument );
            
            //idで取り出してDBの中のオブジェクトには更新が反映されていないことを確認
            Document sameDocumentFromDB = _dbOperator.getRealmObject(
                                                Document.class, Document.PRIMARY_KEY, id );
            assertNotEquals( updatedTitle, sameDocumentFromDB.getTitle() );
            assertNotEquals( updatedBody,  sameDocumentFromDB.getText() );
        });
    }
    
    @Test
    public void executeUpdate(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document unmanagedDocument = createUnmanagedDocument( _dbOperator );
            int    id       = unmanagedDocument.getId();
            String updatedTitle = "updated title";
            String updatedBody  = "updated text";
            unmanagedDocument.updateTitle( updatedTitle );
            unmanagedDocument.updateText( updatedBody );
        
            //テスト対象
            UPDATE.execute( _dbOperator, unmanagedDocument );
        
            //idで取り出してDBの中のオブジェクトに更新が反映されていることを確認
            Document sameDocumentFromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, id );
            assertEquals( updatedTitle, sameDocumentFromDB.getTitle() );
            assertEquals( updatedBody,  sameDocumentFromDB.getText() );
        });
    }
    
    @Test
    public void executeDeleteOperatorArgNullNotDelete(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            setupMultipleDocuments( _dbOperator );
            
            Iterator<Document> preIterator = _dbOperator.readAll( Document.class );
            List<Integer>      idList      = new ArrayList<>();
            while( preIterator.hasNext() ){
                idList.add( preIterator.next().getId() );
            }
            Document sampleDocument = _dbOperator.getRealmObject(
                                    Document.class, Document.PRIMARY_KEY, idList.get( 0 ) );
            
            //テスト対象
            assertThrows( NullPointerException.class,
                    () -> DELETE.execute( null, sampleDocument ));
            
            //Documentの数が減っていないこと、IDが同じであることを確認
            Iterator<Document> postIterator = _dbOperator.readAll( Document.class );
            for( int i=0; i<idList.size(); i++){
                assertEquals( ((int)idList.get( i )), postIterator.next().getId() );
            }
        });
    }
    
    @Test
    public void executeDeleteDocumentArgNullNotDelete(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            setupMultipleDocuments( _dbOperator );
        
            Iterator<Document> preIterator = _dbOperator.readAll( Document.class );
            List<Integer>      idList      = new ArrayList<>();
            while( preIterator.hasNext() ){
                idList.add( preIterator.next().getId() );
            }
            
            //テスト対象
            assertThrows( NullPointerException.class,
                    () -> DELETE.execute( _dbOperator, null ));
            
            //Documentの数が減っていないこと、IDが同じであることを確認
            Iterator<Document> postIterator = _dbOperator.readAll( Document.class );
            for( int i=0; i<idList.size(); i++){
                assertEquals( ((int)idList.get( i )), postIterator.next().getId() );
            }
        });
    }
    
    @Test
    public void executeDeleteArgNullNullNotDelete(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            setupMultipleDocuments( _dbOperator );
        
            Iterator<Document> preIterator = _dbOperator.readAll( Document.class );
            List<Integer>      idList      = new ArrayList<>();
            while( preIterator.hasNext() ){
                idList.add( preIterator.next().getId() );
            }
        
            //テスト対象
            assertThrows( NullPointerException.class,
                    () -> DELETE.execute( null, null ));
        
            //Documentの数が減っていないこと、IDが同じであることを確認
            Iterator<Document> postIterator = _dbOperator.readAll( Document.class );
            for( int i=0; i<idList.size(); i++){
                assertEquals( ((int)idList.get( i )), postIterator.next().getId() );
            }
        });
    }
    
    @Test
    public void executeDelete(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document deleteTarget = createTestDocument( _dbOperator );
            int targetId = deleteTarget.getId();
            //削除対象のDocumentがDB内にあることを確認
            Document targetFromDB = _dbOperator.getRealmObject(
                                Document.class, Document.PRIMARY_KEY, targetId );
            assertEquals( deleteTarget.getId(), targetFromDB.getId() );
            
            //テスト対象
            DELETE.execute( _dbOperator, deleteTarget );
            
            //Documentが削除されていることを確認
            Document targetAfterDelete = _dbOperator.getRealmObject(
                                Document.class, Document.PRIMARY_KEY, targetId );
            assertNull( targetAfterDelete );
        });
    }
    
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _dbOperator = swapInMemoryOperator( activity );
        _isInitialized = true;
    }
}
