package com.honestastrology.realmexample.ui.layout;

import java.util.Iterator;
import java.util.List;

import io.realm.RealmObject;

public interface Viewer<E extends RealmObject> {
    
    public void setContents(Iterator<E> contents);
    
    public List<E> getContents();
    
    public void setSelectedContent(E selectedContent);
    
    public E getSelectedContent();
    
    public void transitViewPage(LayoutType<E> layoutType);
    
}
