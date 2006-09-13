/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.layout.provider;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutNodeProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeOperation;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author sshaw
 *
 * Example provider for layout.  Calculates a square grid and positions each
 * node inside the grid.
 */
public class SquareLayoutProvider extends AbstractLayoutNodeProvider {

	public static String SQUARE_LAYOUT = "Square"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */  
	public boolean provides(IOperation operation) {
		// check to make sure all node are contained in a diagram
		if (operation instanceof ILayoutNodeOperation) {
			Iterator nodes = ((ILayoutNodeOperation)operation).getLayoutNodes().listIterator();
			if (nodes.hasNext()) { 
				Node node = ((ILayoutNode)nodes.next()).getNode();
				View container = (View)node.eContainer(); 
				if (!(container instanceof Diagram))
					return false;
			} 
		}
		else {
			return false;
		}
		
		// Provide for SQUARE_LAYOUT hint.  
		// Note: To override the default layout which is invoked from the Arrange menu
		// then the provider can compare against ILayoutNodeProvider.DEFAULT_LAYOUT.
		IAdaptable layoutHint = ((ILayoutNodeOperation) operation).getLayoutHint();
		String layoutType = (String) layoutHint.getAdapter(String.class);
		//return DEFAULT_LAYOUT.equals(layoutType);
		return SQUARE_LAYOUT.equals(layoutType);	
	}
	
	public Runnable layoutLayoutNodes(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint) {
		
		final List lnodes = layoutNodes;
		
		return new Runnable() {
			public void run() {
				final int rowsize = (int)Math.round(Math.sqrt(lnodes.size()));
				
				// calculate the grid size
				int gridWidth = 0;
				int gridHeight = 0;
				ListIterator li = lnodes.listIterator();
				while (li.hasNext()) {
					ILayoutNode lnode = (ILayoutNode)li.next();
					if (lnode.getWidth() > gridWidth)
						gridWidth = lnode.getWidth();
					if (lnode.getHeight() > gridHeight)
						gridHeight = lnode.getHeight();
				}
				
				// add a small buffer in HiMetric units
				gridWidth += 100;
				gridHeight += 100;
				
				int i = 0;
				li = lnodes.listIterator();
				while (li.hasNext()) {
					ILayoutNode lnode = (ILayoutNode)li.next();
					
					Bounds bounds = (Bounds)lnode.getNode().getLayoutConstraint();
					bounds.setX((i % rowsize) * gridWidth);
					bounds.setY((i / rowsize) * gridHeight);
					lnode.getNode().setLayoutConstraint(bounds);
					
					i++;
				}
			}
		};

	}
}
