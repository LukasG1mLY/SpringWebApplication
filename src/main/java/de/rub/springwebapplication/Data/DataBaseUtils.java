package de.rub.springwebapplication.Data;


import org.ini4j.Wini;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DataBaseUtils extends SQLUtils {

    public DataBaseUtils() throws IOException {
        Wini ini = new Wini(new File("C:\\Users\\Admin\\Desktop\\Application\\src\\main\\resources\\application.properties"));

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

    public void addNewIdAndName(String name)
    {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LDAP_GRP");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LDAP_GRP VALUES(?,?)", newId, name);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getAllInfos() {
        ResultSet rs;
        List<String[]> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,GRP_NAME FROM LDAP_GRP ORDER BY ID");
            while (rs.next()) {
                String[] idAndName = {rs.getString("ID"), rs.getString("GRP_NAME")};
                list.add(idAndName);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void editInfoLDAP(int id, String text) {

        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID =?", text, id + 1);
            System.out.println("Changed Info LDAP_ID_" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_" + id);
        }

    }

}
