package net.opentrends.selectgallery.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Photos {

    private String path, name;

    public Photos(String path, String name){
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
