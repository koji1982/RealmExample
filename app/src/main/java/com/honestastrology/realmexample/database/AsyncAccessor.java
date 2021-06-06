package com.honestastrology.realmexample.database;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;

import java.util.Iterator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

class AsyncAccessor implements DBAccessor {
    
//    private static final String ASYNC_FILE_NAME = "realm_example_async";
    private Realm _asyncRealm;
    
    AsyncAccessor(String asyncFileName){
        RealmConfiguration config = new RealmConfiguration.Builder()
                                            .name( asyncFileName )
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
        _asyncRealm = Realm.getInstance( config );
    }
    
    @Override
    public boolean isNull(){
        return ( _asyncRealm == null );
    }
    
    @Override
    public ConnectType getConnectType(){
        return RealmConnectType.ASYNC;
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _asyncRealm.where( clazz ).max( primaryKeyField );
    }
    
    @Override
    public void create(RealmObject realmObject){
        _asyncRealm.executeTransaction (
                realmTransaction -> realmTransaction.insert( realmObject ) );
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz,
                                                    String   fieldName,
                                                    int      id){
        return _asyncRealm.where( clazz ).equalTo( fieldName, id ).findFirst();
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _asyncRealm.where( clazz ).findAll().iterator();
    }
    
    @Override
    public void update(RealmObject realmObject){
//        _asyncRealm.executeTransaction(
//                realmTransaction -> realmTransaction.copyToRealmOrUpdate( realmObject ));
        _asyncRealm.executeTransaction(
                realmTransaction -> realmTransaction.insertOrUpdate( realmObject) );
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _asyncRealm.executeTransaction(
                realmTransaction -> realmObject.deleteFromRealm() );
    }
    
    @Override
    public void close(){
        _asyncRealm.close();
    }
    
//    private void addChangeListenerToRealm(Realm realm) {
//        // all Tasks in the realm
//        RealmResults<Document> Tasks = _asyncRealm.where(Document.class).findAllAsync();
//        Tasks.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Document>>() {
//            @Override
//            public void onChange(RealmResults<Document> collection, OrderedCollectionChangeSet changeSet) {
//                // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
//                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
//                for (OrderedCollectionChangeSet.Range range : deletions) {
//                    Log.v("QUICKSTART", "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
//                }
//                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
//                for (OrderedCollectionChangeSet.Range range : insertions) {
//                    Log.v("QUICKSTART", "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
//                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
//                for (OrderedCollectionChangeSet.Range range : modifications) {
//                    Log.v("QUICKSTART", "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
//            }
//        });
//    }
    
}
