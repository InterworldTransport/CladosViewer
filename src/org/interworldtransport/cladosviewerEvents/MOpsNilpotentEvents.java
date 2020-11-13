/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosviewer.MOpsNilpotentEvents<br>
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
 * ---org.interworldtransport.cladosviewer.MOpsNilpotentEvents<br>
 * ------------------------------------------------------------------------ <br>
 */

package org.interworldtransport.cladosviewerEvents;

import org.interworldtransport.cladosFExceptions.*;
import org.interworldtransport.cladosG.MonadComplexD;
import org.interworldtransport.cladosG.MonadComplexF;
import org.interworldtransport.cladosG.MonadRealD;
import org.interworldtransport.cladosG.MonadRealF;
import org.interworldtransport.cladosGExceptions.*;

import org.interworldtransport.cladosviewer.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

/**
 * This class manages events relating to the answering of a boolean question. Is
 * the selected monad nilpotent?
 *
 * @version 0.85
 * @author Dr Alfred W Differ
 */
public class MOpsNilpotentEvents implements ActionListener {
	protected JMenuItem _control;
	protected MOpsParentEvents _parent;

	/**
	 * This is the default constructor.
	 * 
	 * @param pmniControlled JMenuItem This is a reference to the Menu Item for
	 *                       which this event acts.
	 * @param pParent        NOpsParentEvents This is a reference to the
	 *                       NOpsParentEvents parent event handler
	 */
	public MOpsNilpotentEvents(JMenuItem pmniControlled, MOpsParentEvents pParent) {
		_control = pmniControlled;
		_control.addActionListener(this);
		_parent = pParent;
	}

	/**
	 * This is the actual action to be performed by this member of the menu. The
	 * monad with focus is tested to see if it is nilpotent.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		int indexNyadPanelSelected = _parent._GUI.appGeometryView.getPaneFocus();
		if (indexNyadPanelSelected < 0) {
			ErrorDialog.show("No nyad in the focus.\nNothing done.", "Need Nyad In Focus");
			return;
		}

		NyadPanel panelNyadSelected = _parent._GUI.appGeometryView.getNyadPanel(indexNyadPanelSelected);
		int indxMndPnlSlctd = panelNyadSelected.getPaneFocus();
		if (indxMndPnlSlctd < 0) {
			ErrorDialog.show("Nilpotent Test needs one monad in focus.\nNothing done.", "Need Monad In Focus");
			return;
		}

		MonadPanel tSpot = panelNyadSelected.getMonadPanel(indxMndPnlSlctd);

		try {
			int pow2Test = (int) Float.parseFloat(_parent._GUI.appFieldBar.getRealText());
			boolean test = switch (tSpot.getRepMode()) {
			case REALF -> MonadRealF.isNilpotent(tSpot.getMonadRF(), pow2Test);
			case REALD -> MonadRealD.isNilpotent(tSpot.getMonadRD(), pow2Test);
			case COMPLEXF -> MonadComplexF.isNilpotent(tSpot.getMonadCF(), pow2Test);
			case COMPLEXD -> MonadComplexD.isNilpotent(tSpot.getMonadCD(), pow2Test);
			};
			if (test)
				_parent._GUI.appStatusBar.setStatusMsg("-->Selected monad is nilpotent at power=" + pow2Test + ".\n");
			else
				_parent._GUI.appStatusBar
						.setStatusMsg("-->Selected monad is NOT nilpotent at power=" + pow2Test + ".\n");
		} catch (NullPointerException eNull) // Catch the empty text 'real number' text field on the FieldBar.
		{
			ErrorDialog.show("Power Nilpotent Test must have a real # in the FieldBar.\nNothing done.",
					"Null Pointer Exception");
			return;
		} catch (NumberFormatException eFormat) // Catch the non-parse-able text 'real number' text field on the
												// FieldBar.
		{
			ErrorDialog.show("Power Nilpotent Test must have a parse-able real # in the FieldBar.\nNothing done.",
					"Number Format Exception");
			return;
		} catch (CladosMonadException e) {
			ErrorDialog.show("Selected monad has an issue.\nNothing done.\n" + e.getSourceMessage(),
					"Clados Monad Exception");
		} catch (FieldBinaryException eb) {
			ErrorDialog.show("Selected monad has an issue.\nNothing done.\n" + eb.getSourceMessage(),
					"Field Binary Exception");
		}
	}
}