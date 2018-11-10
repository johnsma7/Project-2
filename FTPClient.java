import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
class FTPClient {

    public static void main(String argv[]) throws Exception {
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
	
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("To connect to server, enter \"connect\" followed by the server's IP and port.");

	sentence = inFromUser.readLine();
	StringTokenizer tokens = new StringTokenizer(sentence);

	if(sentence.startsWith("connect ")) {
	    serverName = tokens.nextToken(); //passes connect command
	    serverName = tokens.nextToken();
	    connectPort = Integer.parseInt(tokens.nextToken());

	    try {
            ControlSocket = new Socket(serverName, connectPort);

            System.out.println("You are now connected to " + serverName);

            do {
                System.out.println("\nWhat would you like to do? \n list || retr: file.txt ||stor: file.txt  || quit");
                DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());

                DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));

                sentence = inFromUser.readLine();

                if (sentence.equals("list")) {
                    port = port + 2;
                    outToServer.writeBytes(port + " " + sentence + " " + '\n');
                    ServerSocket welcomeData = new ServerSocket(port);
                    Socket dataSocket = welcomeData.accept();

                    BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

                    System.out.println("");
                    System.out.println("Files on server:");

                    //prints out the list of files available from the server.
                    modifiedSentence = inData.readLine();//readUTF

                    String[] list = modifiedSentence.split(" ");

                    for (String s : list
                    ) {
                        System.out.println(s);
                    }

                    System.out.println("(End of files)");
                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || quit: ");


                } else if (sentence.startsWith("retr ")) {

                    port = port + 2;
                    //If the user wants to retrieve a file from the server.
                    System.out.println("Downloading file from server ...");
                    outToServer.writeBytes(port + " " + sentence + " " + '\n');
                    ServerSocket welcomeData = new ServerSocket(port);
                    Socket dataSocket = welcomeData.accept();

                    BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

                    BufferedWriter bw;// This is to write the file.

                    //Get the file name for use on the client side
                    String[] s = sentence.split(" ");
                    String fileName = s[1];

                    statusCode = inFromServer.readLine();

                    if (statusCode.equals("200 OK")) {
                        System.out.println("File Downloaded!");

                        /*
                        These lines are for testing when the client and server are in the same directory.*/
                        String newFileName = fileName.replaceFirst("[.][^.]+$", "");
                        File f = new File(newFileName + 1 + ".txt");

                        //File f = new File(fileName);
                        bw = new BufferedWriter(new FileWriter(f));

                        String fileLine = inData.readLine();

                        if (fileLine != null) {
                            while (!fileLine.equals("EOF")) {
                                bw.write(fileLine + "\n");
                                fileLine = inData.readLine();

                            }
                            dataSocket.close();
                            welcomeData.close();
                            bw.close();
                        }

                    } else if (statusCode.equals("550")) {
                        dataSocket.close();
                        welcomeData.close();
                    } else {
                        System.out.println("Something has gone wrong..." + statusCode);
                        dataSocket.close();
                        welcomeData.close();
                    }
                    System.out.println("\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || quit: ");


                } else if (sentence.startsWith("stor ")) {

                    port = port + 2;
                    //If the user wants to store a file on the server

                    String[] cList = sentence.split(" ");
                    String fileName = cList[1];

                    File curDir = new File(".");
                    File[] fileList = curDir.listFiles();
                    boolean exists = false;

                    for (File f : fileList) {
                        if (f.getName().equals(fileName)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("");
                        System.out.println("Uploading file to server...");
                        File f = new File(fileName);
                        BufferedReader br = new BufferedReader(new FileReader(f));

                        outToServer.writeBytes(port + " " + sentence + " " + '\n');
                        ServerSocket welcomeData = new ServerSocket(port);
                        Socket dataSocket = welcomeData.accept();
                        DataOutputStream dataOutToServer = new DataOutputStream(dataSocket.getOutputStream());

                        String fileLine = br.readLine();
                        while (fileLine != null) {

                            dataOutToServer.writeBytes(fileLine + "\n");
                            fileLine = br.readLine();
                        }

                        dataSocket.close();
                        welcomeData.close();
                        br.close();

                        System.out.println("File uploaded!");

                    }
                } else if (sentence.equals("quit")) {
                    System.out.print("qutting");
                    outToServer.writeBytes(port + " " + sentence + " " + '\n');
                    isOpen = false;
                }
            } while (isOpen);
        }
	    catch (IOException ioEx)
		{
		    ioEx.printStackTrace();
		}
	    finally
		{
		    try
			{
			    System.out.println("\nClosing connection...");
			    ControlSocket.close();
			}
		    catch (IOException ioEx)
			{
			    System.out.println("Unable to disconnect.");
			    System.exit(1);
			}
		}

	} else {
	    System.out.println("Must enter \"connect\" to connect to server.");
	    System.exit(1);
	}
    }
}
