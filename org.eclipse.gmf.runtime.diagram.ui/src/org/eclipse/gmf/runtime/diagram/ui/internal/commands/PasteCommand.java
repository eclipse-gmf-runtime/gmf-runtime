/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import com.ibm.xtools.notation.Location;
import com.ibm.xtools.notation.Node;
import com.ibm.xtools.notation.View;

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
                    	Integer x = (Integer) ViewUtil.getPropertyValue(view,Properties.ID_POSITIONX);
                        ViewUtil.setPropertyValue(view,Properties.ID_POSITIONX, new Integer(x.intValue() + MapMode.DPtoLP(10)));
                        Integer y = (Integer) ViewUtil.getPropertyValue(view,Properties.ID_POSITIONY);
                        ViewUtil.setPropertyValue(view,Properties.ID_POSITIONY, new Integer(y.intValue() + MapMode.DPtoLP(10)));
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
