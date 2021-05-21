package com.example.shirojwttest.model;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class ResultMap extends HashMap<String ,Object> {

    private static final String result= "result";

    public ResultMap success(){
        this.put(result,"success");
        return this;
    }
    public ResultMap fail(){
        this.put(result,"fail");
        return this;
    }
    public ResultMap code(int code){
        this.put("code", code);
        return this;
    }
    public ResultMap message(Object message){
        this.put("message",message);
        return this;
    }
}
