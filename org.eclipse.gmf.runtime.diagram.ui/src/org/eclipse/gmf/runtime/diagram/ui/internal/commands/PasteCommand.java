/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
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

    /**
     * Constructor for PasteCommand.
     * @param viewContext
     * @param data
     */
    public PasteCommand(
        View viewContext,
        ICustomData[] data) {
        this(null, viewContext, data);
    }

    /**
     * Constructor for PasteCommand.
     * @param label
     * @param viewContext
     * @param data
     * 
     */
    public PasteCommand(
        String label,
        View viewContext,
        ICustomData[] data) {
        super(label, viewContext);

        Assert.isNotNull(data);
        this.data = data;
    }

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
	    /* Paste on the target */
	    if (data != null && data.length > 0) {
	        List allViews = new ArrayList();
	    	for (int j = 0; j < data.length; j++) {
	            /* Get the string from the clipboard data */
	            String xml = new String(data[j].getData());
	
	            /* Paste the xml on to the target view's diagram */
	            List views = pasteFromString(getViewContext(),xml);
	            allViews.addAll(views);
	            
	            /* Set the new bounds for the pasted IShapeView views */
                for (Iterator i = views.iterator(); i.hasNext();) {
                    View view = (View) i.next();
                    if (view instanceof Node &&
                    	((Node)view).getLayoutConstraint() instanceof Location/*view instanceof IShapeView*/) {
                    	Integer x = (Integer) ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_X());
                        ViewUtil.setStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_X(), new Integer(x.intValue() + MapMode.DPtoLP(10)));
                        Integer y = (Integer) ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_Y());
                        ViewUtil.setStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_Y(), new Integer(y.intValue() + MapMode.DPtoLP(10)));
                    }
                }
	        }
	        return newOKCommandResult(allViews);
	    }
	    return newOKCommandResult();
	}
	
	/**
     * Method pasteFromString.
     * pastes the clipboard contents on to self
     * @param clipboard The clipboard contents - serialization used during copy
     * @return List The list of IView resulting from the paste
     */
	 private List pasteFromString(View view, String clipboard) {
	    ArrayList retval = new ArrayList();
	    Iterator pastedElements = EObjectUtil.deserialize(view, clipboard, Collections.EMPTY_MAP).iterator();
        while( pastedElements.hasNext() ) {
            Object element = pastedElements.next();
            if (element instanceof View) {
	            retval.add(view);
	        }
        }
        return retval;
	}

}
