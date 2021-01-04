package com.android_enterprises.discountcards.model;

public enum shopType {
    food(1),
    clothing(2),
    general(3);

    private int id;

    private shopType(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public  static shopType fromId(int id){
        for(shopType type:values()){
            if(type.getId() == id){
                return type;
            }
        }
        return null;
    }
}

