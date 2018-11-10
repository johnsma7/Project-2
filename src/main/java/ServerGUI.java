import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class ServerGUI extends JFrame implements ActionListener{
	private JButton connect;
	private JButton go;
	private JButton search;	
	private JTextField serverHN;
	private JTextField portNum;	
	private JTextField userNm;
	private JTextField hostNm;
	private JTextField keyWrd;
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
        
     // Place button for search
        search = new JButton( "Search" );
        position.gridx = 5;
        position.gridy = 5;   
        add(search, position);
        search.addActionListener(this);
       
        //my boi speedy	
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
        
        // Place text for portNum
        portNum = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 6;
        position.gridy = 2;   
        add(portNum, position);
        portNum.addActionListener(this);
        
        // Place text for userNm
        userNm = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 4;
        position.gridy = 3;   
        add(userNm, position);
        userNm.addActionListener(this);
        
        // Place text for hostNm
        hostNm = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 6;
        position.gridy = 3;   
        add(hostNm, position);
        hostNm.addActionListener(this);
        
        // Place text for keyWrd
        keyWrd = new JTextField(15);
        position.gridwidth = 1;
        position.gridx = 4;
        position.gridy = 5;   
        add(keyWrd, position);
        keyWrd.addActionListener(this);
        
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object which = e.getSource();
		if(which == connect){
			if(serverHN.getText().equals("")||portNum.getText().equals("")||userNm.getText().equals("")||hostNm.getText().equals("")){
				System.out.println("error");
			}else{
				System.out.println("connecting...");
				System.out.println(serverHN.getText());
				System.out.println(portNum.getText());
				System.out.println(userNm.getText());
				System.out.println(hostNm.getText());
			}
		}
		if(which == search){
			if(!keyWrd.getText().equals("")){
				System.out.println("searching...");
				System.out.println(keyWrd.getText());
			}else{
				System.out.println("error");
			}
		}
		if(which == speedy){
			System.out.println("scoot scoot");
			System.out.println(speedy.getSelectedItem());
		}
		if(which == go){
			if(!command.getText().equals("")){
				System.out.println("commanding...");
				System.out.println(command.getText());
			}else{
				System.out.println("error");
			}
		}
		
		
	}
    

}
