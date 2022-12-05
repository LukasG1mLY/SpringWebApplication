package de.rub.springwebapplication.Listen;

public class Link {
    String Id, Linktext, Grp_Linktext, description, Sort, Description, Url_active, Url_inactive, Active, Auth_level, Newtab;
    Integer id;


    public Link(String pId,
                String pLinktext,
                String pGrp_Linktext,
                String pdescription,
                String pSort,
                String pDescription,
                String pUrl_active,
                String pUrl_inactive,
                String pActive,
                String pAuth_level,
                String pNewtab,
                Integer pid) {

        Id = pId;
        Linktext = pLinktext;
        Grp_Linktext = pGrp_Linktext;
        description = pdescription;
        Sort = pSort;
        Description = pDescription;
        Url_active = pUrl_active;
        Url_inactive = pUrl_inactive;
        Active = pActive;
        Auth_level = pAuth_level;
        Newtab = pNewtab;
        id = pid;
    }

    public String getId() {
        return String.valueOf(Integer.parseInt(Id));
    }

    public String getLinktext() {
        return Linktext;
    }
    public String getGrp_Linktext() {
        if (description == null) {
            return Grp_Linktext;
        } else {
            return Grp_Linktext + " --> " + description;
        }
    }
    public String getSort() {
        return Sort;
    }
    public String getDescription() {
        return Description;
    }
    public String getUrl_active() {
        return Url_active;
    }
    public String getUrl_inactive() {
        return Url_inactive;
    }
    public String getActive() {
        return Active;
    }
    public String getAuth_level() {
        return Auth_level;
    }
    public String getNewtab() {
        return Newtab;
    }
    public Integer getid() {
        return id;
    }

}
