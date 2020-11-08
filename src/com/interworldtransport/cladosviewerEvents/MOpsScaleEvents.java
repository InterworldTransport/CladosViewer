/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosviewer.MOpsScaleEvents<br>
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
 * ---com.interworldtransport.cladosviewer.MOpsScaleEvents<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosviewerEvents;

import com.interworldtransport.cladosF.CladosField;
import com.interworldtransport.cladosF.RealF;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosF.ComplexF;
import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosFExceptions.FieldBinaryException;

import com.interworldtransport.cladosG.MonadRealF;
import com.interworldtransport.cladosG.MonadRealD;
import com.interworldtransport.cladosG.MonadComplexF;
import com.interworldtransport.cladosG.MonadComplexD;

import com.interworldtransport.cladosG.AlgebraRealF;
import com.interworldtransport.cladosG.AlgebraRealD;
import com.interworldtransport.cladosG.AlgebraComplexF;
import com.interworldtransport.cladosG.AlgebraComplexD;

import com.interworldtransport.cladosviewer.MonadPanel;
import com.interworldtransport.cladosviewer.NyadPanel;
import com.interworldtransport.cladosviewer.ErrorDialog;

import java.awt.event.*;
import javax.swing.*;

/** com.interworldtransport.cladosviewer.SOpsScaleEvents
 *  This class manages events relating to a simple operation...
 *  Rescale this Monad.
 *
 * @version 0.85
 * @author Dr Alfred W Differ
 */
public class MOpsScaleEvents implements ActionListener
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
    public MOpsScaleEvents(	JMenuItem pmniControlled,
    						MOpsParentEvents pParent)
    {
		_control=pmniControlled;
		_control.addActionListener(this);
		_parent=pParent;
    }

/** 
 * This is the actual action to be performed by this member of the menu.
 * It 'scales' the focus monad in the sense of scalar multiplication with the field
 * suggested in the field bar. 
 * However, it only uses the values in the field bar. No DivFieldType matching occurs.
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
    		ErrorDialog.show("Scale Operation needs one monad in focus.\nNothing done.", "Need Monad In Focus");
    		return;
    	}
    	
	    MonadPanel tMSpotPnl=tNSpotPnl.getMonadPanel(tNSpotPnl.getPaneFocus());
	    
	    try
	    {
		    switch(tMSpotPnl.getRepMode())
	    	{
	    		case REALF:		MonadRealF tMonadRF = tMSpotPnl.getMonadRF();
	    						RealF tFieldRF = (RealF) CladosField.REALF.createZERO(AlgebraRealF.shareCardinal(tMonadRF.getAlgebra()));
				    			tFieldRF.setReal(Float.parseFloat(_parent._GUI.appFieldBar.getRealText()));
								tMonadRF.scale(tFieldRF);
								_parent._GUI.appStatusBar.setStatusMsg("\tmonad has been rescaled by ");
								_parent._GUI.appStatusBar.setStatusMsg(_parent._GUI.appFieldBar.getRealText()+"\n");
								break;
	    		case REALD:		MonadRealD tMonadRD = tMSpotPnl.getMonadRD();
								RealD tFieldRD = (RealD) CladosField.REALD.createZERO(AlgebraRealD.shareCardinal(tMonadRD.getAlgebra()));
								tFieldRD.setReal(Double.parseDouble(_parent._GUI.appFieldBar.getRealText()));
								tMonadRD.scale(tFieldRD);
								_parent._GUI.appStatusBar.setStatusMsg("\tmonad has been rescaled by ");
								_parent._GUI.appStatusBar.setStatusMsg(_parent._GUI.appFieldBar.getRealText()+"\n");
								break;
	    		case COMPLEXF:	MonadComplexF tMonadCF = tMSpotPnl.getMonadCF();
	    						ComplexF tFieldCF = (ComplexF) CladosField.COMPLEXF.createZERO(AlgebraComplexF.shareCardinal(tMonadCF.getAlgebra()));
	    						tFieldCF.setReal(Float.parseFloat(_parent._GUI.appFieldBar.getRealText()));
	    						tFieldCF.setImg(Float.parseFloat(_parent._GUI.appFieldBar.getImgText()));
								tMonadCF.scale(tFieldCF);
								_parent._GUI.appStatusBar.setStatusMsg("\tmonad has been rescaled by (R");
								_parent._GUI.appStatusBar.setStatusMsg(_parent._GUI.appFieldBar.getRealText()+", I");
								_parent._GUI.appStatusBar.setStatusMsg(_parent._GUI.appFieldBar.getImgText()+")\n");
								break;
	    		case COMPLEXD:	MonadComplexD tMonadCD = tMSpotPnl.getMonadCD();
					    		ComplexD tFieldCD = (ComplexD) CladosField.COMPLEXD.createZERO(AlgebraComplexD.shareCardinal(tMonadCD.getAlgebra()));
					    		tFieldCD.setReal(Double.parseDouble(_parent._GUI.appFieldBar.getRealText()));
					    		tFieldCD.setImg(Double.parseDouble(_parent._GUI.appFieldBar.getImgText()));
								tMonadCD.scale(tFieldCD);
								_parent._GUI.appStatusBar.setStatusMsg("\tmonad has been rescaled by (R");
								_parent._GUI.appStatusBar.setStatusMsg(_parent._GUI.appFieldBar.getRealText()+", I");
								_parent._GUI.appStatusBar.setStatusMsg(_parent._GUI.appFieldBar.getImgText()+")\n");
	    	}
		    tMSpotPnl.setCoefficientDisplay();
	    }
		catch (FieldBinaryException eb)
		{
			ErrorDialog.show("Monad has not been rescaled.\nNothing done.\n"+eb.getSourceMessage(), "Field Binary Exception");
		}
	    
    }
 }