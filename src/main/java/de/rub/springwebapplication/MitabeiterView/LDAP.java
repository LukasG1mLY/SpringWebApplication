package de.rub.springwebapplication.MitabeiterView;

public class LDAP {

    String id, content;

    public LDAP(String pId, String pContent) {
        id = pId;
        content = pContent;
    }

    public String getContent() {return content;}
    public String getId() {return id;}
}
