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

package org.eclipse.gmf.tests.runtime.gef.ui;

import junit.framework.TestCase;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.gef.ui.internal.figures.DiamondFigure;

/**
 * @author oboyko
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FigureSlidableAnchorTests
	extends TestCase {
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStraightlineTolerance() {
		NodeFigure fig = new NodeFigure();
		fig.setBounds(new Rectangle(100,100, 100, 100));
		
		// default anchor test 
		Point p1 = new Point(150,150); 
		Point p2 = new Point(148,300); 
		ConnectionAnchor anchor = fig.getSourceConnectionAnchorAt(p1);
		Point reference = anchor.getLocation(p2);
		assertTrue(reference.x==p2.x);

		// Slidable anchor test inside figure's bounds
		p1 = new Point(110,200); 
		p2 = new Point(112,300); 
		anchor = fig.getSourceConnectionAnchorAt(p1);
		reference = anchor.getLocation(p2);
		assertTrue(reference.x==p2.x);
	}
	
	public void testAnchorPosition() {
		NodeFigure fig = new NodeFigure();
		fig.setBounds(new Rectangle(-10,-10, 20, 20));
		
		// default anchor test 
		Point p = new Point(3,3); 
		ConnectionAnchor anchor = fig.getSourceConnectionAnchorAt(p);
		Point reference = anchor.getReferencePoint();
		assertTrue(reference.x==0 && reference.y==0);

		// Slidable anchor test inside figure's bounds
		p = new Point(8,8); 
		anchor = fig.getSourceConnectionAnchorAt(p);
		reference = anchor.getReferencePoint();
		assertTrue(reference.x==p.x && reference.y==p.y);

		// Slidable anchor test outside figure's bounds
		p = new Point(12,12); 
		anchor = fig.getSourceConnectionAnchorAt(p);
		reference = anchor.getReferencePoint();
		assertTrue(reference.x==10 && reference.y==10);
		
		p = new Point(15,0); 
		anchor = fig.getSourceConnectionAnchorAt(p);
		reference = anchor.getReferencePoint();
		assertTrue(reference.x==10 && reference.y==0);
	}
	
	public void testAnchorLocationOnPolygonFigure() {
		DiamondFigure fig = new DiamondFigure();
		fig.setBounds(Rectangle.SINGLETON);
		fig.getBounds().setLocation(-10,-10);
		fig.getBounds().setSize(20,20);
		Point reference = new Point(20,0);
		
		// Default anchor location
		Point position = new Point(2,2);
		ConnectionAnchor anchor = fig.getSourceConnectionAnchorAt(position);
		Point location = anchor.getLocation(reference);
		assertTrue(location.x==10 && location.y==0);
		
		// Slidable anchor location inside the figure
		position = new Point(-8,0);
		anchor = fig.getSourceConnectionAnchorAt(position);
		location = anchor.getLocation(reference);
		assertTrue(location.x==10 && location.y==0);
		
		// Slidable anchor location outside the figure
		position = new Point(10,5);
		anchor = fig.getSourceConnectionAnchorAt(position);
		location = anchor.getLocation(reference);
		assertTrue(location.x==10 && location.y==0);

		// Reference inside the figure
		reference = new Point(-2,5);
		location = anchor.getLocation(reference);
		assertTrue(location.x==-5 && location.y==5);
		
		// Reference and anchor's position are equal
		reference = new Point(10,5);
		location = anchor.getLocation(reference);
		assertTrue(location.x==7 && location.y==3);
		
		// Rference point is at the center of the figure as well as the anchors position
		reference = new Point(0,0);
		position = new Point (0,0);
		anchor = anchor = fig.getSourceConnectionAnchorAt(position);
		location = anchor.getLocation(reference);
		assertTrue(location.x==0 && location.y==0);
	}
}
