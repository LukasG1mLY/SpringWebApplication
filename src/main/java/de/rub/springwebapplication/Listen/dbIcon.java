package de.rub.springwebapplication.Listen;

public class dbIcon {
    String Id, ContentType;

    public dbIcon(String pId, String pContentType) {
        Id = pId;
        ContentType = pContentType;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContentType() {
        return ContentType;
    }

}
