package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {
    
    public static DBOperator swapInMemoryOperator(MainActivity activity){
        DBOperator dbOperator = DBOperator.getInMemoryInstance( activity, activity );
        try {
            //dbOperatorをinMemoryに入れ替える
            Field field = activity.getClass().getDeclaredField("_dbOperator");
            field.setAccessible( true );
            field.set(activity, dbOperator );
        } catch (Exception e){
        }
        return dbOperator;
    }
    
    static EditPage extractEditPage(MainActivity activity){
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
            Method getNewIdMethod = RequestCommand.class
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
}
