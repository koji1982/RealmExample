package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.honestastrology.realmexample.ui.control.CommandControl;

/** EditPageから抜ける時の確認時に表示するダイアログ */
class BackConfirmDialog {
    
    private final AlertDialog              _confirmDialog;
    private final CommandControl<Document> _commandControl;
    
    BackConfirmDialog(MainActivity mainActivity){
        _commandControl   = mainActivity;
        _confirmDialog = new AlertDialog.Builder(mainActivity)
                                    .setMessage( R.string.back_ask )
                                    .setPositiveButton( R.string.back, new BackButton() )
                                    .setNegativeButton( R.string.cancel, new CancelButton() )
                                    .create();
    }
    
    void showConfirm(){
        _confirmDialog.show();
    }
    
    private class BackButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            _commandControl.request( UIRequestCommand.READ );
        }
    }
    
    private class CancelButton implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            dialogInterface.dismiss();
        }
    }
    
}
