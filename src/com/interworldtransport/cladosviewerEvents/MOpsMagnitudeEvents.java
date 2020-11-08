/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosviewer.MOpsMagnitudeEvents<br>
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
 * ---com.interworldtransport.cladosviewer.MOpsMagnitudeEvents<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosviewerEvents;

import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosF.ComplexF;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosF.RealF;
import com.interworldtransport.cladosGExceptions.CladosMonadException;
import com.interworldtransport.cladosviewer.MonadPanel;
import com.interworldtransport.cladosviewer.NyadPanel;
import com.interworldtransport.cladosviewer.ErrorDialog;

import java.awt.event.*;
import javax.swing.*;

/** 
 *  This class manages events relating to the answering of a simple question.
 *  What is the magnitude of this Monad?
 *
 * @version 0.85
 * @author Dr Alfred W Differ
 */
public class MOpsMagnitudeEvents implements ActionListener
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
    public MOpsMagnitudeEvents(	JMenuItem pmniControlled,
								MOpsParentEvents pParent)
    {
		_control=pmniControlled;
		_control.addActionListener(this);
		_parent=pParent;
    }

/** 
 * This is the actual action to be performed by this member of the menu.
 */
    public void actionPerformed(ActionEvent evt)
    {
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
    		ErrorDialog.show("Magnitude Discovery needs one monad in focus.\nNothing done.", "Need Monad In Focus");
    		return;
    	}
    	
    	MonadPanel tMSpotPnl=tNSpotPnl.getMonadPanel(indxMndPnlSlctd);
    	try 
    	{    		
    		switch (tMSpotPnl.getRepMode())
        	{
		    	case REALF: 	RealF scaleRF = tMSpotPnl.getMonadRF().magnitude();
							    		_parent._GUI.appFieldBar.setWhatFloatR(scaleRF.getModulus());
								    	break;
		    	case REALD: 	RealD scaleRD = tMSpotPnl.getMonadRD().magnitude();
							    		_parent._GUI.appFieldBar.setWhatDoubleR(scaleRD.getModulus());
								    	break;
		    	case COMPLEXF:	ComplexF scaleCF = tMSpotPnl.getMonadCF().magnitude();
							    		_parent._GUI.appFieldBar.setWhatFloatR(scaleCF.getModulus());
							    		_parent._GUI.appFieldBar.setWhatFloatI(0.0F);
								    	break;
		    	case COMPLEXD:	ComplexD scaleCD = tMSpotPnl.getMonadCD().magnitude();
							    		_parent._GUI.appFieldBar.setWhatDoubleR(scaleCD.getModulus());
							    		_parent._GUI.appFieldBar.setWhatDoubleI(0.0D);
        	}
    		_parent._GUI.appStatusBar.setStatusMsg("-->Selected monad magnitude has been computed.\n");
    	} 
    	catch (CladosMonadException e) 
    	{
    		ErrorDialog.show("Selected monad has an issue.\nNothing done.\n"+e.getSourceMessage(), "Clados Monad Exception");
    	}
    }
 }