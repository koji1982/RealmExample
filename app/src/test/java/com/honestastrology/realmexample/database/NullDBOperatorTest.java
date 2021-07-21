package com.honestastrology.realmexample.database;

import com.honestastrology.realmexample.Document;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class NullDBOperatorTest {
    
    private DBOperator _nullDBOperator;
    
    @Before
    public void setup(){
        _nullDBOperator = DBOperator.getNullInstance();
    }
    
    @Test
    public void isNullTrue(){
        assertTrue( _nullDBOperator.isNull() );
    }
    
    @Test
    public void getCurrentConnectNullType(){
        assertEquals( RealmConnectType.NULL, _nullDBOperator.getCurrentConnect() );
    }
    
    @Test
    public void getMaxPrimaryNumberReturnsNull(){
        assertNull( _nullDBOperator.getMaxPrimaryNumber( 
                        Document.class, Document.PRIMARY_KEY ));
    }
    
    @Test
    public void createNotCreateAnyDocument(){
        _nullDBOperator.create( new Document(Document.INIT_ID) );
        assertNull( _nullDBOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, Document.INIT_ID));
    }
    
    @Test
    public void getRealmObjectReturnsNull(){
        assertNull( _nullDBOperator.getRealmObject(
                        Document.class, Document.PRIMARY_KEY, Document.INIT_ID ));
    }
    
    @Test
    public void readAllReturnsEmptyIterator(){
        Iterator<Document> returnedIterator = _nullDBOperator.readAll( Document.class );
        assertFalse( returnedIterator.hasNext() );
    }
    
    @Test
    public void updateDoNothing(){
        Document document = new Document( Document.INIT_ID );
        String updateTitle = "update_title";
        String updateBody  = "update_body";
        document.updateTitle( updateTitle );
        document.updateText( updateBody );
        
        _nullDBOperator.update( document );
        
        assertNull( _nullDBOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId() ));
    }
    
    @Test
    public void deleteDoNothing(){
        Document document = new Document( Document.INIT_ID );
        assertNull( _nullDBOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId() ));
        
        _nullDBOperator.delete( document );
    
        assertNull( _nullDBOperator.getRealmObject(
                Document.class, Document.PRIMARY_KEY, document.getId() ));
    }
}
