package com.honestastrology.realmexample;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/** RealmExampleでの唯一のRealmObject */
public class Document extends RealmObject {
    
    public static final String PRIMARY_KEY    = "_id";
    public static final String EMPTY_STRING   = "";
    public static final int    INIT_ID        = 0;
    public static final int    NEXT_ID_STRIDE = 1;
    
    @PrimaryKey
    private int _id;
    private String _title;
    private String _text;
    
    public Document(){
        this._id = INIT_ID;
    }
    
    public Document(int id){
        this._id    = id;
        this._title = EMPTY_STRING;
        this._text  = EMPTY_STRING;
    }
    
    @Override
    public String toString(){
        return _title;
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
    
    public void updateTitle(@NonNull String title) {
        if( isManaged() ){
            getRealm().executeTransaction(
                    realmTransaction -> this._title = title
            );
        } else {
            this._title = title;
        }
        
    }
    
    public void updateText(@NonNull String text){
        if( isManaged() ){
            getRealm().executeTransaction(
                    realmTransaction -> this._text = text
            );
        } else {
            this._text = text;
        }
    }
    
}
