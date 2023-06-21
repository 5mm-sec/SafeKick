package com.example.safekick;

import com.google.gson.annotations.SerializedName;

public class registerDATA {
    @SerializedName("helmet_state")
    private int helmet_state;

    @SerializedName("two_riding")
    private int two_riding;

    @SerializedName("accident")
    private int accidnet;

    @SerializedName("Delete_auth")
    private int Delete_auth;

    @SerializedName("wholeflags")
    private  int wholeflags;

    public int get_Test() {
        return helmet_state;
    }
    public int getHelmet_state() {return helmet_state;}
    public int getTwo_riding() {return two_riding;}
    public int getAccidnet(){return accidnet; }
    public int getDelete_auth(){return Delete_auth;}
    public int getWholeflags(){return wholeflags;}

}
