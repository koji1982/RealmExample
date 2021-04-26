package com.honestastrology.realmexample;

import com.honestastrology.realmexample.ui.control.UICommand;
import com.honestastrology.realmexample.dbaccess.DBOperator;
import com.honestastrology.realmexample.ui.layout.Viewer;

/**
 * 
 */

public enum DocumentCommand implements UICommand<Document> {
    
    CREATE    (R.id.create_button) {
        @Override
        public void execute(Viewer<Document> viewer, DBOperator _dbOperator){
            Number maxPrimaryNumber
                    = _dbOperator.getMaxPrimaryNumber( Document.class, Document.PRIMARY_KEY);
            if( maxPrimaryNumber == null ){
                maxPrimaryNumber = R.integer.default_0;
            }
            int nextId = (int)maxPrimaryNumber + R.integer.plus_1;
            Document newDocument = new Document( nextId );
            _dbOperator.create( newDocument );
        }
    },
    READ      (R.id.read_button){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator _dbOperator){
            viewer.setContents( _dbOperator.readAll(Document.class) );
        }
    },
    UPDATE    (R.id.update_button){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator _dbOperator){
            _dbOperator.update( viewer.getSelectedContent() );
        }
    },
    DELETE    (R.id.delete_button){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator _dbOperator){
            _dbOperator.delete( viewer.getSelectedContent() );
        }
    },
    TEXT_LIST (R.id.document_title){
        @Override
        public void execute(Viewer<Document> viewer, DBOperator _dbOperator){
            System.out.println("   TEXT_LIST   " 
                       + String.valueOf(viewer.getSelectedContent().getId()));
        }
    };
    
    @Override
    public int getUIId(){
        return _UIId;
    }
    
    private final int _UIId;
    
    DocumentCommand(int UIId){
        this._UIId = UIId;
    }
    
}
