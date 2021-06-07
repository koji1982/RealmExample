package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.ui.control.Command;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.Viewer;

/**
 * 
 */

public enum DocumentCommand implements Command<Document> {
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
            Document createdDocument = dbOperator.getRealmObject(
                            Document.class, Document.PRIMARY_KEY, newId);
            
            viewer.setSelectedContent( createdDocument );
            viewer.transitViewPage( LayoutDefine.EDITOR );
        }
    },
    /**
     * データベースから全てのDocumentを取得し表示するコマンド
     * */
    READ      ( R.integer.reload_operation ) {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            if( !dbOperator.isNull() ) {
                viewer.setContents( dbOperator.readAll(Document.class) );
            }
            viewer.transitViewPage( LayoutDefine.TITLE_LIST );
            viewer.updateConnectDisplay( dbOperator.getCurrentConnect() );
        }
    },
    UPDATE    ( R.id.update_button ){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            
            if( dbOperator.isNull() ) return;
            
            viewer.update();
            dbOperator.update( viewer.getSelectedContent() );
        }
    },
    DELETE    ( R.id.list_item ){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            
            if( dbOperator.isNull() ) return;
            
            dbOperator.delete( viewer.getSelectedContent() );
            
            READ.execute( viewer, dbOperator );
        }
    },
    SWITCH_CONNECT( R.id.sync_async_button ){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator dbOperator){
            ConnectType connectType = dbOperator.getCurrentConnect();
            connectType.switches( dbOperator );
            
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
    
    DocumentCommand(int UIId){
        this._UIId = UIId;
    }
    
}
