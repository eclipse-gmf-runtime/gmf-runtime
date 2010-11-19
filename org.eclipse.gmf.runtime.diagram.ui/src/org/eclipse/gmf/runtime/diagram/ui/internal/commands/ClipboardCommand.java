/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardManager;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.common.ui.util.CustomDataTransfer;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Abstract parent for all concrete clipboard commands used for IViews
 * 
 * @author Vishy Ramaswamy
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
		
		/* Add the elements to the selection */
		while (iter.hasNext()) {
			EObject viewElement =(View)iter.next();
			if (viewElement != null) {
				selection.add(viewElement);
			}
		}
		
		/*
		 * We must append all inner edges of a node being copied. Edges are non-containment
		 * references, hence they won't be copied for free. Therefore, we add them here to
		 * the list of views to copy.
		 */
		List<Edge> innerEdges = new LinkedList<Edge>();
		for (Iterator itr = views.iterator(); itr.hasNext();) {
			View view = (View) itr.next();
			if (!(view instanceof Diagram)) {
				innerEdges.addAll(ViewUtil.getAllInnerEdges(view));
			}
		}
		selection.addAll(innerEdges);

		// add the measurement unit in an annotation.  Put it in the last position
		//   to work around a limitation in the copy/paste infrastructure, that
		//   selects the ClipboardSupportFactory based on the first element in
		//   the copy list.  If the annotation is first, then we get the wrong
		//   clipboard support instance
		View firstView = (View)views.get(0);
		Diagram dgrm = firstView.getDiagram();
		EAnnotation measureUnitAnnotation  = EcoreFactory.eINSTANCE.createEAnnotation();
		measureUnitAnnotation.setSource(dgrm.getMeasurementUnit().getName());
		selection.add(measureUnitAnnotation);

		/* Copy the selection to the string */
		return ClipboardUtil.copyElementsToString(selection,
			new HashMap(), new NullProgressMonitor()); 	
	}
}
