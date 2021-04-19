package com.honestastrology.realmexample.ui.button;

import com.honestastrology.realmexample.R;

enum DBOperation implements Operation {
    
    CREATE    (R.id.create_button) {
        @Override
        public void sendRequest(){
            
        }
    },
    READ      (R.id.read_button){
        @Override
        public void sendRequest(){
            
        }
    },
    UPDATE    (R.id.update_button){
        @Override
        public void sendRequest(){
            
        }
    },
    DELETE    (R.id.delete_button){
        @Override
        public void sendRequest(){
            
        }
    };
    
    int getUIId(){
        return _UIId;
    }
    
    private final int _UIId;
    
    DBOperation(int UIId){
        this._UIId = UIId;
    }
    
}
