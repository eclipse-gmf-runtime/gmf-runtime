/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardManager;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.common.ui.util.CustomDataTransfer;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Abstract parent for all concrete clipboard commands used for IViews
 * 
 * @author Vishy Ramaswamy
 * @canBeSeenBy %level1
 */
public abstract class ClipboardCommand extends AbstractModelCommand {
    /**
     * String constant for the clipboard format
     */
    public static final String DRAWING_SURFACE = "Drawing Surface"; //$NON-NLS-1$

    /**
     * The target <code>View</code> used as a context for the clipboard
     * operations. The cut and copy will use this to retrieve the view model.
     * The paste will use this as the target view.
     */
    private final View viewContext;

    /**
     * Constructor for ClipboardCommand.
     * @param viewContext The target view used as a context for the clipboard operations
     */
    public ClipboardCommand(View viewContext) {
        this(null, viewContext);
    }

    /**
     * Constructor for ClipboardCommand.
     * @param label The label for the command
     * @param viewContext The target view used as a context for the clipboard operations
     */
    public ClipboardCommand(
        String label,
        View viewContext) {
        super(label, viewContext);

        Assert.isNotNull(viewContext);

        this.viewContext = viewContext;
    }

    /**
     * Copies the list of views to the system clipboard by delegating the
     * serialization exercise to the view context's <code>IViewModel</code>
     * 
     * @param source The list of views to be copied
     */
    protected void copyToClipboard(List source) {
        /* Check if the source has elements */
        if (source == null || source.size() == 0) {
            return;
        }

        /* Copy the views */
        CustomData data =
                new CustomData(
                    DRAWING_SURFACE,
                    copyViewsToString(source).getBytes());

        /* Add the data to the clipboard manager */
        if (data != null) {
            ClipboardManager.getInstance().addToCache(
                new ICustomData[] { data },
                CustomDataTransfer.getInstance());
        }
    }

    /**
     * Returns the viewContext.
     * @return IView
     */
    protected View getViewContext() {
        return viewContext;
    }

	/**
	 * convert a <code>List</code> of <code>View</code>s to a string, using
	 * the serialized representation of each view
	 * @param views
	 * @return string representation of all supplied views
	 */
	public static String copyViewsToString(List views) {
		Assert.isNotNull(views);
		Assert.isTrue(views.size() > 0);

		/* Create an empty selection */
		List selection = new ArrayList();

		/* views iterator */
		Iterator iter = views.iterator();

		/* Add the elements to the selection */
		while (iter.hasNext()) {
			EObject viewElement =(View)iter.next();
			if (viewElement != null)
				selection.add(viewElement);
		}

		/* Copy the selection to the string */
		return EObjectUtil.serialize( selection, Collections.EMPTY_MAP); 	
	}
}
