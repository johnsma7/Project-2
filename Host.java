import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

public class Host {
    String sentence;
	    String modifiedSentence = "";
	    boolean isOpen = true;
	    int number = 1;
	    boolean notEnd = true;
	    String statusCode;
	    boolean clientgo = true;
	    int port = 12000;
	    String serverName;
	    int connectPort;
        Socket ControlSocket = null;
        DataOutputStream outToServer;
        DataInputStream inFromServer;
        BufferedReader inFromUser;

    public static void main(String[] args){
        FTPClient client = new FTPClient();
        FTPServer server = new FTPServer();
        //possibly just start the GUI class
	
	    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    }

    //host connects to the main server taking in the hostname and port from the GUI
    //initiate connection
    public void initConnection(String server, String port){
        serverName = server;
        connectPort = port;
        ControlSocket = new Socket(serverName, connectPort);
        System.out.println("You are now connected to " + serverName);
        inFromUser= new BufferedReader(new InputStreamReader(System.in));//may not need
        outToServer = new DataOutputStream(ControlSocket.getOutputStream());
        inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));
    }

    //host sends username, hostname, and connection speed to the server
    //loops through files in the current directory and prompts user for description
    //adds to an array and send to server
    public void sendUsername(String user, String host, String connection){  //is it reachable
            connectPort = connectPort + 2;
            outToServer.writeBytes(user + " " + host + " " + connection + '\n');
            ServerSocket welcomeData = new ServerSocket(port);
            Socket dataSocket = welcomeData.accept();

            BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

            System.out.println("");
            System.out.println("Files on server:");

            //prints out the list of files available from the server.
            modifiedSentence = inData.readLine();

            String[] list = modifiedSentence.split(" ");

            for (String s : list
            ) {
                System.out.println(s);
            }

            System.out.println("(End of files)");
            welcomeData.close();
            dataSocket.close();
    }
    

    //searches for keyword in description

    //connects to the the other host server listed in the table

    //disconnects with the server, sends QUIT command to server
}
