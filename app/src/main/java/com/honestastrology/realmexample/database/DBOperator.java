package com.honestastrology.realmexample.database;

import android.content.Context;

import java.util.Iterator;

import io.realm.RealmObject;

public interface DBOperator {
    
    public static DBOperator getNullInstance(){
        return NullDBOperator.getInstance();
    }
    
    public static DBOperator getInMemoryInstance(Context     context,
                                                 Persistence persistence){
        //SyncPreparedCallbackが指定されていない場合は、処理を行わないオブジェクトを渡す
        return new BasicOperator(context, persistence);
    }
    
//    public static DBOperator getInMemorySyncInstance(Context context,
//                                                     String syncId,
//                                                     DBErrorCallback errorCallback,
//                                                     SyncConnectedCallback syncCallback){
//        return new BasicOperator(context, syncId, errorCallback, syncCallback);
//    }
    
    public static DBOperator createBasicOperator(Context         context,
                                                 String          asyncFileName,
                                                 String          syncId,
                                                 DBErrorCallback errorCallback){
        return new BasicOperator(context, asyncFileName, syncId, errorCallback);
    }
    
    public boolean isNull();
    
    public ConnectType getCurrentConnect();
    
    public void toSync();
    
    public void toAsync();
    
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryFieldName);
    
    //新しいRealmObjectを作る場合は一意の@PrimaryKeyを保持していなければならない
    public void create(RealmObject realmObject);
    
    public <E extends RealmObject> E getRealmObject(Class<E> clazz, String fieldName, int id); 
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    public void update(RealmObject realmObject);
    
    public void delete(RealmObject realmObject);
    
    public void closeAll();
    
    public interface SyncConnectedCallback extends Runnable {
        
        public void run();
        
    }
    
}
