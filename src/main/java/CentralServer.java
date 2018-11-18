
import com.healthmarketscience.jackcess.*;
import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Database;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.*;

import java.awt.*;
import java.util.*;
import java.nio.*;
import java.net.*;
import java.io.*;

/*
    Layout of the gv.mdb file:

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
        while(true){
            try {
            /* This is where the cs waits for the keyword search from the host
                and returns the desired information in a series of strings. */

                String nextLine = inFromClient.readLine();

                /* This should only be done the first time that a host connects to the cs.
                 * The first line should look like :
                 * init username hostname speed\n
                 *
                 * The second line will be the start of the xml file
                 * */
                if (nextLine.startsWith("init")) {

                    // Grab the other things from the first line
                    // and throw them in the database.
                    String[] line = nextLine.split(" ");

                    int usersRowCount = users.getRowCount();

                    users.addRow(usersRowCount + 1, line[1], line[2], client.getInetAddress().getHostAddress(), client.getPort(), line[3]);

                    // Read in the xml file line by line and add the information to the database.
                    // Start reading in the xml file:
                    nextLine = inFromClient.readLine();
                    File x = new File("fromClient.xml");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(x));
                    while (!nextLine.equals("EOF") || nextLine != null) {

                        bw.write(nextLine);

                        nextLine = inFromClient.readLine();
                    }

                    // Close the file writer
                    bw.close();


                    /* Parse the xml file and add the relevant data to the database
                     *
                     * Parsing stuff found at https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
                     * */

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document d = dBuilder.parse(x);

                    d.getDocumentElement().normalize();

                    //Debugging line
                    System.out.println("Root: " + d.getDocumentElement().getNodeName());

                    // The list of <file> tags
                    NodeList nList = d.getElementsByTagName("file");

                    //This loop will grab each file and add the relevant info to the database file.
                    for (int fileCount = 0; fileCount < nList.getLength(); fileCount++) {
                        Node file = nList.item(fileCount);
                        int filesRowCount = sharedFiles.getRowCount();
                        if(file.getNodeType() == Node.ELEMENT_NODE){
                            Element element = (Element) file;
                            String[] n = element.getElementsByTagName("name").item(0).getTextContent().split("/.");
                            sharedFiles.addRow(filesRowCount + 1, usersRowCount + 1, n[0], n[1],
                                    element.getElementsByTagName("description").item(0).getTextContent());

                        }
                    }

                } else if (nextLine.startsWith("key")){
                    // For the keyword search the first line should be:
                    // key <keyword>\n

                    String[] line = nextLine.split(" ");
                    ArrayList<String> fileList = new ArrayList<String>();
                    Column nameColumn = sharedFiles.getColumn("name");
                    Column descipColumn = sharedFiles.getColumn("description");

                    // Search through the file names for the keyword
                    IndexCursor filesCursor = CursorBuilder.createCursor(sharedFiles.getIndex("name"));
                    IndexCursor usersCursor = CursorBuilder.createCursor(users.getIndex("userID"));

                    for (Row r: filesCursor.newEntryIterable(line[1])) {
                        for (Row ur: usersCursor.newEntryIterable(r.get("userID"))){
                            fileList.add(ur.get("speed") + " " + ur.get("hostName") +"/" + ur.get("ipAddress") +
                                    " " + r.get("name") + r.get("type"));
                        }
                    }

                    // Search through the file description for the keyword
                    filesCursor = CursorBuilder.createCursor(sharedFiles.getIndex("description"));
                    usersCursor.reset();

                    for (Row r: filesCursor.newEntryIterable(line[1])) {
                        for (Row ur: usersCursor.newEntryIterable(r.get("userID"))){
                            fileList.add(ur.get("speed") + " " + ur.get("hostName") +"/" + ur.get("ipAddress") +
                                    " " + r.get("name") + r.get("type"));
                        }
                    }


                    // Send everything to the client that asked for it
                    for (String s:
                         fileList) {
                        outToClient.writeBytes(s + "\n");
                    }
                    outToClient.writeBytes("EOF\n");

                } else {
                    System.out.println("Unexpected start line.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
