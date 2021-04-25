package com.honestastrology.realmexample.ui.content;

import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.honestastrology.realmexample.R;
import com.honestastrology.realmexample.realm.Document;

import java.util.Iterator;

class DocumentViewer implements Viewer<Document> {
    
    private AdapterView<ListAdapter> _listView;
    private ListAdapter              _listAdapter;
    private Document                 _currentSelectedDocument;
    
    DocumentViewer(Activity mainActivity){
        _listView = mainActivity.findViewById(R.id.db_content_list);
        _listAdapter = new ArrayAdapter<>(
                mainActivity,
                R.layout.text_list,
                new String[]{"-----","-----","00000","-----"});
        _listView.setAdapter(_listAdapter);
    }
    
    @Override
    public void setContents(Iterator<Document> iterator){
        
    }
    
    @Override
    public Document getSelectedContent(){
        return _currentSelectedDocument;
    }
    
}
