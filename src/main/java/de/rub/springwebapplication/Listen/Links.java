package de.rub.springwebapplication.Listen;

public class Links {

    String Links;
    Integer Id;

    public Integer getId() {
        return Id;
    }

    public Links(String pLinks, Integer pId) {
        Links = pLinks;
        Id = pId;

    }
    public String getLinks() {
        return Links;
    }
}
