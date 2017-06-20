package view.components;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JSplitPane;

public class JAccordionPanel extends JSplitPane {

	private static final long serialVersionUID = 1L;

	private ArrayList<JSplitPane> listSplitPanels = new ArrayList<JSplitPane>();
	
	private final int iNumPanels;
	
	public JAccordionPanel(int iNumPanels) {
		
		this.iNumPanels = iNumPanels;
		
		listSplitPanels.add(this);

		this.setOneTouchExpandable(true);
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);

		double dWeight = 1.0 / iNumPanels;
		//double dWeight = 0.5;

		this.setResizeWeight(dWeight);
		
		JSplitPane pnlBefore = this;
		
		for (int i=1; i<iNumPanels-1; i++) {

			
			JSplitPane pnlAfter = new JSplitPane();
			
			pnlAfter.setOneTouchExpandable(true);
			pnlAfter.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
			pnlAfter.setResizeWeight(dWeight);
			
			listSplitPanels.add(pnlAfter);
			pnlBefore.setBottomComponent(pnlAfter);
			
			pnlBefore = pnlAfter;
			
		}
		
	}
	
	
	public void set(Component comp, int iPanelIdx) {
		
		if ( (iPanelIdx > iNumPanels-1) || (iPanelIdx < 0) ) {
			
		} else if (iPanelIdx == iNumPanels-1) {

			listSplitPanels.get(iPanelIdx-1).setBottomComponent(comp);
			
		} else {

			listSplitPanels.get(iPanelIdx).setTopComponent(comp);
			
		}

		
	}
	
}
