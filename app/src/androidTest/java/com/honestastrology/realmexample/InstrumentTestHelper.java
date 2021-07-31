package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.database.Persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InstrumentTestHelper {
    
    public static DBOperator swapInMemoryOperator(MainActivity activity){
        try {
            Field field_operator = activity.getClass().getDeclaredField("_dbOperator");
            field_operator.setAccessible( true );
            DBOperator dbOperator = (DBOperator)field_operator.get(activity);
            dbOperator.closeAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        DBOperator inMemoryOperator = DBOperator.getInMemoryInstance(
                                                activity, Persistence.TEMPORARY);
        try {
            //dbOperatorをinMemoryに入れ替える
            Field field = activity.getClass().getDeclaredField("_dbOperator");
            field.setAccessible( true );
            field.set(activity, inMemoryOperator );
        } catch (Exception e){
        }
        return inMemoryOperator;
    }
    
    public static EditPage extractEditPage(MainActivity activity){
        try {
            //MainActivityからEditPageを取り出す
            Field field_viewer = activity.getClass().getDeclaredField("_viewer");
            field_viewer.setAccessible( true );
            DocumentViewer viewer = (DocumentViewer)field_viewer.get( activity );
            Field field_editPage = viewer.getClass().getDeclaredField("_editPage");
            field_editPage.setAccessible( true );
            return (EditPage)field_editPage.get(viewer);
        } catch (Exception e){
        }
        return null;
    }
    
    public static Document createTestDocument(DBOperator dbOperator){
        int newId = 0;
        try {
            Method getNewIdMethod = UIRequestCommand.class
                                            .getDeclaredMethod("getNewId", DBOperator.class);
            getNewIdMethod.setAccessible( true );
            newId = (int)getNewIdMethod.invoke(null, dbOperator);
        } catch (Exception e){
        }
        Document document = new Document( newId );
        dbOperator.create( document );
        return dbOperator.getRealmObject(
                Document.class,
                Document.PRIMARY_KEY,
                newId );
    }
    
    public static Document createUnmanagedDocument(DBOperator dbOperator){
        Document document = createTestDocument( dbOperator );
        return document.getRealm().copyFromRealm( document );
    }
    
    public static List<Document> setupMultipleDocuments(DBOperator dbOperator){
        List<Document> idList = new ArrayList<>();
        for( int i=0; i<5; i++){
            Document createdDocument = createTestDocument(dbOperator);
            idList.add( createdDocument );
        }
        return idList;
    }
}
