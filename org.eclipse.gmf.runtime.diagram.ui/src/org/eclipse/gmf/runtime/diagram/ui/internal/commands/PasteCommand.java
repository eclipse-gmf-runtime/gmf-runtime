/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.MeasurementUnitHelper;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.runtime.notation.Bendpoints;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Size;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.jface.util.Assert;

/**
 * Paste Command for the views
 * 
 * @author Vishy Ramaswamy
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public final class PasteCommand extends ClipboardCommand {
    /**
     * The clipboard data
     */
    private final ICustomData[] data; 
    
    private int offset = 0;
    private IMapMode mm;

    /**
     * Constructor for PasteCommand.
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param label
     * @param viewContext
     * @param data
     * @param mm
	 * 			the <code>IMapMode</code> that is used to convert the layout constraint
	 * 			and calculate the offset in logical coordinates
     */
    public PasteCommand(TransactionalEditingDomain editingDomain, 
        String label,
        View viewContext,
        ICustomData[] data, IMapMode mm) {
        super(editingDomain, label, viewContext);

        Assert.isNotNull(data);
        this.data = data;
        this.offset = mm.DPtoLP(10);
        this.mm = mm;
    }

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

	    /* Paste on the target */
	    if (data != null && data.length > 0) {
	        List allViews = new ArrayList();
	    	for (int j = 0; j < data.length; j++) {
	            /* Get the string from the clipboard data */
	            String xml = new String(data[j].getData());
	
	            /* Paste the xml on to the target view's diagram */
	            List views = pasteFromString(getViewContext(),xml);
	            allViews.addAll(views);
	        }
	        return CommandResult.newOKCommandResult(allViews);
	    }
	    return CommandResult.newOKCommandResult();
	}
	
	/**
     * Method pasteFromString.
     * pastes the clipboard contents on to self
     * @param clipboard The clipboard contents - serialization used during copy
     * @return List The list of IView resulting from the paste
     */
	 private List pasteFromString(View view, String clipboard) {
	    ArrayList retval = new ArrayList();
	    Iterator pastedElements = ClipboardUtil.pasteElementsFromString(clipboard, view, null, null).iterator();
	    
	    // get the measurement unit
	    MeasurementUnit mu = MeasurementUnit.HIMETRIC_LITERAL;
	    
	    while( pastedElements.hasNext() ) {
            Object element = pastedElements.next();
            if (element instanceof View) {
	            retval.add(element);
	        }
            else if (element instanceof EAnnotation) {
            	EAnnotation measureUnitAnnotation  = (EAnnotation)element;
        		String unitName = measureUnitAnnotation.getSource();
        		mu = MeasurementUnit.get(unitName);
            }
        }
	    
        /* Set the new bounds for the pasted IShapeView views */
	    Set edges = convertNodesConstraint(retval, mu, new Point(offset, offset));
        
        // now go through all associated edges and adjust the bendpoints
        convertEdgeBendpoints(mu, edges);
        
        return retval;
	}

	/**
	 * @param mu the <code>MeasurementUnit</code> for the notation diagram.
	 * @param edges the <code>Set</code> of edges to convert the bendpoints of.
	 */
	private void convertEdgeBendpoints(MeasurementUnit mu, Set edges) {
		for (Iterator i = edges.iterator(); i.hasNext();) {
        	Edge nextEdge = (Edge)i.next();
        	Bendpoints bendpoints = nextEdge.getBendpoints();
        	
        	if (bendpoints instanceof RelativeBendpoints) {
    			RelativeBendpoints relBendpoints = (RelativeBendpoints)bendpoints;
        		List points = relBendpoints.getPoints();
        		List newpoints = new ArrayList(points.size());
        		ListIterator li = points.listIterator();
        		
        		IMapMode viewMapMode = MeasurementUnitHelper.getMapMode(mu);
        		
        		while (li.hasNext()) {
        			RelativeBendpoint rb = (RelativeBendpoint)li.next();
        			
        			Dimension source = new Dimension(rb.getSourceX(), rb.getSourceY());
        			Dimension target = new Dimension(rb.getTargetX(), rb.getTargetY());
        			if (!viewMapMode.equals(mm)) {
        				source = (Dimension)viewMapMode.LPtoDP(source);
        				source = (Dimension)mm.DPtoLP(source);
        				
        				target = (Dimension)viewMapMode.LPtoDP(target);
        				target = (Dimension)mm.DPtoLP(target);
        			}
        			
        			newpoints.add(new RelativeBendpoint(source.width, source.height, 
        							target.width, target.height));
        		}
        		
        		relBendpoints.setPoints(newpoints);
        	}
        	
        }
	}

	/**
	 * @param retval the <code>List</code> of <code>Node</code> objects to convert the constraint of.
	 * @param mu the <code>MeasurementUnit</code> for the notation diagram.
	 * @return the <code>Set</code> of <code>Edge</code> views that are attached to the list of nodes 
	 */
	private Set convertNodesConstraint(List retval, MeasurementUnit mu, Point ptOffset) {
		Set edges = new HashSet();
        for (Iterator i = retval.iterator(); i.hasNext();) {
            View nextView = (View) i.next();
            if (nextView instanceof Node) {
            	Node node = (Node)nextView;
        		Point loc = new Point(0, 0);
        		LayoutConstraint lc = node.getLayoutConstraint();
        		if (lc instanceof Location) {
        			Location locC = (Location)lc;
        			loc = new Point(locC.getX(), locC.getY());
        		}
        			
        		Dimension size = new Dimension(0, 0);
        		if (lc instanceof Size) {
        			Size sizeC = (Size)lc;
        			size = new Dimension(sizeC.getWidth(), sizeC.getHeight());
        		}
        		
        		IMapMode viewMapMode = MeasurementUnitHelper.getMapMode(mu);
        				
        		if (!viewMapMode.equals(mm)) {
        			// convert location to native coordinates
        			loc = (Point)viewMapMode.LPtoDP(loc);
        			loc = (Point)mm.DPtoLP(loc);
        			
        			// convert size to native coordinates
        			Dimension origSize = new Dimension(size);
        			size = (Dimension)viewMapMode.LPtoDP(size);
        			size = (Dimension)mm.DPtoLP(size);
        			if (origSize.width == -1)
        				size.width = -1;
        			if (origSize.height == -1)
        				size.height = -1;
        		}
        		
        		Rectangle constraintRect = new Rectangle(loc, size);
        		constraintRect = constraintRect.getTranslated(ptOffset.x, ptOffset.y);
    			ViewUtil.setStructuralFeatureValue(nextView,NotationPackage.eINSTANCE.getLocation_X(), new Integer(constraintRect.x));
                ViewUtil.setStructuralFeatureValue(nextView,NotationPackage.eINSTANCE.getLocation_Y(), new Integer(constraintRect.y));
                ViewUtil.setStructuralFeatureValue(nextView,NotationPackage.eINSTANCE.getSize_Width(), new Integer(constraintRect.width));
                ViewUtil.setStructuralFeatureValue(nextView,NotationPackage.eINSTANCE.getSize_Height(), new Integer(constraintRect.height));
                
                edges.addAll(((Node)nextView).getTargetEdges());
                edges.addAll(((Node)nextView).getSourceEdges());
                
                // recursively perform the same operation on children of the node
                edges.addAll(convertNodesConstraint(node.getPersistedChildren(), mu, new Point(0, 0)));
            }
        }
		return edges;
	}

}
