package de.rub.springwebapplication.Listen;

public class Link_to_Ldap_Grp {

    String Link_Id, Ldap_Grp_Id;

    public Link_to_Ldap_Grp(String pLink_Id, String pLdap_Grp_Id) {
        Link_Id = pLink_Id;
        Ldap_Grp_Id = pLdap_Grp_Id;
    }

    public String getLink_Id() {
        return Link_Id;
    }

    public String getLdap_Grp_Id() {
        return Ldap_Grp_Id;
    }
}
