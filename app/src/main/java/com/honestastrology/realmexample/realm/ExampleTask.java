package com.honestastrology.realmexample.realm;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ExampleTask implements Runnable {
    
    @Override
    public void run() {
        
        String realmName = "My Project";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
        Realm backgroundThreadRealm = Realm.getInstance(config);
        
        Document document = new Document(1);
        backgroundThreadRealm.executeTransaction (
                transactionRealm -> transactionRealm.insert(document) );
        // all results in the realm
        RealmResults<Document> results = backgroundThreadRealm.where(Document.class).findAll();
        
        // you can also filter a collection
        //Nで始まるresultsを取得
        RealmResults<Document> TasksThatBeginWithN = results.where().beginsWith("name", "N").findAll();
        //statusがOpenのresultsを取得
//        RealmResults<Document> openTasks = results.where().equalTo("status", TaskStatus.Open.name()).findAll();
        //resultの先頭を取得
        Document otherTask = results.get(0);
        // all modifications to a realm must happen inside of a write block
        //トランザクションをラムダで記述して実行
//        backgroundThreadRealm.executeTransaction( transactionRealm -> {
//            Document innerOtherTask = transactionRealm.where(Document.class).equalTo("_id", otherTask.getTitle()).findFirst();
//            innerOtherTask.setStatus(TaskStatus.Complete);
//        });
        
        //次のresultsを取得してトランザクションを実行
        Document yetAnotherDocument = results.get(0);
        String yetAnotherTaskName = yetAnotherDocument.getTitle();
        // all modifications to a realm must happen inside of a write block
        backgroundThreadRealm.executeTransaction( transactionRealm -> {
            Document innerYetAnotherTask = transactionRealm
                                                   .where(Document.class)
                                                   .equalTo("_id", yetAnotherTaskName)
                                                   .findFirst();
            innerYetAnotherTask.deleteFromRealm();
        });
        
        // because this background thread uses synchronous realm transactions, at this point all
        // transactions have completed and we can safely close the realm
        backgroundThreadRealm.close();
    }
    
}
