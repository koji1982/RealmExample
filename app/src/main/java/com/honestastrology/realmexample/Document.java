package com.honestastrology.realmexample;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Document extends RealmObject {
    
    public static final String PRIMARY_KEY = "_id";
    
    @PrimaryKey
    private int    _id;
    private String _title;
    private String _text;
    
    public Document(){
        this._id = 0;
    }
    
    public Document(int id){
        this._id    = id;
        this._title = "";
        this._text  = "";
    }
    
    public int getId(){
        return _id;
    }
    
    public String getTitle() {
        return _title;
    }
    
    public String getText(){
        return _text;
    }
    
    public void setTitle(String title) {
        this._title = title;
    }
    
    public void setText(String text){
        this._text = text;
    }
    
}
