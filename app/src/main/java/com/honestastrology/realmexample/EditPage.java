package com.honestastrology.realmexample;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.honestastrology.realmexample.ui.layout.Viewer;
import com.honestastrology.realmexample.ui.layout.ViewPage;

class EditPage implements ViewPage<Document> ,View.OnClickListener {
    
    private Viewer<Document>   _viewer;
    private TextView           _editView;
    private Document           _selectedDocument;
    
    EditPage(Activity mainActivity, Viewer<Document> viewer){
        
        mainActivity.setContentView( R.layout.edit_view );
        _editView = mainActivity.findViewById( R.id.body_text);
        
        _viewer   = viewer;
    }
    
    @Override
    public void onClick(View view){
        _selectedDocument.setText( _editView.getText().toString() );
    }
    
    @Override
    public void showContent(){
        _selectedDocument = _viewer.getSelectedContent();
        _editView.setText( _selectedDocument.getText() );
    }
    
}
