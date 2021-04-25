package com.honestastrology.realmexample;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.honestastrology.realmexample.realm.Document;
import com.honestastrology.realmexample.ui.content.Viewer;
import com.honestastrology.realmexample.ui.control.DocumentCommand;
import com.honestastrology.realmexample.ui.control.Operator;
import com.honestastrology.realmexample.ui.control.Selector;
import com.honestastrology.realmexample.ui.control.UICommand;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener {
    
    private Viewer<Document>   _viewer;
    private Selector<Document> _buttonSelector;
    private Operator           _dbOperator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        _dbOperator = Operator.createOperator(this);
    }
    
    private void initUI(){
        
        setContentView(R.layout.activity_main);
        _viewer         = Viewer.create(this, Document.class);
        _buttonSelector = Selector.create(DocumentCommand.values());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _dbOperator.closeAll();
    }
    
    @Override
    public void onClick(View view){
        UICommand<Document> clickedCommand 
                = _buttonSelector.selectCommand( view.getId() );
        clickedCommand.execute( _viewer, _dbOperator);
    }
    
}