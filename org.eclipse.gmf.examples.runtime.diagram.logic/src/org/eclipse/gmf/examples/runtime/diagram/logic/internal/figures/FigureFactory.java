/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 *
 * This class is used to create figures
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class FigureFactory {
	public static NodeFigure createNewLED(){
		return new LEDFigure();
	}

	public static PolylineConnectionEx createNewBendableWire(){
		PolylineConnectionEx conn = new PolylineConnectionEx();
		return conn;
	}
	
	public static NodeFigure createNewCircuit(){
		CircuitFigure f = new CircuitFigure();
		return f;
	}
	
	public static NodeFigure createNewFlowContainer(){
		LogicFlowFigure lf = new LogicFlowFigure();
		return lf;
	}
}