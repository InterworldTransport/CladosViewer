/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosviewer.MOpsLocalDualEvents<br>
 * -------------------------------------------------------------------- <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<p>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <p> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<p> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosviewer.MOpsLocalDualEvents<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosviewerEvents;

import com.interworldtransport.cladosviewer.MonadPanel;
import com.interworldtransport.cladosviewer.NyadPanel;
import com.interworldtransport.cladosviewer.ErrorDialog;

import java.awt.event.*;
import javax.swing.*;

/** 
 *  This class manages events relating to a simple operation...
 *  Take the dual of this Monad.
 *
 * @version 0.85
 * @author Dr Alfred W Differ
 */
public class MOpsLocalDualEvents implements ActionListener
{
    protected JMenuItem 		_control;
    protected MOpsParentEvents 	_parent;

/** 
 * This is the default constructor.
 * @param pmniControlled
 *  JMenuItem
 * This is a reference to the Menu Item for which this event acts.
 * @param pParent
 * 	NOpsParentEvents
 * This is a reference to the NOpsParentEvents parent event handler
 */
    public MOpsLocalDualEvents(	JMenuItem pmniControlled,
								MOpsParentEvents pParent)
    {
		_control=pmniControlled;
		_control.addActionListener(this);
		_parent=pParent;
    }

/** 
 * This is the actual action to be performed by this member of the menu.
 * The monad with focus has is multiplied by the PS on the right or left. 
 * 
 * A future version of the method must use the PS represented in the
 * reference frame instead. Fourier decomposition is done against that frame 
 * and not the canonical one most of the time.
 */
    public void actionPerformed(ActionEvent evt)
    {
    	String command = evt.getActionCommand();
    	
    	int indexNyadPanelSelected = _parent._GUI.appGeometryView.getPaneFocus();
    	if (indexNyadPanelSelected<0) 
    	{
    		ErrorDialog.show("No nyad in the focus.\nNothing done.", "Need Nyad In Focus");
    		return;	
    	}
    	
    	NyadPanel tNSpotPnl = _parent._GUI.appGeometryView.getNyadPanel(indexNyadPanelSelected);
    	int indxMndPnlSlctd = tNSpotPnl.getPaneFocus();
    	if (indxMndPnlSlctd<0) 
    	{
    		ErrorDialog.show("Dual Operation needs one monad in focus.\nNothing done.", "Need Monad In Focus");
    		return;
    	}
    	
    	MonadPanel tMSpotPnl=tNSpotPnl.getMonadPanel(tNSpotPnl.getPaneFocus());
    	
    	
    	if (command.equals("dual>"))
    	{
        	switch (tMSpotPnl.getRepMode())
        	{
    	    	case REALF: 	tMSpotPnl.getMonadRF().dualLeft();
    							    	break;
    	    	case REALD: 	tMSpotPnl.getMonadRD().dualLeft();
    							    	break;
    	    	case COMPLEXF:	tMSpotPnl.getMonadCF().dualLeft();
    							    	break;
    	    	case COMPLEXD:	tMSpotPnl.getMonadCD().dualLeft();
        	}
    		_parent._GUI.appStatusBar.setStatusMsg("-->Selected monad has been 'dualed' from the left.\n");
    	}
    	if (command.equals("<dual"))
    	{
    		switch (tMSpotPnl.getRepMode())
        	{
    	    	case REALF: 	tMSpotPnl.getMonadRF().dualRight();
    							    	break;
    	    	case REALD: 	tMSpotPnl.getMonadRD().dualRight();
    							    	break;
    	    	case COMPLEXF:	tMSpotPnl.getMonadCF().dualRight();
    							    	break;
    	    	case COMPLEXD:	tMSpotPnl.getMonadCD().dualRight();
    							    	break;
        	}
    		_parent._GUI.appStatusBar.setStatusMsg("-->Selected monad has been 'dualed' from the right.\n");
    	}
    	tMSpotPnl.setCoefficientDisplay();
    }
 }
