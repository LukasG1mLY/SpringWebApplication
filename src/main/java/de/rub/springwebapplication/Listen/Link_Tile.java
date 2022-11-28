package de.rub.springwebapplication.Listen;

public class Link_Tile {

    String Id, Name, Tile_Column_Id, Sort, Description ;

    public Link_Tile(String pId, String pName, String pDescription, String pSort, String pTile_Column_Id) {
        Id = pId;
        Name = pName;
        Description = pDescription;
        Sort = pSort;
        Tile_Column_Id = pTile_Column_Id;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getTile_Column_Id() {return Tile_Column_Id;}
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
