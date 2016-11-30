package net.opentrends.selectgallery.model;

import java.io.Serializable;

/**
 * Created by miguelprieto on 30/11/16.
 */

public class PhonePhoto implements Serializable {
    private int id;
    private String folderName;
    private String photoPath;
    private boolean isPressed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }
}
