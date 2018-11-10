import com.healthmarketscience.jackcess.*;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Database;

import java.awt.*;
import java.util.*;
import java.nio.*;
import java.net.*;
import java.io.*;

/*
    Layout of the gvnapster.mdb file:

    Table SharedFiles has fields:
        fileID (int)
        userID (int)
        name (string)
        type (string)
        description (string)

    Table Users has fields:
        userID (int)
        first (string)
        last (string)
        userName (string)
        hostName (string)
        ipAddress (string)
        port (int)
 */

public class CentralServer {
    ;

    public static void main(String[] args){
        try {
            DatabaseBuilder d = new DatabaseBuilder();
            Database db = d.open(new File("gvnapster.mdb"));

            Table users = db.getTable("Users");
            Table sharedFiles = db.getTable("SharedFiles");

            for (Row row: users
                 ) {
                System.out.println("Look ma, a row: " + row);
            }

            for (Row row: sharedFiles
                 ) {
                System.out.println("Different table, row: " + row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
