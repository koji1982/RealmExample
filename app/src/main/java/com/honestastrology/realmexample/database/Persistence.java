package com.honestastrology.realmexample.database;

/** In-Memory Realmであるかどうかを判別するための列挙型 */
public enum Persistence {
    DURABLE,
    TEMPORARY;
}
