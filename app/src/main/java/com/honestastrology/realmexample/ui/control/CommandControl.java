package com.honestastrology.realmexample.ui.control;

import io.realm.RealmObject;

public interface CommandControl<E extends RealmObject> {
    
    public void request(RequestCommand<E> command);
    
    public void send(SendCommand<E> command, E sendTarget);
    
}
