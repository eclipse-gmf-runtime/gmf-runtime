/*
 * Licensed Materials - Use restricted, please refer to the "Samples Gallery" terms
 * and conditions in the IBM International Program License Agreement.
 *
 * © Copyright IBM Corporation 2005. All Rights Reserved. 
 */
package org.eclipse.gmf.examples.runtime.diagram.layout.provider;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider;
import com.ibm.xtools.notation.Bounds;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.Node;
import com.ibm.xtools.notation.View;

/**
 * @author sshaw
 *
 * Example provider for layout.  Calculates a square grid and positions each
 * node inside the grid.
 */
public class SquareLayoutProvider extends AbstractProvider 
			implements ILayoutNodesProvider {

	public static String SQUARE_LAYOUT = "Square"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */  
	public boolean provides(IOperation operation) {
		// check to make sure all node are contained in a diagram
		if (operation instanceof ILayoutNodesOperation) {
			Iterator nodes = ((ILayoutNodesOperation)operation).getLayoutNodes().listIterator();
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
		// then the provider can compare against ILayoutNodesProvider.DEFAULT_LAYOUT.
		IAdaptable layoutHint = ((ILayoutNodesOperation) operation).getLayoutHint();
		String layoutType = (String) layoutHint.getAdapter(String.class);
		//return DEFAULT_LAYOUT.equals(layoutType);
		return SQUARE_LAYOUT.equals(layoutType);	
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.presentation.services.layout.ILayoutNodesProvider#layoutNodes(java.util.List, boolean, org.eclipse.core.runtime.IAdaptable)
	 */
	public Runnable layoutNodes(List layoutNodes,
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
