/*
<h2>Copyright</h2>
Copyright (c) 2005 Interworld Transport.  All rights reserved.<br>
---com.interworldtransport.cladosviewer.SOpsReverseEvents------------------------------------------
<p>
Interworld Transport grants you ("Licensee") a license to this software
under the terms of the GNU General Public License.<br>
A full copy of the license can be found bundled with this package or code file.
<p>
If the license file has become separated from the package, code file, or binary
executable, the Licensee is still expected to read about the license at the
following URL before accepting this material.
<blockquote><code>http://www.opensource.org/gpl-license.html</code></blockquote>
<p>
Use of this code or executable objects derived from it by the Licensee states their
willingness to accept the terms of the license.
<p>
A prospective Licensee unable to find a copy of the license terms should contact
Interworld Transport for a free copy.
<p>
---com.interworldtransport.cladosviewer.SOpsReverseEvents------------------------------------------
*/

package com.interworldtransport.cladosviewer;
import com.interworldtransport.clados.*;
import java.awt.event.*;
import javax.swing.*;

/** com.interworldtransport.cladosviewer.SOpsReverseEvents
 *  This class manages events relating to a simple operation...
 *  Reverse this Monad.
 *
 * @version 0.80, $Date: 2005/07/25 01:44:25 $
 * @author Dr Alfred W Differ
 */
public class SOpsReverseEvents implements ActionListener
 {
    protected ViewerMenu		ParentGUIMenu;
    protected JMenuItem 		ControlIt;
    protected SOpsEvents 		Parent;

/** This is the default constructor.
 */
    public SOpsReverseEvents(	ViewerMenu pGUIMenu,
    				JMenuItem pmniControlled,
				SOpsEvents pParent)
    {
	this.ParentGUIMenu=pGUIMenu;
	this.ControlIt=pmniControlled;
	this.ControlIt.addActionListener(this);
	this.Parent=pParent;

    }//end of SOpsReverseEvents constructor

/** This is the actual action to be performed by this member of the menu.
 */
    public void actionPerformed(ActionEvent evt)
    {
	MonadPanel MP0=ParentGUIMenu.ParentGUI.CenterAll.getNyadPanel(0).getMonadPanel(0);
	Monad Monad0=MP0.getMonad();
	Monad0.Reverse();
	MP0.setBottomFields();
	ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("First Monad has been reversed.\n");

    }//end of action performed method.

 }//end of SOpsReverseEvents class
