package de.rub.springwebapplication.Data;

import KLibrary.utils.SQLUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
public class DataBaseUtils extends SQLUtils {





    //Datenbank Zugangsdaten Link, Nutzername, Passwort

    private static final String pDatabaseUrl = "jdbc:oracle:thin:@ora-uv.uv.rub.de/multi.uv";
    private static final String pUser = "portaltest";
    private static final String pPassword = "DfkLSr4ETest";


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
    public void editInfoStaff(String text) {
        try {
            onExecute("UPDATE INFOBOX SET INFO = ? WHERE ROLLE = 'staff'", text);
            System.out.println("Worked");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute");
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
    public void editInfoStudent(String text) {
        try {
            onExecute("UPDATE INFOBOX SET INFO = ? WHERE ROLLE = 'student'", text);
            System.out.println("Worked");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute");
        }
    }
    public DataBaseUtils() throws SQLException {
        super(pDatabaseUrl, pUser, pPassword);
    }
}


