package hw9;

import java.awt.event.*;

//A small mouse listener class
public class Mouse_Listener implements MouseListener {
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	//If the mouse is hovering over a location 
	public void mouseEntered(MouseEvent e) {	
		
		//If the mouse is hovering over a SmartButton, tell Control
		if ((e.getComponent() instanceof SmartButton))
		RPICampusPathsMain.locationHovered(((SmartButton)e.getComponent()).ID);
	}
	
	//If the mouse is newly not hovering over a button, tell Control
	public void mouseExited(MouseEvent e) {
		RPICampusPathsMain.locationHovered(-1);
	}
}