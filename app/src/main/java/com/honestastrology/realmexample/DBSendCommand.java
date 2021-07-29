package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.control.SendCommand;

/** DBOperatorへDocument(RealmObject)を引数として渡す場合のCommand */
enum DBSendCommand implements SendCommand<Document> {
    /** Document(RealmObject)が　isManaged == false の時に、
     * Documentを更新するためのコマンド。
     * isManaged == true の場合はDocumentを直接更新するだけでDB側のデータも
     * 更新されるため、このコマンドは必要ない。 */
    UPDATE  {
        @Override
        public void execute(DBOperator dbOperator, Document targetDocument){
            if( dbOperator.isNull() ) return;
            dbOperator.update( targetDocument );
        }
    },
    /** Document(RealmObject)を削除するためのコマンド */
    DELETE   {
        @Override
        public void execute(DBOperator dbOperator, Document targetDocument){
            if( dbOperator.isNull() ) return;
            dbOperator.delete( targetDocument );
        }
    };
    
}
