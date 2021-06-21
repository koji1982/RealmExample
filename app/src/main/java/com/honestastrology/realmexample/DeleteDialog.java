package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.honestastrology.realmexample.ui.control.CommandControl;

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
        _deleteTarget = deleteTarget;
        _decisionDialog.show();
    }
    
    private class DeleteButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            _commandControl.send( DocumentSendCommand.DELETE, _deleteTarget );
            _commandControl.request( DocumentUICommand.READ );
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
