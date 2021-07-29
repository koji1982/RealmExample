package com.honestastrology.realmexample.ui.control;

import io.realm.RealmObject;

/** DB,Viewerへの各操作コマンドを受け取るインターフェース */
public interface CommandControl<E extends RealmObject> {
    
    public void request(RequestCommand<E> command);
    
    public void send(SendCommand<E> command, E sendTarget);
    
}
