/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;

/**
 * @author mmuszyns
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 * 
 * Class SingleDimensionalAnchor
 * 
 * Provides basic functionality for anchors whose location on
 * a shape is single dimensional,
 */

abstract public class SingleDimensionalAnchor extends AbstractConnectionAnchor {
	
	public SingleDimensionalAnchor(IFigure owner) {
		super(owner);
	}

	/**
	 * Method strToAbsPos.
	 * @param terminal
	 * @return int
	 * 
	 * Extracts the absolute position out of terminal string.
	 * 
	 */
	public static int strToAbsPos(String terminal) {
		int a = 0;
		try {
			int rIndex = terminal.indexOf('R');
			int start = rIndex < 0 ? 0 : rIndex+1;
			int commaIndex = terminal.indexOf(',');
			if (commaIndex < 0)
				a = Integer.parseInt(terminal.substring(start));
			else
				a = Integer.parseInt(terminal.substring(start, commaIndex));
		}
		catch (NumberFormatException e) {
			Log.error(Draw2dPlugin.getInstance(), IStatus.ERROR, e.getMessage(), e);
			throw new IllegalArgumentException();
		}
		return a;
	}
	
	/**
	 * Method strToReorient.
	 * @param terminal
	 * @return boolean
	 */
	public static boolean strToReorient(String terminal) {
		int i = terminal.indexOf('R');
		return i >= 0;
	}
	
	/**
	 * Method strToOrd.
	 * @param terminal
	 * @return int
	 * 
	 * Extracts the ordinal number out of terminal string.
	 * 
	 */
	public static int strToOrd(String terminal) {
		int o = 0;
		try {
			int commaIndex = terminal.indexOf(',');
			if (commaIndex < 0)
				throw new IllegalArgumentException();
			int sepIndex = terminal.indexOf('a');
			if (sepIndex < 0)
				sepIndex = terminal.indexOf('@');
			if (sepIndex < 0)
				o = Integer.parseInt(terminal.substring(commaIndex+1));
			else
				o = Integer.parseInt(terminal.substring(commaIndex+1, sepIndex));
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException(terminal);
		}
		return o;
	}
	
	/**
	 * Method nameToDistance.
	 * @param terminal
	 * @return int
	 */
	public static int terminalToDistance(String terminal) {
		// expects "anchor24"
		// converts it to 24
		if (terminal.length() == 0 )
			return 0;
		StringBuffer s = new StringBuffer(terminal);
		int i = 0;
		while(!(Character.isDigit(s.charAt(i)))) {
			i++;
			if (i == s.length())
				break;
		}
		Integer x = new Integer(0);
		if (i<s.length()) {
			String num = s.substring(i);
			x = new Integer(num);
		}
		return x.intValue();
	}
	
	/**
	 * Method absPosToStr.
	 * @param absPos
	 * @param ordinal
	 * @param diagramPosString
	 * @return String
	 * 
	 * Generates a new terminal string for a given absolute position, ordinal number, 
	 * and a diagram position string.
	 * 
	 */
	public static String absPosToStr(int absPos, int ordinal, String diagramPosString) {
		if (absPos < 0)
			absPos = 0;
		return "" + absPos + "," + ordinal + diagramPosString;//$NON-NLS-1$//$NON-NLS-2$
	}
	
	/**
	 * @see org.eclipse.draw2d.ConnectionAnchor#getReferencePoint()
	 * @return  The reference point of this anchor.
	 * 
	 * Returns the point which is used as the reference by this 
	 * connection anchor. It is generally dependent on the figure
	 * which is the owner of this anchor.
	 *
	 */
	public Point getReferencePoint() {
		return getLocation(Point.SINGLETON);
	}
	
}

