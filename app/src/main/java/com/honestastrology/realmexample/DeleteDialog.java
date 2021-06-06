package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

class DeleteDialog {
    
    private final AlertDialog          _decisionDialog;
    private final View.OnClickListener _operationCommand;
    
    private View _deleteTarget;
    
    DeleteDialog(MainActivity mainActivity){
        _operationCommand = mainActivity;
        _decisionDialog   = new AlertDialog.Builder(mainActivity)
                                  .setMessage( R.string.delete_ask )
                                  .setPositiveButton( R.string.delete, new DeleteButton())
                                  .setNegativeButton( R.string.cancel, (dialog, i)->{ dialog.dismiss(); })
                                  .create();
        
    }
    
    void show(View view){
        _deleteTarget = view;
        _decisionDialog.show();
    }
    
    private class DeleteButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            _operationCommand.onClick( _deleteTarget );
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
