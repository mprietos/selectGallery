package net.opentrends.selectgallery.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoFolder implements Serializable
{

    private int id;
    private String name;
    private String coverPath;
    private List<PhonePhoto> photoList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public List<PhonePhoto> getPhotoList() {
        if ( photoList == null ) {
            photoList = new ArrayList<>();
        }
        return photoList;
    }

    public void setPhotoList(List<PhonePhoto> photoList) {
        this.photoList = photoList;
    }
}
