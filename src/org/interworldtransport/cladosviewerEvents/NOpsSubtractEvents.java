/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosviewer.NOpsSubtractEvents<br>
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
 * ---org.interworldtransport.cladosviewer.NOpsSubtractEvents<br>
 * ------------------------------------------------------------------------ <br>
 */

package org.interworldtransport.cladosviewerEvents;

import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosGExceptions.*;

import org.interworldtransport.cladosviewer.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 *org.interworldtransport.cladosviewerr.COpsSubtractEvents This class manages
 * events relating to a complex operation. Subtract from this Monad another
 * Monad.
 *
 * @version 0.85
 * @author Dr Alfred W Differ
 */
public class NOpsSubtractEvents implements ActionListener {
	protected JMenuItem _control;
	protected NOpsParentEvents _parent;

	/**
	 * This is the default constructor.
	 * 
	 * @param pmniControlled JMenuItem This is a reference to the Menu Item for
	 *                       which this event acts.
	 * @param pParent        COpsEvents This is a reference to the NOpsParentEvents
	 *                       parent event handler
	 */
	public NOpsSubtractEvents(JMenuItem pmniControlled, NOpsParentEvents pParent) {
		_control = pmniControlled;
		_control.addActionListener(this);
		_parent = pParent;
	}

	/**
	 * This is the actual action to be performed by this member of the menu.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		int indxNydPnlSlctd = _parent._GUI.appGeometryView.getPaneFocus(); //NyadPane in focus
		if (indxNydPnlSlctd < 0 | indxNydPnlSlctd == _parent._GUI.appGeometryView.getNyadListSize() - 1) {
			ErrorDialog.show("Subtract needs a nyad in focus.\nNothing done.", "Need Nyad In Focus");
			return; // No focus? Stop. Go home.
		}

		NyadPanel tSpot = _parent._GUI.appGeometryView.getNyadPanel(indxNydPnlSlctd);
		NyadPanel tSpotPlus = _parent._GUI.appGeometryView.getNyadPanel(indxNydPnlSlctd + 1);

		int indxMndPnlSlctd = tSpot.getPaneFocus(); //MonadPane in focus on focused NyadPanel
		if (indxMndPnlSlctd < 0 | indxNydPnlSlctd > tSpotPlus.getMonadListSize()) {
			ErrorDialog.show("Subtraction needs two monads at the same index in a nyad.\nNothing done.",
					"Monads In Focus Issue");
			return; // No focus? Stop. Go home.
		}

		MonadPanel temp0 = tSpot.getMonadPanel(indxMndPnlSlctd);
		MonadPanel temp1 = tSpotPlus.getMonadPanel(indxMndPnlSlctd);

		try {
			switch (temp0.getRepMode()) {
			case REALF -> (temp0.getMonadRF()).subtract(temp1.getMonadRF());
			case REALD -> (temp0.getMonadRD()).subtract(temp1.getMonadRD());
			case COMPLEXF -> (temp0.getMonadCF()).subtract(temp1.getMonadCF());
			case COMPLEXD -> (temp0.getMonadCD()).subtract(temp1.getMonadCD());
			}
			temp0.setCoefficientDisplay();
		} catch (FieldBinaryException eb) {
			ErrorDialog.show(
					"Field Binary error between second and first monads.\nNothing done.\n" + eb.getSourceMessage(),
					"Field Binary Exception");
		} catch (CladosMonadException e) {
			ErrorDialog.show(
					"Reference Match error between second and first monads.\nNothing done.\n" + e.getSourceMessage(),
					"Clados Monad Exception");
		}
	}
}