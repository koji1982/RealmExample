package com.honestastrology.realmexample.database;

/** 通常どおり保存される(DURABLE)のかアプリ終了後にデータが保存されない(TEMPORARY)
 * のかを指定するための列挙型。特に指定がなければ通常どおり保存される(DURABLE)。　*/
public enum Persistence {
    DURABLE,
    TEMPORARY;
}
