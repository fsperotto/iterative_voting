package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.VotingSimulator;
import view.SimulatorViewForm;

/**
* MAIN CLASS OF THE SIMULATOR (VIEW).
* 
* This program executes a series of voting scenarios.
* The user can configure the number of voters, candidates, iterations and election methods
* The program structure follows the MVC architecture  
*  
* @author Filipo S. Perotto / Stéphane Airiau / Umberto Grandi
* @version 1.0
*/


public class Program {

	
	// inaccessible constructor
	private Program() {	}

	
	// program entry point
	public static void main(String[] args) {
		
		//Define Look and Feel
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}        
		
		
		//call on EDT
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				//Create Model
				VotingSimulator objSim = new VotingSimulator();
				objSim.initialize();
				
				//Create View
				SimulatorViewForm  objForm = new SimulatorViewForm(objSim);
				objForm.setVisible(true);
				
			}
			
		});
		
	}

}
