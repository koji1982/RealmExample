package com.honestastrology.realmexample;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.honestastrology.realmexample.ui.layout.Viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DocumentViewer implements Viewer<Document> {
    
    private static final String[] INIT_TEXT
            = new String[]{"TITLE 1" + "\n" + "TEXT BODY 1","-----","00000","-----"};
    
    private AdapterView<ArrayAdapter<String>> _listView;
    
    private Document        _currentSelectedDocument;
    
    DocumentViewer(Activity mainActivity){
        _listView = mainActivity.findViewById(R.id.db_content_list);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(mainActivity, R.layout.text_list);
        _listView.setAdapter(arrayAdapter);
        _listView.setOnItemClickListener(new ListClickListener());
    }
    
    @Override
    public void setContents(Iterator<Document> iterator){
        //Iterator内のDocumentからtitleを取り出してListに追加
        //そのtitleListをListViewとして表示する
        List<String> titleList   = new ArrayList<>();
        while( iterator.hasNext() ){
            Document document = iterator.next();
            String title = document.getTitle();
            if( title.equals(null)){
                title = "";
            }
            titleList.add( title );
        }
        ArrayAdapter<String> contentsAdapter = _listView.getAdapter();
        contentsAdapter.clear();
        contentsAdapter.addAll( titleList );
        _listView.setAdapter( contentsAdapter );
    }
    
    @Override
    public Document getSelectedContent(){
        //onItemClick()のパラメータpositionの値をもとに
        //更新される
        return _currentSelectedDocument;
    }
    
    private static class ListClickListener implements AdapterView.OnItemClickListener {
        
        @Override
        public void onItemClick(AdapterView<?> adapterView,
                                View view,
                                int position,
                                long id){
            
        }
    }
    
}
