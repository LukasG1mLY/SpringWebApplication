package de.rub.springwebapplication.Listen;

import java.sql.Blob;

public class Icon {
    private String id;
    private String contentType;
    private Blob iconData;

    public Icon(String id, String contentType, Blob iconData) {
        this.id = id;
        this.contentType = contentType;
        this.iconData = iconData;
    }

    public String getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public Blob getIconData() {
        return iconData;
    }
}