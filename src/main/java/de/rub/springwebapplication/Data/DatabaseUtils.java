package de.rub.springwebapplication.Data;

import de.rub.springwebapplication.Listen.*;
import de.rub.springwebapplication.Listen.Icon;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils extends SQLUtils {

    public DatabaseUtils() throws IOException {
        Wini ini = new Wini(new File("src/main/resources/application.properties"));

        String pDatabaseUrl = ini.get("pDatabaseUrl", "spring.datasource.url");
        String pUser = ini.get("pUser", "spring.datasource.username");
        String pPassword = ini.get("pPassword", "spring.datasource.password");

        try {
            this.setDatabase(pDatabaseUrl, pUser, pPassword);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String getInfoStaff() {

        ResultSet rs;

        try {
            rs = onQuery("SELECT INFO FROM INFOBOX WHERE ROLLE = 'staff'");
            rs.next();
            return rs.getString("INFO");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery");
            return "";
        }
    }
    public String getInfoStudent() {

        ResultSet rs;

        try {
            rs = onQuery("SELECT INFO FROM INFOBOX WHERE ROLLE = 'student'");
            rs.next();
            return rs.getString("INFO");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery");
            return "";
        }
    }
    public void editInfoStaff(String text) {
        try {
            onExecute("UPDATE INFOBOX SET INFO = ? WHERE ROLLE = 'staff'", text);
            System.out.println("Changed Info Text for staff");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute");

        }
    }
    public void editInfoStudent(String text) {
        try {
            onExecute("UPDATE INFOBOX SET INFO = ? WHERE ROLLE = 'student'", text);
            System.out.println("Changed Info Text for student");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute");
        }
    }
    public void editInfoLDAP(int id, String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID =?", text, id);
            System.out.println("Changed Info LDAP_ID_" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_" + id);
        }
    }
    public void editInfoLDAP_ROLE(int ID, String Content) {
        try {
            onExecute("UPDATE LDAP_ROLE SET ROLE_NAME =? WHERE ID =?",Content, ID);
            System.out.println("Changed Info LDAP_ROLE " + ID);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ROLE " + ID);
        }
    }
    public void editInfoLink(int Id, String Linktext, Integer Link_group_ID, Double Sort, String Description, String Url_Active, Integer Url_inActive, Integer Active, Double Auth_Level, Integer NewTab) {

        try {
            onExecute("UPDATE LINK SET LINKTEXT =?,LINK_GRP_ID =?,SORT =?,LINK_DESCRIPTION =?,URL_ACTIVE =?,URL_INACTIVE =?,ACTIVE =?,AUTH_LEVEL =?,NEWTAB =? WHERE ID =?",Linktext, Link_group_ID, Sort, Description, Url_Active, Url_inActive, Active, Auth_Level, NewTab, Id);
            System.out.println("Changed Info LINK_" + (Id));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LINK_" + Id);
        }

    }
    public void editInfoLink_Grp(int id, String pGrp_Linktext, Double pIcon_Id, Double pTile_Id, Double pSort, String pDescription) {
        try {
            onExecute("UPDATE LINK_GRP SET GRP_LINKTEXT =?, ICON_ID =?, TILE_ID =?, SORT =?, LINK_GRP_DESCRIPTION =? WHERE ID =?", pGrp_Linktext, pIcon_Id, pTile_Id, pSort, pDescription, id);
            System.out.println("Changed Info Link group:" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by Link group:" + id);
        }
    }
    public void editInfoLink_Tile(String pName, String pDescription, Double pSort, Double pTile_Column_Id, int id) {
        try {
            onExecute("UPDATE LINK_TILE SET NAME =?, DESCRIPTION =?, SORT =?, TILE_COLUMN_ID =? WHERE ID =?", pName, pDescription, pSort, pTile_Column_Id, id);
            System.out.println("Changed Info Link Tile:" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by Link Tile:" + id);
        }
    }
    public void deleteInfoLDAP(int id)  {
        try {
            onExecute("DELETE FROM LDAP_GRP WHERE ID =?", id);
            System.out.println("Deleted ROW_ " + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void deleteInfoLink(int id)  {
        try {
            onExecute("DELETE FROM LINK_TO_LDAP_GRP WHERE LINK_ID =?", id);
            onExecute("DELETE FROM LINK WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }
    }
    public void deleteInfoLDAP_ROLE(int ID) {
        try {
            onExecute("DELETE FROM LDAP_ROLE WHERE ID =?", ID);
            System.out.println("Deleted ROW_" + (ID));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (ID));
        }
    }
    public void deleteInfoLink_Grp(int id)  {
        try {
            onExecute("DELETE FROM LINK_GRP WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void deleteInfoLink_Tile(int id)  {
        try {
            onExecute("DELETE FROM LINK_TILE WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void addNewIdAndName_Link(Integer LdapId, String Linktext, String Link_group_ID, Double Sort, String Description, String Url_Active, Boolean Url_inActive, Boolean Active, Double Auth_Level, Boolean NewTab) {
        try {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK VALUES(?,?,?,?,?,?,?,?,?,?)", newId, Linktext, Link_group_ID, Sort, Description, Url_Active, Url_inActive, Active, Auth_Level, NewTab);
            onQuery("SELECT LD.ID,LG.LDAP_GRP_ID FROM LDAP_GRP LD INNER JOIN LINK_TO_LDAP_GRP LG ON LD.ID = LG.LDAP_GRP_ID");
            onExecute("INSERT INTO LINK_TO_LDAP_GRP VALUES(?,?)", newId, LdapId);

            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Link_without(String Linktext, String Link_group_ID, Double Sort, String Description, String Url_Active, Boolean Url_inActive, Boolean Active, Double Auth_Level, Boolean NewTab) {
        try {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK VALUES(?,?,?,?,?,?,?,?,?,?)", newId, Linktext, Link_group_ID, Sort, Description, Url_Active, Url_inActive, Active, Auth_Level, NewTab);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Ldap(String name) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LDAP_GRP ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LDAP_GRP VALUES(?,?)", newId, name);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_ROLE(String Content) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LDAP_ROLE ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LDAP_ROLE VALUES(?,?)", newId, Content);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Link_Grp(String pGrp_Linktext, Double pIcon_Id, Double pTile_Id, Double pSort, String pDescription) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK_GRP ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK_GRP VALUES(?,?,?,?,?)", newId, pGrp_Linktext, pIcon_Id, pTile_Id, pSort, pDescription);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Link_Tile(String pName, String pDescription, Double pSort, Double pTile_Column_Id) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK_TILE ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK_TILE VALUES(?,?,?,?,?)", newId, pName, pDescription, pSort, pTile_Column_Id);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<InfoBox> getInfo_InfoBox() {
        ResultSet rs;
        List<InfoBox> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,ROLLE,INFO FROM INFOBOX ORDER BY ID");
            while (rs.next()) {
                list.add(new InfoBox(rs.getString("ROLLE"), rs.getString("INFO")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery by Infobox");
        }
        return list;
    }
    public List<Ldap> getInfo_Ldap() {
        ResultSet rs;
        List<Ldap> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,GRP_NAME FROM LDAP_GRP ORDER BY ID");
            while (rs.next()) {
                list.add(new Ldap(rs.getString("ID"), rs.getString("GRP_NAME")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<LDAP_ROLE> getAllInfos_LDAP_ROLE() {
        ResultSet rs;
        List<LDAP_ROLE> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,ROLE_NAME FROM LDAP_ROLE ORDER BY ID");
            while (rs.next()) {
                list.add(new LDAP_ROLE(rs.getString("ID"), rs.getString("ROLE_NAME")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Link_grp> getInfo_Link_Grp() {
        ResultSet rs;
        List<Link_grp> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM LINK_GRP ORDER BY ID");
            while (rs.next()) {
                list.add(new Link_grp(
                        rs.getString("ID"),
                        rs.getString("GRP_LINKTEXT"),
                        rs.getString("ICON_ID"),
                        rs.getString("TILE_ID"),
                        rs.getString("SORT"),
                        rs.getString("LINK_GRP_DESCRIPTION")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Link_Tile> getInfo_Link_Tile() {
        ResultSet rs;
        List<Link_Tile> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM LINK_TILE ORDER BY ID");
            while (rs.next()) {
                list.add(new Link_Tile(
                        rs.getString("ID"),
                        rs.getString("NAME"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("SORT"),
                        rs.getString("TILE_COLUMN_ID")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Tile_Column> getInfo_Tile_Column() {
        ResultSet rs;
        List<Tile_Column> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM TILE_COLUMN ORDER BY ID");
            while (rs.next()) {
                list.add(new Tile_Column(
                        rs.getString("TILE_NAME"),
                        rs.getString("ID")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<dbIcon> getIconImage() {
        ResultSet rs;
        List<dbIcon> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM ICON ORDER BY ID");
            while (rs.next()) {
                list.add(new dbIcon(
                        rs.getString("ID"),
                        rs.getString("CONTENTTYPE")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Link> getInfo_Link() {
        ResultSet rs;
        List<Link> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT L.*,LG.GRP_LINKTEXT,LG.LINK_GRP_DESCRIPTION,LG.ID FROM LINK L INNER JOIN LINK_GRP LG ON L.LINK_GRP_ID = LG.ID ORDER BY L.ID");
            while (rs.next()) {
                list.add(new Link(
                        rs.getString("ID"),
                        rs.getString("LINKTEXT"),
                        rs.getString("GRP_LINKTEXT"),
                        rs.getString("SORT"),
                        rs.getString("LINK_DESCRIPTION"),
                        rs.getString("URL_ACTIVE"),
                        rs.getString("URL_INACTIVE"),
                        rs.getString("ACTIVE"),
                        rs.getString("AUTH_LEVEL"),
                        rs.getString("NEWTAB")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Ldap> getInfo_Link_to_Ldap_Grp() {
        ResultSet rs;
        List<Ldap> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM LINK_TO_LDAP_GRP ORDER BY ID");
            while (rs.next()) {
                list.add(new Ldap(rs.getString("ID"),
                        rs.getString("GRP_NAME")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public Ldap addNewIdAndName_Link1(String LdapId) {
        try {
            ResultSet rs = onQuery("SELECT LD.ID,LG.LDAP_GRP_ID FROM LDAP_GRP LD INNER JOIN LINK_TO_LDAP_GRP LG ON LD.ID = LG.LDAP_GRP_ID WHERE ID =?", LdapId);
            while (rs.next()) {
                rs.getString("LDAP_GRP_ID");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getLinkImage(int i) {

        ResultSet rs;

        try {
            rs = onQuery("SELECT URL FROM ICON WHERE ID =?", i);
            rs.next();
            return rs.getString("URL");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery");
            return "";
        }
    }

    public List<Icon> Icon() throws SQLException {
        ResultSet rs = onQuery("SELECT ID, CONTENTTYPE, ICON FROM ICON");
        List<Icon> icons = new ArrayList<>();
        try {
            while (rs.next()) {
                String id = rs.getString("ID");
                String contentType = rs.getString("CONTENTTYPE");
                Blob iconData = rs.getBlob("ICON");
                Icon icon = new Icon(id, contentType, iconData);
                icons.add(icon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return icons;
    }

    public void saveToDB(byte[] iconData, String contentType) throws SQLException {
        // Get the next ID for the new row
        ResultSet rs = onQuery("SELECT MAX(ID) FROM ICON");
        long nextId = 1;
        try {
            if (rs.next()) {
                nextId = rs.getLong(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert the data into the database
        try {
            onExecute("INSERT INTO ICON (ID, ICON, CONTENTTYPE) VALUES (?, ?, ?)", nextId, iconData, contentType);
        } catch (SQLException e) {
            e.printStackTrace();



        }
    }



}
