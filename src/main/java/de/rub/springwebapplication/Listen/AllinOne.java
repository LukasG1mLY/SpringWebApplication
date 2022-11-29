package de.rub.springwebapplication.Listen;

public class AllinOne {
    String Id;
    String Linktext;
    String GRP_LINKTEXT;
    String Description;
    String Url_active;


    public AllinOne(String pId,
                String pLinktext,
                String pGRP_LINKTEXT,
                String pDescription,
                String pUrl_active)
    {

        Id = pId;
        Linktext = pLinktext;
        GRP_LINKTEXT = pGRP_LINKTEXT;
        Description = pDescription;
        Url_active = pUrl_active;
    }

    public String getId() {
        return String.valueOf(Integer.parseInt(Id));
    }

    public String getLinktext() {
        return Linktext;
    }

    public String getGRP_LINKTEXT() {
        return GRP_LINKTEXT;
    }

    public String getDescription() {
        return Description;
    }

    public String getUrl_active() {
        return Url_active;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
