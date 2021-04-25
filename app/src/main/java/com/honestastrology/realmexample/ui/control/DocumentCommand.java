package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.R;
import com.honestastrology.realmexample.realm.Document;
import com.honestastrology.realmexample.ui.content.Viewer;

public enum DocumentCommand implements UICommand<Document> {
    
    CREATE    (R.id.create_button) {
        @Override
        public void execute(Viewer<Document> viewer, Operator _dbOperator){
            _dbOperator.create( viewer.getSelectedContent() );
        }
    },
    READ      (R.id.read_button){
        @Override
        public void execute(Viewer<Document> viewer, Operator _dbOperator){
            viewer.setContents( _dbOperator.readAll(Document.class) );
        }
    },
    UPDATE    (R.id.update_button){
        @Override
        public void execute(Viewer<Document> viewer, Operator _dbOperator){
            _dbOperator.update( viewer.getSelectedContent() );
        }
    },
    DELETE    (R.id.delete_button){
        @Override
        public void execute(Viewer<Document> viewer, Operator _dbOperator){
            _dbOperator.delete( viewer.getSelectedContent() );
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
