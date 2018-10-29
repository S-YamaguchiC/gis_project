package com.example.linebot.line;

import java.util.ArrayList;

public class Substring {

    public ArrayList<String> getString(String str) {

        // substring var(4 chars)
        String[] sub = new String[4];

        // split for break(ex. AB:C, D:EF, :GHI...)
        String[] split = str.split("\n");

        // (ex. C, EF, GHI...)
        for ( int i=0; i<4; i++) {
            sub[i] = split[i].substring(split[i].indexOf("：")+1);
        }

        int i=0;
        ArrayList<String> array = new ArrayList<>();
        while(i < 4) {
            array.add(sub[i]);
            i++;
        }

        return array;
    }
}
