package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.honestastrology.realmexample.ui.control.CommandControl;

/** Document削除時に呼ばれるダイアログ。
 * ダイアログ表示中Deleteボタンをタッチした時に削除Commandを送信する */
class DeleteDialog {
    
    private final AlertDialog              _decisionDialog;
    private final CommandControl<Document> _commandControl;
    
    private Document _deleteTarget;
    
    DeleteDialog(MainActivity mainActivity){
        _commandControl   = mainActivity;
        _decisionDialog   = new AlertDialog.Builder(mainActivity)
                                  .setMessage( R.string.delete_ask )
                                  .setPositiveButton( R.string.delete, new DeleteButton())
                                  .setNegativeButton( R.string.cancel, new CancelButton())
                                  .create();
    }
    
    void showConfirm(Document deleteTarget){
        if( deleteTarget == null ) return;
        _deleteTarget = deleteTarget;
        _decisionDialog.show();
    }
    
    private class DeleteButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            _commandControl.send( DBSendCommand.DELETE, _deleteTarget );
            _commandControl.request( UIRequestCommand.READ );
            _deleteTarget = null;
        }
    }
    
    private class CancelButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            dialogInterface.dismiss();
            _deleteTarget = null;
        }
    }
    
}
