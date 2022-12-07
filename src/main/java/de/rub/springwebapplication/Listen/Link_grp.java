package de.rub.springwebapplication.Listen;

public class Link_grp {

    String Id, Grp_Linktext, Icon_Id, Tile_Id, Sort, Description ;

    public Link_grp(String pId, String pGrp_Linktext, String pIcon_Id, String pTile_Id, String pSort, String pDescription) {
        Id = pId;
        Grp_Linktext = pGrp_Linktext;
        Icon_Id = pIcon_Id;
        Tile_Id = pTile_Id;
        Sort = pSort;
        Description = pDescription;
    }
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getGrp_Linktext() {
        return Grp_Linktext;
    }
    public String getIcon_Id() {
        return Icon_Id;
    }
    public String getTile_Id() {
        return Tile_Id;
    }
    public String getSort() {
        return Sort;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
}
