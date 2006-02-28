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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardManager;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.common.ui.util.CustomDataTransfer;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * Abstract parent for all concrete clipboard commands used for IViews
 * 
 * @author Vishy Ramaswamy
 * @canBeSeenBy %level1
 */
public abstract class ClipboardCommand extends AbstractTransactionalCommand {
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
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param viewContext The target view used as a context for the clipboard operations
     */
    public ClipboardCommand(TransactionalEditingDomain editingDomain, View viewContext) {
        this(editingDomain, null, viewContext);
    }

    /**
     * Constructor for ClipboardCommand.
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param label The label for the command
     * @param viewContext The target view used as a context for the clipboard operations
     */
    public ClipboardCommand(TransactionalEditingDomain editingDomain, 
        String label,
        View viewContext) {
        super(editingDomain, label, getWorkspaceFiles(viewContext));

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

		// add the measurement unit in a annotation
		View firstView = (View)views.get(0);
		Diagram dgrm = firstView.getDiagram();
		EAnnotation measureUnitAnnotation  = EcoreFactory.eINSTANCE.createEAnnotation();
		measureUnitAnnotation.setSource(dgrm.getMeasurementUnit().getName());
		selection.add(measureUnitAnnotation);
		
		/* Add the elements to the selection */
		while (iter.hasNext()) {
			EObject viewElement =(View)iter.next();
			if (viewElement != null) {
				selection.add(viewElement);
			}
		}

		/* Copy the selection to the string */
		return ClipboardUtil.copyElementsToString(selection,
			Collections.EMPTY_MAP, new NullProgressMonitor()); 	
	}
}
