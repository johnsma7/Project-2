import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class ServerGUI extends JFrame implements ActionListener{
	private JButton connect;
	private JButton go;	
	private JTextField serverHN;
	private JTextField PortNum;	
	private JTextField usernym;
	private JTextField hostnym;
	private JTextField keywrd;
	private JTextField command;
	private JComboBox<String> speedy;
	private JScrollPane options;
	private JTextArea textArea;

	String[] choices = { "Ethernet","CHOICE 2", "CHOICE 3","CHOICE 4","CHOICE 5","CHOICE 6"};
	String[] header = {"speed","hostname","filename"};
	Object[][] data = {
			{"speed","hostname","filename"},
		    {"T1","DCCLIENT9.dcc9.local/148.61.112.42","Abhay.txt"},
		    {"Ethernet","DCCLIENT/148.61.112.49","Vinay.txt"}
		};
    public static void main(String args[]){
       ServerGUI gui = new ServerGUI();

       gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       gui.setTitle("GV-NAPSTER Host");
       gui.setSize(700,500);
       gui.pack();		
       gui.setVisible(true);
    }
    public ServerGUI(){
		setLayout(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        // Place label for input information
        position.gridx = 0;
        position.gridy = 2;  
        add(new JLabel("Source Hostname   "),position);
        
        // Place label for next person
        position.gridx = 5;
        position.gridy = 2;  
        add(new JLabel("   Port   "),position);
        
        // Place label for average seconds for PortNum
        position.gridx = 0;
        position.gridy = 3;  
        add(new JLabel("Username"),position);
        
        // Place label for total time
        position.gridx = 5;
        position.gridy = 3;  
        add(new JLabel("  Hostname  "),position);
        
        
        //Place label for average seconds per eatery
        position.gridx = 7;
        position.gridy = 3;  
        add(new JLabel("Speed"),position);
        
        // Place label for seconds
        position.gridx = 0;
        position.gridy = 5;  
        add(new JLabel("Keyword"),position);
        
        // Place label for number of eateries
        position.gridx = 0;
        position.gridy = 8;  
        add(new JLabel("Enter Command"),position);

        // Place label for output information
        position.gridx = 0;
        position.gridy = 0;  
        add(new JLabel("CONNECTION:"),position);
        
        // Place label for throughput
        position.gridx = 0;
        position.gridy = 4;  
        add(new JLabel("SEARCH:"),position);

        // Place label for total time
        position.gridx = 0;
        position.gridy = 7;  
        add(new JLabel("FTP:"),position);
  
        // Place button for connect
        connect = new JButton( "Connect" );
        position.gridx = 7;
        position.gridy = 2;   
        add(connect, position);
        connect.addActionListener(this);
        
        // Place button for go
        go = new JButton( "Go" );
        position.gridx = 8;
        position.gridy = 8;   
        add(go, position);
        go.addActionListener(this);
        
     // Place button for go
        go = new JButton( "Search" );
        position.gridx = 5;
        position.gridy = 5;   
        add(go, position);
        go.addActionListener(this);
        
        speedy = new JComboBox<String>(choices);
        position.gridx = 8;
        position.gridy = 3;   
        add(speedy, position);
        speedy.addActionListener(this);
       
       JTable table = new JTable(data, header);
       System.out.println("hello");
        position.gridx = 0;
        position.gridy = 6;  
        position.gridwidth = 5;
        add(table, position);
        
        // Place text for next person
        serverHN = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 4;
        position.gridy = 2;   
        add(serverHN, position);
        serverHN.addActionListener(this);
        
        // Place text for PortNum
        PortNum = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 6;
        position.gridy = 2;   
        add(PortNum, position);
        PortNum.addActionListener(this);
        
        // Place text for total time
        usernym = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 4;
        position.gridy = 3;   
        add(usernym, position);
        usernym.addActionListener(this);
        
        // Place text for the time at the eatery
        hostnym = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 6;
        position.gridy = 3;   
        add(hostnym, position);
        hostnym.addActionListener(this);
        
        // Place text for when the person leaves
        keywrd = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 4;
        position.gridy = 5;   
        add(keywrd, position);
        keywrd.addActionListener(this);
        
        // Place text for the number of eateries
        command = new JTextField(40);
        position.gridwidth = 9;
        position.gridx = 4;
        position.gridy = 8;   
        add(command, position);
        command.addActionListener(this);
        
        textArea = new JTextArea(8,65); 
        position.gridwidth = 9;
        position.gridx = 0;
        position.gridy = 9;   
        add(textArea, position);
        
     
      
    //    JScrollPane options = new JScrollPane(textArea);
       // table.setFillsViewportHeight(true);
        
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
    

}