package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.ui.control.RequestCommand;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

import java.util.Iterator;

/**
 * 
 */

public enum UIRequestCommand implements RequestCommand<Document> {
    /**
     * Documentを新しく作成し、編集画面に切り替える
     * 編集する際にRealmObjectとして登録されていなければならないため
     * 画面を切り替える前に、生成したDocumentをデータベースに渡している
     * */
    CREATE  {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            
            if( dbOperator.isNull() ) return;
            
            int newId = getNewId( dbOperator );
            Document newDocument = new Document( newId );
            
            dbOperator.create( newDocument );
            Document managedDocument = dbOperator.getRealmObject(
                            Document.class, Document.PRIMARY_KEY, newId);
            
            viewer.show( managedDocument );
        }
    },
    /**
     * データベースから全てのDocumentを取得し表示するコマンド
     * */
    READ   {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            Iterator<Document> documentIterator = null;
            if( !dbOperator.isNull() ) {
                documentIterator = dbOperator.readAll( Document.class ) ;
            }
            
            viewer.showList( documentIterator );
            viewer.displayConnectString( dbOperator.getCurrentConnect() );
        }
    },
    SWITCH_CONNECT {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            ConnectType connectType = dbOperator.getCurrentConnect();
            //切り替えメソッド
            connectType.switches( dbOperator );
            //切り替え後のデータを表示する
            READ.execute( viewer, dbOperator );
        }
    };
    
    private static int getNewId(DBOperator dbOperator){
        Number maxPrimaryNumber = dbOperator.getMaxPrimaryNumber(
                                    Document.class, Document.PRIMARY_KEY );
        if( maxPrimaryNumber == null ){
            return Document.INIT_ID;
        }
        return maxPrimaryNumber.intValue() + Document.NEXT_ID_STRIDE;
    }
}
