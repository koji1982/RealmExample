package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.ui.control.ReceiveCommand;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

import java.util.Iterator;

/**
 * 
 */

public enum DocumentUICommand implements ReceiveCommand<Document> {
    /**
     * Documentを新しく作成し、編集画面に切り替える
     * 編集する際にRealmObjectとして登録されていなければならないため
     * 画面を切り替える前に、生成したDocumentをデータベースに渡している
     * */
    CREATE    ( R.id.create_button ) {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            
            if( dbOperator.isNull() ) return;
            
            int newId = getNewId( dbOperator );
            Document newDocument = new Document( newId );
            
            dbOperator.create( newDocument );
            Document managedDocument = dbOperator.getRealmObject(
                            Document.class, Document.PRIMARY_KEY, newId);
            
            viewer.show( managedDocument );
//            viewer.setSelectedContent( createdDocument );
//            viewer.transitViewPage( LayoutDefine.EDITOR );
        }
    },
    /**
     * データベースから全てのDocumentを取得し表示するコマンド
     * */
    READ      ( R.integer.reload_operation ) {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            Iterator<Document> documentIterator = null;
            if( !dbOperator.isNull() ) {
                documentIterator = dbOperator.readAll( Document.class ) ;
            }
            
            viewer.show( documentIterator );
            viewer.setConnectString( dbOperator.getCurrentConnect() );
        }
    },
    SWITCH_CONNECT( R.id.sync_async_button ){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            ConnectType connectType = dbOperator.getCurrentConnect();
            //切り替えメソッド
            connectType.switches( dbOperator );
            //切り替え後のデータを表示する
            READ.execute( viewer, dbOperator );
        }
    },
    BACK_FROM_EDIT( R.id.back_from_edit ){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
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
    
    @Override
    public int getUIId(){
        return _UIId;
    }
    
    private final int _UIId;
    
    DocumentUICommand(int UIId){
        this._UIId = UIId;
    }
    
}