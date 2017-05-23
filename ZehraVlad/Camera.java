package ZehraVlad;

import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Zehra Punjwani and Vlad Niculescu
 *
 */
@SuppressWarnings("serial")
public class Camera extends JSlider {
	private JSlider cam;
	private Hashtable<Integer, JLabel> hash;
	
	//Call multiple methods in this Camera() method. allows for structuring of the programme.
	public Camera(){
		creatingSlider();
	    sliderLabeling();
	  }
	
	/**
	 * 
	 * @return void
	 * 
	 */
	//Creating the Slider 
	public void creatingSlider(){
		cam = new JSlider(JSlider.HORIZONTAL,0,15,1);
		cam.setMinorTickSpacing(20); //Spcaing between the slider minor and major ticks, making the slider for graphically presentable.
	    cam.setMajorTickSpacing(1);
	    cam.setPaintTicks(true);
	    cam.setPaintLabels(true);
	    cam.setBorder(new EmptyBorder(5,20,5,20));
	    cam.setValue(0);
	}
	
	/**
	 * 
	 * @return void
	 * 
	 */
	////We create the sliderlabeling using Hashtable, this allows for easy lookup and cross referencing when converting text to 
	//hexadecimals and vice versa.
	public void sliderLabeling(){
		//Create and new instance of the hashtable called hash
		hash = new Hashtable<Integer, JLabel>();
		//A for loop, that iterates through integers, and creates the slider labeling for numbers 0 to 9. 
		//Using the loop avoids repetation and makes the code more efficient.
		for(int i=0;i<10;i++){
		   hash.put(i, new JLabel(Integer.toString(i)));
		}
		hash.put(10, new JLabel("A"));
		hash.put(11, new JLabel("B"));
		hash.put(12, new JLabel("C"));
		hash.put(13, new JLabel("D"));
		hash.put(14, new JLabel("E"));
		hash.put(15, new JLabel("F"));
		   
		cam.setLabelTable(hash);
	}
	
	/**
	*
	*@return Hashtable<Integer, JLabel> hash
	*
	*/
	public Hashtable<Integer, JLabel> getHashTable(){
		return hash;
	}
	
	/**
	*
	*@return void
	*@param ArrayList<Integer> input
	*
	*/
	//try and Catch in the case of an InterruptedException. 
	public void moveSlider(ArrayList<Integer> input) {
		//try,
		try{
			//while i type Integer in input
			for(Integer i : input){
				//SetValue of cam to i
				cam.setValue(i);
				//Sleep the Thread for 1000ms between each loop (Slider Movement)
				Thread.sleep(1000);
			}
		//Catch the InterruptedException in the case of incorrect data Type.
		}catch(InterruptedException e){
			System.out.println("Please use integers");			
		}				
	}
	
	/**
	*
	*@return JSlider cam
	*
	*/
	//getSlider() method to obtain the Camera - Slider
	public JSlider getSlider(){
		return cam;
	}
}
