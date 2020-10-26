/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosviewer.OptionsDialog<br>
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
 * ---com.interworldtransport.cladosviewer.OptionsDialog<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosviewer;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**  com.interworldtransport.cladosviewer.OptionsDialog
 * The Optons Dialog window is supposed to show a window that would allow a user
 * to adjust the configuration file from within the Viewer.
 * @version 0.85,
 * @author Dr Alfred W Differ
 */
public class OptionsDialog extends JDialog implements ActionListener, TableModelListener
{
	private class 			MyTableModel 		extends AbstractTableModel 
	{
		private 			String[] 			columnNames = {"Key", "Value"};
		private				Object[][]			data;

		public int getColumnCount() 
		{
			return columnNames.length;
	    }
	    public String getColumnName(int col) 
	    {
	        return columnNames[col];
	    }
	    public int getRowCount() 
	    {
	        return data.length;
	    }    
	    public Object getValueAt(int row, int col) 
	    {
	        return data[row][col];
	    }
	    /*
	     * Don't need to implement this method unless your table's
	     * editable.
	     */
	    public boolean isCellEditable(int row, int col) 
	    {
	        if (col == 1) return true;
	        return false;
	    }	    
	    /*
	     * Don't need to implement this method unless your table's
	     * data can change.
	     */
	    public void setValueAt(Object value, int row, int col) 
	    {
	        data[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }
	}
	
	private	static final 	Color				_backColor 		= new Color(255, 255, 222);
	private	static final	Color				_tblBackColor 	= new Color(212, 212, 192);
	private 				CladosCalculator	_GUI; 
	private 				JButton 			btnClose;
	private 				JButton				btnReload;
	private 				JButton 			btnSave;
	private 				JPanel				mainPane		= new JPanel(new BorderLayout());

/**
 * The constructor sets up the options dialog box and displays it.
 * @param mainWindow
 *  CladosCalculator
 * This is just a reference to the owning application so error messages can be 
 * reported out to the GUI.
 */
    public OptionsDialog(CladosCalculator mainWindow)
    {
		super(mainWindow, "Preferences Dialog", true); 
		_GUI=mainWindow;
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setBackground(_backColor);
		setContentPane(mainPane);
		createControlButtons();
		addPropTable();
		setSize(600, 600);	

		Point parentLocation = mainWindow.getLocation();
		int Xloc = (int) parentLocation.getX() + ((mainWindow.getWidth() - 300) / 2);
		int Yloc = (int) parentLocation.getY(); //+ ((mainWindow.getHeight() - 400) / 2);
		setLocation(Xloc, Yloc);
		setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent event) 
    {
    	switch (event.getActionCommand())
    	{
    		case "reload":	
    			reloadAll();
    			break;
    		case "close":	
    			dispose();
    			break;
    		case "save":	
    			saveAll();
    			dispose();
    	}
    }
    
    @Override
	public void tableChanged(TableModelEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
    
    private void addPropTable()
    {
    	
    	MyTableModel test = new MyTableModel();
    	JTable propTable = new JTable(test);
    	propTable.setBackground(_tblBackColor);
    	
		Object[] testSet = _GUI.IniProps.stringPropertyNames().toArray();
		test.data = new String [testSet.length][2];
		for (int j=0; j<test.getRowCount(); j++)
		{	//setValueAt(Object value, int row, int col)
			test.setValueAt(testSet[j], j, 0);
			test.setValueAt( _GUI.IniProps.get(testSet[j]), j, 1);
		}
		
		mainPane.add(new JScrollPane(propTable), "Center");
		propTable.setFillsViewportHeight(true);    
		propTable.setAutoCreateRowSorter(true);
		propTable.getModel().addTableModelListener(this);
    }
    
    private void createControlButtons()
    {	// Create button panel
    	JPanel controlPanel = new JPanel(new FlowLayout());
    	controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    	controlPanel.setBackground(_backColor);
    	mainPane.add(controlPanel, "South");
    	
    	// Create buttons
		btnReload = new JButton(new ImageIcon(_GUI.IniProps.getProperty("Desktop.Image.Edit")));
		btnReload.setActionCommand("reload");
		btnReload.setToolTipText("Reload properties");
		btnReload.setPreferredSize(new Dimension(30,30));
		btnReload.setBorder(BorderFactory.createEtchedBorder(0));
		btnReload.addActionListener(this);
		controlPanel.add(btnReload);	
    		
		btnSave = new JButton(new ImageIcon(_GUI.IniProps.getProperty("Desktop.Image.Save")));
		btnSave.setActionCommand("save");
		btnSave.setToolTipText("Save any changes, then close.");
		btnSave.setPreferredSize(new Dimension(30,30));
		btnSave.setBorder(BorderFactory.createEtchedBorder(0));
		btnSave.addActionListener(this);
		controlPanel.add(btnSave);
	
		btnClose = new JButton(new ImageIcon(_GUI.IniProps.getProperty("Desktop.Image.Close")));
		btnClose.setActionCommand("close");
		btnClose.setToolTipText("Close the dialog. No further changes.");
		btnClose.setPreferredSize(new Dimension(30,30));
		btnClose.setBorder(BorderFactory.createEtchedBorder(0));
		btnClose.addActionListener(this);
		controlPanel.add(btnClose);	
    }
    
    private void reloadAll()
    {
    	File fIni=new File(_GUI.IniProps.getProperty("Desktop.PropertiesFile"));
    	if (!(fIni.exists() & fIni.isFile() & fIni.canWrite())) _GUI.appStatusBar.setStatusMsg("The configuration file is not valid.");
		
    	try (	FileInputStream tempSpot=new FileInputStream(fIni);
	    		BufferedInputStream tSpot = new BufferedInputStream(tempSpot))
    	{
    		_GUI.IniProps=new Properties(System.getProperties());
    		_GUI.IniProps.loadFromXML(tSpot); // This loads an XML formatted key/pair properties file.
    		addPropTable();
    		tSpot.close();
    		tempSpot.close();
    	}
    	catch(IOException e)
    	{
    		_GUI.appStatusBar.setStatusMsg("IO Problem:  Incomplete Access to associated INI files.");
    	}
    }
    
    private void saveAll()
    {
    	if (_GUI.IniProps.getProperty("Desktop.PropertiesFile") != null)	// save to file described in conf setting
	    {
	    	File fIni=new File(_GUI.IniProps.getProperty("Desktop.PropertiesFile"));
	    	if (!(fIni.exists() & fIni.isFile() & fIni.canWrite()))
	    	{
	    		saveAsAll();	// Defer to Save As
	    		return;
	    	}
	    	try(	FileOutputStream tempSpot=new FileOutputStream(fIni);
		    		BufferedOutputStream tSpot = new BufferedOutputStream(tempSpot))
	    	{
	    	   	_GUI.IniProps.storeToXML(tSpot, "Saved From Clados Calculator Properties Dialog");
	    	   	_GUI.appStatusBar.setStatusMsg("-->Options SAVED.\n");
	    	}
	    	catch (IOException e)
	    	{
	    	   	_GUI.appStatusBar.setStatusMsg("-->Options NOT saved. IO Exception involving Properties target file.\n");
	    	}
	    	finally
 	    	{
 	    		fIni = null;
 	    	}
	    }
	    else	saveAsAll();	// Defer to Save As
    }
	private void saveAsAll()
    {
    	JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	int returnVal = fc.showSaveDialog(btnSave);
 	    if (returnVal == JFileChooser.APPROVE_OPTION) 
 	    {
 	    	File fIni = fc.getSelectedFile();
 	    	try (	FileOutputStream tempSpot=new FileOutputStream(fIni);
		    		BufferedOutputStream tSpot = new BufferedOutputStream(tempSpot))
 	    	{
	    	   	_GUI.IniProps.storeToXML(tSpot, "Saved From Clados Calculator Properties Dialog");
	    	   	_GUI.appStatusBar.setStatusMsg("-->Options SAVED AS.\n");
	    	}
	    	catch (IOException e)
	    	{
	    	   	_GUI.appStatusBar.setStatusMsg("-->Options NOT saved. IO Exception involving Properties target file.\n");
	    	}
 	    	finally
 	    	{
 	    		fIni = null;
 	    	}
 	    } 
    }
}
