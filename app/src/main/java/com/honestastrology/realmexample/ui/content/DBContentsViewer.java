package com.honestastrology.realmexample.ui.content;

import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.honestastrology.realmexample.R;

import java.util.Iterator;

import io.realm.RealmObject;

class DBContentsViewer<E extends RealmObject> implements Viewer<E> {
    
    private AdapterView<ListAdapter> _listView;
    private ListConverter<E> _listConverter;
    
    private E      _currentSelectedDocument;
    
    DBContentsViewer(Activity mainActivity, Class<E> clazz){
        _listView = mainActivity.findViewById(R.id.db_content_list);
        _listConverter = ListConverter.create(clazz);
        ListAdapter listAdapter= new ArrayAdapter<>(
                            mainActivity,
                            R.layout.text_list,
                            new String[]{"-----","-----","00000","-----"});
        _listView.setAdapter(listAdapter);
    }
    
    @Override
    public void setContents(Iterator<E> iterator){
        
    }
    
    @Override
    public E getSelectedContent(){
        return _currentSelectedDocument;
    }
    
}
