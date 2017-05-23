package ZehraVlad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * 
 * @author Zehra Punjwani and Vlad Niculescu
 *
 */
public class Communication {
	
	private Camera c = new Camera();
	private JFrame window;
	private JButton jbSend;
	private JTextArea taCenter;
	private JTextField tfSouth;
	private Hashtable<String,String> hextable;
	private ArrayList <String> csvresult;
	private String[] temp;
	private ArrayList<Character> toChars;
	private ArrayList<Integer> ascii;
	private ArrayList<String> hexlist;
	private HashMap<String, Integer> hashMap;
	private ArrayList<Integer> integerList;

	/**
	 * 
	 * @return void
	 * 
	 */
	//Calls the rest of the methods, creates the widget, the ButtonListener and the HashMap
	public Communication(){
		addWidgets();
		ButtonListener();
		hashMap();
	}
	
	/**
	 * 
	 * @return void
	 * 
	 */
	//Creating the entire GUI
	public void addWidgets(){
		window = new JFrame();
		window.setTitle("Matthew Damon on Mars");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jbSend = new JButton("Send");
		
		window.setLayout(new BorderLayout());
		
		JPanel jpNorth = new JPanel();
		window.add(jpNorth, BorderLayout.NORTH);
		
		taCenter = new JTextArea();
		window.add(taCenter, BorderLayout.CENTER);
        taCenter.setLineWrap(true);
        JScrollPane taCenterScroll = new JScrollPane(taCenter);
        window.getContentPane().add(taCenterScroll);
		
        tfSouth = new JTextField();
        window.add(tfSouth, BorderLayout.SOUTH);
        
		JPanel jpSouth = new JPanel();
		window.add(jpSouth, BorderLayout.SOUTH);
		
		jpNorth.setLayout(new GridLayout(1, 2));
		jpNorth.add(c.getSlider());
		
		jpSouth.setLayout(new GridLayout(2, 2));
		jpSouth.add(tfSouth);
		jpSouth.add(jbSend);
		
		jpNorth.setBorder(BorderFactory.createLineBorder(Color.black));
		taCenter.setBorder(BorderFactory.createLineBorder(Color.black));
		tfSouth.setBorder(BorderFactory.createLineBorder(Color.black));
		jpSouth.setBorder(BorderFactory.createLineBorder(Color.black));
		
		window.setMinimumSize(new Dimension(500,400));
		window.pack();	
		window.setVisible(true);
	}
	
	/**
	 * 
	 * @return void
	 * 
	 */
	
	public void ButtonListener(){
		// button action listener
		jbSend.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				//Create an instance of a new thread, in order to run the slider simulataneously with the main Thread
				new Thread(new Runnable(){
					public void run(){
						//Validation Checks, check if there are any inputs in the textArea and textField, if input found...
						if (!(taCenter.getText().length() <= 0)){
							if (tfSouth.getText().length() > 0){
								
								//try....
								try {
									window.setTitle("Sending...");
									getCSV(); //Getting content from CSV file.
									getHashTablefromCSV(csvresult); // Parsing the ASCII value row and HEX row from CSV into HashTable
									toChars(); // Converting the input from string to a list of characters
									taCenter.setText("");
									convertChartoAscii(toChars); //Converting each char to ASCII value.
									window.setTitle("Matthew Damon on Mars");
									JOptionPane.showMessageDialog(taCenter, "Message Sucessfully Sent!");
								
								//Catch FileNotFoundException...
								} catch (FileNotFoundException e) {
									JOptionPane.showMessageDialog(taCenter, "File Not Found!");
								}
							}
							else{
								JOptionPane.showMessageDialog(taCenter, "Data and File Directory MUST be entered!");
							}
						}
						else{
							JOptionPane.showMessageDialog(taCenter, "Data and File Directory MUST be entered!");
						}
					}
				}).start();
			}	
		});
	}
	
	
	/**
	 * 
	 * @return void
	 * 
	 */
	public  void sendIntegersToCamera(){ //Moving the slider
		c.moveSlider(hexToInt());
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getInput(){// Gets the input from the text field
		String getInput = taCenter.getText();
		return getInput;
	}
	
	/**
	 * 
	 * @return boolean
	 * @throws FileNotFoundException
	 */
	public boolean getCSV() throws FileNotFoundException{ //Gets the CSV file and it parses line by line in an ArrayList
		File file = new File(tfSouth.getText());
		csvresult = new ArrayList<String>();
		Scanner in = new Scanner(file);
		in.useDelimiter("\n");
		while(in.hasNext()){
			csvresult.add(in.next());
		}
		return true;
	}
	
	/**
	 * 
	 * @return void
	 * @param csvresult
	 */
	public void getHashTablefromCSV(ArrayList<String> csvresult){ //Getting from the CSV result the row containing the ASCII values and the one containing HEX values.
																	// and storing them into a hashtable.
		hextable = new Hashtable<String,String>();
		for(int i = 0; i < csvresult.size(); i++){
			temp = csvresult.get(i).split(",");
			hextable.put(temp[0],temp[2]);
		}
	}
	
	/**
	 * 
	 * @return void
	 * @param toChar
	 */
	public void convertChartoAscii(ArrayList<Character> toChar){ //Converting each char from the input to the ascii corespondant value.
		ascii = new ArrayList<Integer>();
		for(int i = 0; i < toChars.size(); i++){
			ascii.add((int)toChars.get(i));
		}
		generateHexList(ascii);
	}
	
	/**
	 * 
	 * @return void
	 * @param ascii
	 */
	public void generateHexList(ArrayList<Integer> ascii){ //Getting the correspondant hexcode to each char using the HashTable generated from the CSV file.
		hexlist = new ArrayList<String>();
		for(int i=0;i<ascii.size();i++){
			hexlist.add(hextable.get(String.valueOf(ascii.get(i))));
		}
		
		sendIntegersToCamera(); // sending the integers to the camera and moving the slider.
	}
	
	/**
	 * 
	 * @return hexList
	 */
	//abtain the hexList
	public ArrayList<String> getHexList(){
		return hexlist;
	}
	
	/**
	 * 
	 * @return void
	 * 
	 */
	//Print the hexTable when requried. 
	public void printTable(){
		System.out.println(hextable);
	}
	
	/**
	 * 
	 * @return void
	 * 
	 */
	public void toChars(){ //Gets every character from the inputed string.
		toChars = new ArrayList<Character>();
		for(int i = 0; i < getInput().length(); i++){
			toChars.add(getInput().charAt(i));
		}
	}

	/**
	 * 
	 * @return ArrayList<Integer> integerList
	 */
	public ArrayList<Integer> hexToInt(){ //Converts the HEX code retrieved into the corespondent set of integers.
		integerList = new ArrayList<Integer>();
		for(String i : getHexList()){
			String[] parts = i.split("");
			for(int j = 0; j < parts.length; j++){
				integerList.add(hashMap.get((parts[j])));
			}
		}
		return integerList;
	}
	
	/**
	 * 
	 * @return void
	 * 
	 */
	public void hashMap(){ //Generating a hashtable for the hexvalues.
		//We store the sliderLabeling in a HashMap instead of in an ArrayList to allow for efficient lookup of the frequency of each type of slider Position
		hashMap = new HashMap<String, Integer>();
		//Iterate through the loop for numbers 0 to 9, inorder to avoid multiple lines of code. 
		for(int i=0;i<10;i++){
			//put the integer into the hasMap.
		   hashMap.put(Integer.toString(i),i);
		}
		//Put A to F hash values and the equivalent key into the the hashMap
		hashMap.put("A", 10);
		hashMap.put("B", 11);
		hashMap.put("C", 12);
		hashMap.put("D", 13);
		hashMap.put("E", 14);
		hashMap.put("F", 15);
	}
}
