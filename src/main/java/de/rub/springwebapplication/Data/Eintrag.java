package de.rub.springwebapplication.MitabeiterView;

public class Eintrag {

    String id, content;

    public Eintrag(String pId, String pContent) {
        id = pId;
        content = pContent;
    }

    public String getContent() {return content;}
    public String getId() {return id;}
}
