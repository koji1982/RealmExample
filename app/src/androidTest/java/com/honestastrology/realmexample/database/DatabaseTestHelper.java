package com.honestastrology.realmexample.database;

import android.content.Context;

import com.honestastrology.realmexample.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTestHelper {
    
    public static RealmAccessor extractAsyncAccessor(Context context){
        BasicOperator operator = (BasicOperator)DBOperator.getInMemoryInstance(
                                            context, Persistence.TEMPORARY);
        try {
            Field field_accessor = operator.getClass().getDeclaredField("_asyncDB");
            field_accessor.setAccessible( true );
            return (RealmAccessor)field_accessor.get(operator);
        } catch (Exception e){
            
        }
        return null;
    }
    
    public static RealmAccessor extractSyncAccessor(Context context){
//        try{
//            Field field = activity.getClass().getDeclaredField("_dbOperator");
//            field.setAccessible( true );
//            DBOperator operator = (DBOperator)field.get(activity);
//            operator.closeAll();
//        } catch (Exception e){
//            
//        }
        DBOperator operator = DBOperator.getInMemoryInstance(
                                            context, Persistence.TEMPORARY);
        try {
            Field field_accessor = operator.getClass().getDeclaredField("_syncDB");
            field_accessor.setAccessible( true );
            return (RealmAccessor)field_accessor.get(operator);
        } catch (Exception e){
        
        }
        return null;
    }
    
    public static Document createTestDocument(RealmAccessor accessor){
        int newId = 0;
        Number maxPrimaryNumber = accessor.getMaxPrimaryNumber(
                Document.class, Document.PRIMARY_KEY );
        if( maxPrimaryNumber == null ){
            newId = Document.INIT_ID;
        } else {
            newId = maxPrimaryNumber.intValue() + Document.NEXT_ID_STRIDE;
        }
        
        Document document = new Document( newId );
        accessor.create( document );
        return accessor.getRealmObject(
                Document.class,
                Document.PRIMARY_KEY,
                newId );
    }
    
    public static Document createUnmanagedDocument(RealmAccessor accessor){
        Document document = createTestDocument( accessor );
        return document.getRealm().copyFromRealm( document );
    }
    
    public static List<Document> setupMultipleDocuments(RealmAccessor accessor){
        List<Document> idList = new ArrayList<>();
        for( int i=0; i<5; i++){
            Document createdDocument = createTestDocument( accessor );
            idList.add( createdDocument );
        }
        return idList;
    }
    
}
