/*
<h2>Copyright</h2>
Copyright (c) 2005 Interworld Transport.  All rights reserved.<br>
---com.interworldtransport.cladosviewer.BOpsIdempotentEvents------------------------------------------
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
---com.interworldtransport.cladosviewer.BOpsIdempotentEvents------------------------------------------
*/

package com.interworldtransport.cladosviewer;
import com.interworldtransport.clados.*;
import java.awt.event.*;
import javax.swing.*;

/** com.interworldtransport.cladosviewer.BOpsIdempotentEvents
 *  This class manages events relating to the answering of a boolean question.
 *  Is the Monad idempotent?
 *
 * @version 0.80, $Date: 2005/07/25 01:44:25 $
 * @author Dr Alfred W Differ
 */
public class BOpsIdempotentEvents implements ActionListener
 {
    protected ViewerMenu		ParentGUIMenu;
    protected JMenuItem 		ControlIt;
    protected BOpsEvents 		Parent;

/** This is the default constructor.
 */
    public BOpsIdempotentEvents(	ViewerMenu pGUIMenu,
    					JMenuItem pmniControlled,
					BOpsEvents pParent)
    {
	this.ParentGUIMenu=pGUIMenu;
	this.ControlIt=pmniControlled;
	this.ControlIt.addActionListener(this);
	this.Parent=pParent;

    }//end of BOpsIdempotentEvents constructor

/** This is the actual action to be performed by this member of the menu.
 */
    public void actionPerformed(ActionEvent evt)
    {
	Monad Monad0=ParentGUIMenu.ParentGUI.CenterAll.getNyadPanel(0).getMonadPanel(0).getMonad();
	boolean test=false;
	try
	{
		test=Monad0.isIdempotent();
	}
	catch (CladosException e)
	{
		;
	}

	if (test)
	{
		ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("First Monad is judged as idempotent.\n");
	}
	else
	{
		ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("First Monad is judged as not idempotent.\n");
	}
    }//end of action performed method.

 }//end of BOpsIdempotentEvents class
