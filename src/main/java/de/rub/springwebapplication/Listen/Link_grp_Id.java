package de.rub.springwebapplication.Listen;

public class Link_grp_Id {

    String Grp_Linktext, Description;
    Integer Id;

    public Integer getId() {
        return Id;
    }

    public Link_grp_Id(String pGrp_Linktext, String pDiscription, Integer pId) {



        Grp_Linktext = pGrp_Linktext;
        Description = pDiscription;
        Id = pId;

    }
    public String getGrp_Linktext() {
        if (Description == null) {
            return Grp_Linktext;
        } else {
            return Grp_Linktext +  " --> " + Description;
        }
    }
}
