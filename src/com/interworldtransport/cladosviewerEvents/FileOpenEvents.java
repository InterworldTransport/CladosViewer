/**
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosviewer.FileOpenEvents<br>
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
 * ---com.interworldtransport.cladosviewer.FileOpenEvents<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosviewerEvents;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.interworldtransport.cladosF.CladosFBuilder;
import com.interworldtransport.cladosF.CladosField;
import com.interworldtransport.cladosG.AlgebraAbstract;
import com.interworldtransport.cladosG.CladosGBuilder;
import com.interworldtransport.cladosG.Foot;
import com.interworldtransport.cladosG.MonadAbstract;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.GeneratorRangeException;
import com.interworldtransport.cladosviewer.ErrorDialog;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class FileOpenEvents implements ActionListener {

	private final static String path2AllCardinals = "//Foot/Cardinals/Cardinal/@type";
	private final static String path2AllSignatures = "//GProduct/@signature";
	private final static String path2DivFields = "//Algebra/*[@cardinal]";
	private final static String path2FootCardinals = "//Foot[name=!]/Cardinals/Cardinal/@type";
	private final static String path2FootNames = "//Algebra/Foot/@name";
	private final static String path2NyadNames = "//Nyad/@name";
	// private final static String path4NyadOrders = "count(//Nyad/@order)";
	private final static String path4NyadCount = "count(//Nyad)";
	private ArrayList<AlgebraAbstract> _algs;
	private ArrayList<Foot> _foot;
	private ArrayList<MonadAbstract> _monads;
	private int _nyadCount;
	private CladosField _repMode;

	protected JMenuItem _control;
	protected FileEvents _parent;

	public FileOpenEvents(JMenuItem _control, FileEvents _parent) {
		super();
		this._control = _control;
		_control.addActionListener(this);
		this._parent = _parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If SaveFile is unknown at this point, present a file chooser.
		// If there are issues with the file pointed to by SaveFile, present a chooser
		File fIni;
		if (_parent._GUI.IniProps.getProperty("Desktop.File.Snapshot") != null)
		// save to file described in conf setting
		{
			fIni = new File(_parent._GUI.IniProps.getProperty("Desktop.File.Snapshot"));
			if (!(fIni.exists() & fIni.isFile() & fIni.canWrite())) {
				int returnVal = _parent.fc.showSaveDialog(_control);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fIni = _parent.fc.getSelectedFile();
				} else
					return;
			}
		} else {
			int returnVal = _parent.fc.showSaveDialog(_control);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fIni = _parent.fc.getSelectedFile();
			} else
				return;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fIni);
			if (doc == null | doc.getFirstChild().getNodeName() != "NyadList")
				return;

			_repMode = findDivField(doc, xPathfactory.newXPath());
			// Validate! If currently visible nyads have a different repMode,
			// just STOP before conflicts happen.
			if (_parent._GUI.appGeometryView.getNyadListSize() > 0) {
				if (_parent._GUI.appGeometryView.getRepMode() != _repMode)
					return;
			}

			// TODO parse the XML into the 'defaults' for initiating the calculator.
			// 'Count', 'Order', 'DivField', etc.
			buildGProducts(doc, xPathfactory.newXPath());
			// ----------------
			// Bases and GProducts are built and sitting in CladosGBuilder.INSTANCE lists.
			// They can be acquired when building monads and nyads later.
			// ----------------
			buildCardinals(doc, xPathfactory.newXPath());
			// ----------------
			// Cardinals are built and sitting in CladosFBuilder.INSTANCE list.
			// They can be acquired when building monads and nyads later.
			// ----------------
			buildFoot(doc, xPathfactory.newXPath());
			if (_foot == null | _foot.size() == 0)
				return; // STOP if no Foot objects created.
			appendFootCardinals(doc, xPathfactory.newXPath());
			// ----------------
			// Foot objects have been worked out and created.
			// NOT appending Frames right now as those will change soon.
			// ----------------

			_nyadCount = Integer.parseInt(xpath.evaluate(path4NyadCount, doc));
			// ----------------
			// Now we know how many nyads and which DivField children are involved.
			// ----------------

			// TODO Validate! If Count and Order (+) don't make sense, just STOP.
			String[] nyadNames = new String[_nyadCount];
			XPathExpression expr = xpath.compile(path2NyadNames);
			NodeList nyadNameNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int k = 0; k < nyadNameNodes.getLength(); k++)
				nyadNames[k] = nyadNameNodes.item(k).getNodeValue();

		} catch (ParserConfigurationException e1) {
			ErrorDialog.show("Couldn't acquire DocumentBuilderFactory instance.\n" + e1.getMessage(),
					"Parser Configuration Exception");
			return;
		} catch (SAXException e1) {
			ErrorDialog.show("Couldn't parse the XML to create a Document.\n" + e1.getMessage(), "SAX Exception");
			return;
		} catch (IOException e1) {
			ErrorDialog.show("Stopped by a general IO issue.\n" + e1.getMessage(), "IO Exception");
			return;
		} catch (NumberFormatException e1) {
			ErrorDialog.show("A number was expected somewhere and couldn't be parsed.\n" + e1.getMessage(),
					"Number Format Exception");
			return;
		} catch (XPathExpressionException e1) {
			ErrorDialog.show("XPath string malformed\nOR\nXML file doesn't contain expected node.\n" + e1.getMessage(),
					"XPath Expression Exception");
			return;
		} catch (DOMException e1) {
			ErrorDialog.show("Couldn't parse the XML to create a Document.\n" + e1.getMessage(), "DOM Exception");
			return;
		} catch (GeneratorRangeException e1) {
			ErrorDialog.show("An unsupported signature length was found in file.", "Generator Range Exception");
			return;
		} catch (BadSignatureException e1) {
			ErrorDialog.show("An malformed(bad characters) signature was found in file.", "Bad Signature Exception");
			return;
		}

		// TODO Call builder method in parent Event handler. It will hand back an
		// ArrayList<NyadAbstract>?

		// TODO Validate! If ArrayList length isn't 'Count', inform the user.

		// TODO Deliver nyads one at a time to the Viewer Panel in the calculator to be
		// appended as if created there.

		_parent._GUI.appStatusBar
				.setStatusMsg("File OPEN is not ready yet. Working on XML parser of CladosG objects.\n");
	}

	private void appendFootCardinals(Document pDoc, XPath pX) throws XPathExpressionException {
		for (Foot pt : _foot) {
			StringBuffer test = new StringBuffer(path2FootCardinals);
			int spot = test.indexOf("!");
			test.replace(spot, spot, String.valueOf(_foot.indexOf(pt)));

			XPathExpression expr = pX.compile(test.toString());
			NodeList cardNodes = (NodeList) expr.evaluate(pDoc, XPathConstants.NODESET);
			if (cardNodes == null | cardNodes.getLength() == 0)
				continue;
			for (int m = 0; m < cardNodes.getLength(); m++)
				pt.appendCardinal(CladosFBuilder.INSTANCE.findCardinal(cardNodes.item(m).getNodeName()));
		}
	}

	private void buildCardinals(Document pDoc, XPath pX) throws XPathExpressionException {
		XPathExpression expr = pX.compile(path2AllCardinals);
		NodeList cardNodes = (NodeList) expr.evaluate(pDoc, XPathConstants.NODESET);
		for (int k = 0; k < cardNodes.getLength(); k++)
			CladosFBuilder.INSTANCE.createCardinal(cardNodes.item(k).getNodeValue());
	}

	private void buildFoot(Document pDoc, XPath pX) throws XPathExpressionException {
		XPathExpression expr = pX.compile(path2FootNames);
		NodeList footNodes = (NodeList) expr.evaluate(pDoc, XPathConstants.NODESET);
		if (footNodes == null | footNodes.getLength() == 0)
			return;

		ArrayList<String> finds = new ArrayList<String>(footNodes.getLength());

		for (int k = 0; k < footNodes.getLength(); k++)
			if (!finds.contains(footNodes.item(k).getNodeName()))
				finds.add(footNodes.item(k).getNodeName());

		_foot = new ArrayList<Foot>(finds.size());
		for (String pt : finds)
			_foot.add(CladosGBuilder.createFoot(pt, pt));
	}

	private void buildGProducts(Document pDoc, XPath pX)
			throws XPathExpressionException, DOMException, GeneratorRangeException, BadSignatureException {
		XPathExpression expr = pX.compile(path2AllSignatures);
		NodeList sigNodes = (NodeList) expr.evaluate(pDoc, XPathConstants.NODESET);
		for (int k = 0; k < sigNodes.getLength(); k++)
			CladosGBuilder.INSTANCE.createGProduct(sigNodes.item(k).getNodeValue());
	}

	private CladosField findDivField(Document pDoc, XPath pX) throws XPathExpressionException {
		XPathExpression expr = pX.compile(path2DivFields);
		NodeList divFieldNodes = (NodeList) expr.evaluate(pDoc, XPathConstants.NODESET);
		String first = divFieldNodes.item(0).getNodeName();
		if (divFieldNodes.getLength() == 1)
			switch (first) {
			case "RealF":
				return CladosField.REALF;
			case "RealD":
				return CladosField.REALD;
			case "ComplexF":
				return CladosField.COMPLEXF;
			case "ComplexD":
				return CladosField.COMPLEXD;
			default:
				return null;
			}

		for (int k = 1; k < divFieldNodes.getLength(); k++)
			if (first != divFieldNodes.item(k).getNodeName())
				return null;

		switch (first) {
		case "RealF":
			return CladosField.REALF;
		case "RealD":
			return CladosField.REALD;
		case "ComplexF":
			return CladosField.COMPLEXF;
		case "ComplexD":
			return CladosField.COMPLEXD;
		default:
			return null;
		}

	}
}