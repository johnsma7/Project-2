
import java.io.*;
import java.net.*;
import java.util.*;


class FTPServer {

    public static void main(String args[]) throws Exception{

	ServerSocket welcomeSocket;
	int port = 12000;

	try
	    {
		welcomeSocket  = new ServerSocket(port);
		while (true) {
		    Socket connectionSocket = welcomeSocket.accept();

		    System.out.println("\nNew client connected.");

		    ClientHandler handler = new ClientHandler(connectionSocket);

		    handler.start();
		}
	    }
	catch (IOException ioEX)
	    {
		System.out.println("\nError in setting up port.");
		System.exit(1);
	    }

    }
}

class ClientHandler extends Thread
{
    private DataOutputStream outToClient;
    private BufferedReader inFromClient;
    private String fromClient;
    private String clientCommand;
    private String clientFileName = "";
    private byte[] data;
    private String frstln;
    private Boolean closed = false;
    private Socket client;
    private int port;
    private Socket dataSocket;
	
    public ClientHandler(Socket socket) {

	try
	    {
		client = socket;
		outToClient = new DataOutputStream(client.getOutputStream());
		inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		port = client.getPort();
	    }
	catch (IOException ioEx)
	    {
		ioEx.printStackTrace();
		}
	}
		
    public void run() {
	try
	    {

            File curDir = new File(".");
            File[] fileList = curDir.listFiles();
		do
		    {
			fromClient = inFromClient.readLine();
			StringTokenizer tokens = new StringTokenizer(fromClient);
			frstln = tokens.nextToken();
			port = Integer.parseInt(frstln);
			clientCommand = tokens.nextToken();
			BufferedReader dataInFromClient;

            if (clientCommand.equals("list")) {
                
				try {
                    dataSocket = new Socket(client.getInetAddress(), port);
                    DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                    //list everything in the current directory and send to client
                    String fileNames = "";
                    if (fileList != null) {

                        for (File f : fileList) {
                            if (f.exists()) {
                                fileNames = fileNames + f.getName() + " ";
                                // System.out.println(f.getName());//Debugging line, remove later
                            }
                        }
                    } else {
                        System.out.println("file list null");
                    }

                    dataOutToClient.writeBytes(fileNames);
                    dataSocket.close();
                } catch (IOException ioEx) {
                    System.out.println("Unable to set up port!131");
                }

                // System.out.println("Data Socket closed");
				
			}
			

			else if (clientCommand.equals("retr")) {
			    
				try {
                    // System.out.println("Start of retr." + "\n" + fromClient);//debugging line, remove later
                    dataSocket = new Socket(client.getInetAddress(), port);
                    DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                    //  System.out.println(dataSocket.isConnected());

                    //Read in the file name from the client.
                    String fileName = tokens.nextToken();
                    // System.out.println(fileName);//debugging line, remove later
                    boolean exists = false;

                    // System.out.println(dataSocket.isConnected());

                    try {
                        for (File f : fileList) {
                            if (f.getName().equals(fileName)) {
                                exists = true;
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("There are no files in the current directory.\n If you see this something is very wrong.");
                        e.printStackTrace();
                    }

                    if (exists) {
                        outToClient.writeBytes("200 OK\n");
                        // System.out.println("Sent 200 OK"); //Debugging line

                        File f = new File(fileName);
                        BufferedReader input = new BufferedReader(new FileReader(f));

                        try {
                            String fileLine = input.readLine();
                            // System.out.println(fileLine);
                            while (fileLine != null) {
                                dataOutToClient.writeBytes(fileLine + "\n");
                                fileLine = input.readLine();
                                //  System.out.println(fileLine);
                            }
                        } catch (NoSuchElementException e) {
                            System.out.println("Empty line reached.");//debugging line, remove later
                            dataOutToClient.writeBytes("EOF");
                        }
                        dataOutToClient.writeBytes("EOF");
                    } else {
                        outToClient.writeBytes("550\n");
                        System.out.println("Sent 550, file doesn't exist");//debugging line
                    }
                } catch (IOException e) {
                    System.out.println("IOException for retr:");
                    e.printStackTrace();

                }

                try {
                    // System.out.println("Closing connection...");
                    dataSocket.close();
                    // System.out.println("Connection closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
				
			} 
			
			else if (clientCommand.equals("stor")){

				//saves the file to the current directory.
                try {
                    dataSocket = new Socket(client.getInetAddress(), port);
                    dataInFromClient = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

                    //  System.out.println(dataSocket.isConnected());

                    String fileName = tokens.nextToken();

                    // System.out.println(fileName);

                    String newFileName = fileName.replaceFirst("[.][^.]+$", "");
                    File f = new File(newFileName + 1 + ".txt");

                    //File f = new File(fileName);
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));

                    String fileLine = dataInFromClient.readLine();

                    while (fileLine != null) {
                        bw.write(fileLine + "\n");
                        fileLine = dataInFromClient.readLine();
                    }
                    dataSocket.close();
                    bw.close();

                } catch (IOException e) {
                    System.out.println("Port couldn't be made.");
                }
			
			}


		
		    } while (!clientCommand.equals("quit"));
	    }
	catch(IOException ioEx)
	    {
		ioEx.printStackTrace();
	    }
	try
	    {
		if (client != null)
		    {
			String inetadd = "" + client.getInetAddress();
			System.out.println("\n" + inetadd.substring(1) + " has disconnected");
			client.close();
		    }
	    }
	catch (IOException ioEX)
	    {
		System.out.println("Unable to disconnect.");
	    }
    }

}
