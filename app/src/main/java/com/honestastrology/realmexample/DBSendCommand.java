package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.control.SendCommand;

enum DBSendCommand implements SendCommand<Document> {
    
    UPDATE  {
        @Override
        public void execute(DBOperator dbOperator, Document sendTarget){
            if( dbOperator.isNull() ) return;
            dbOperator.update( sendTarget );
        }
    },
    DELETE   {
        @Override
        public void execute(DBOperator dbOperator, Document sendTarget){
            if( dbOperator.isNull() ) return;
            dbOperator.delete( sendTarget );
        }
    };
    
}
