package de.rub.springwebapplication.Data;


import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataBaseUtils extends SQLUtils {

    public DataBaseUtils() throws IOException {
        Wini ini = new Wini(new File("C:\\Users\\Admin\\Desktop\\SpringWebApplication\\src\\main\\resources\\application.properties"));

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

    public String getInfoLDAP_ID_1() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 1");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_2() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 2");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_3() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 3");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_4() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 4");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_5() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 5");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_6() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 6");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_7() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 7");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public String getInfoLDAP_ID_8() {

        ResultSet rs;

        try {

            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID = 8");
            rs.next();
            return rs.getString("GRP_NAME");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed onQuery LDAP");
            return "";
        }

    }

    public void editInfoLDAP_ID_1(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '1'", text);
            System.out.println("Changed Info LDAP_ID 1");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_1");
        }

    }

    public void editInfoLDAP_ID_2(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '2'", text);
            System.out.println("Changed Info LDAP_ID 2");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_2");
        }

    }

    public void editInfoLDAP_ID_3(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '3'", text);
            System.out.println("Changed Info LDAP_ID 3");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_3");
        }

    }

    public void editInfoLDAP_ID_4(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '4'", text);
            System.out.println("Changed Info LDAP_ID 4");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_4");
        }

    }

    public void editInfoLDAP_ID_5(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '5'", text);
            System.out.println("Changed Info LDAP_ID 5");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_5");
        }

    }

    public void editInfoLDAP_ID_6(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '6'", text);
            System.out.println("Changed Info LDAP_ID 6");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_6");
        }

    }

    public void editInfoLDAP_ID_7(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '7'", text);
            System.out.println("Changed Info LDAP_ID 7");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_7");
        }

    }

    public void editInfoLDAP_ID_8(String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID = '8'", text);
            System.out.println("Changed Info LDAP_ID 8");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_8");
        }

    }

}



