/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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