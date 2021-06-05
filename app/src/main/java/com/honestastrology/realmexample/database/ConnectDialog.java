package com.honestastrology.realmexample.database;

import android.app.ProgressDialog;
import android.content.Context;

import com.honestastrology.realmexample.R;

class ConnectDialog implements WaitingDialog {
    
    private final ProgressDialog _progressDialog;
    
    ConnectDialog(Context mainActivity){
        //SyncDB接続中に表示するダイアログを生成する
        //生成にContextが必要なのでここで生成して保持しておく
        _progressDialog = new ProgressDialog( mainActivity );
        _progressDialog.setMessage(mainActivity.getText(R.string.connecting));
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
    
    @Override
    public void showConnectingDialog(){
        if( _progressDialog.isShowing() ) return;
        _progressDialog.show();
    }
    
    @Override
    public void dismissConnectingDialog(){
        if( !_progressDialog.isShowing() ) return;
        _progressDialog.dismiss();
    }
}
