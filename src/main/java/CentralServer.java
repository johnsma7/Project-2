
import com.healthmarketscience.jackcess.*;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Database;
import javax.xml.parsers.*;

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




    public static void main(String[] args){
        try {

            int port =12000;
            ServerSocket welcomeSocket;
            while(true) {
                welcomeSocket = new ServerSocket(port);
                Socket connectionSocket = welcomeSocket.accept();
                CentralClientHandler cch = new CentralClientHandler(connectionSocket);
                cch.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class CentralClientHandler extends Thread{
    private Database db;
    private Table users, sharedFiles;
    private Socket client;
    private DataOutputStream outToClient;
    private BufferedReader inFromClient;

    CentralClientHandler(Socket c){
        try {
            db = DatabaseBuilder.open(new File("E:\\Projects\\Project2\\src\\main\\resources\\gv.mdb"));//Change this for your individual machines.
            users = db.getTable("Users");
            sharedFiles = db.getTable("SharedFiles");

            client = c;
            outToClient = new DataOutputStream(client.getOutputStream());
            inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

            /*
            This is used in debugging:

            for (Row row : users
            ) {
                System.out.println("Look ma, a row: " + row);
            }

            for (Row row : sharedFiles
            ) {
                System.out.println("Different table, row: " + row);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        // Read in the xml file line by line and add the information to the database.
        // This should only be done the first time that a host connects to the cs.
        try {

            String nextLine = inFromClient.readLine();
            File x = new File("fromClient.xml");
            BufferedWriter bw = new BufferedWriter(new FileWriter(x));
            while (!nextLine.equals("EOF") || nextLine == null) {

                bw.write(nextLine);

                nextLine = inFromClient.readLine();
            }

            /*TODO: Parse the xml file and add the relevant data to the database */



        } catch (Exception e) {
            e.printStackTrace();
        }


        /*TODO: This is where the cs waits for the keyword search from the host
            and returns the desired information in a series of strings. */

        while(true){
            try {



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
