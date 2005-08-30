/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.ui.action.AbstractModelActionDelegate;
import com.ibm.xtools.notation.View;

/**
 * Abstract action delegate for presentation actions that work with 
 * selected <code>EObject</code> in a model.
 * 
 * @author ldamus
 */
public abstract class AbstractPresentationModelActionDelegate
	extends AbstractModelActionDelegate {

	/**
	 * Gets the viewer from the selected object.
	 * 
	 * @return the viewer.
	 */
	protected EditPartViewer getViewer() {
		return ((EditPart) getFirstSelectedObject()).getRoot().getViewer();
	}

	/**
	 * Gets the first selected object from the current structured selection.
	 * 
	 * @return the first selected object.
	 */
	protected Object getFirstSelectedObject() {
		return getStructuredSelection().getFirstElement();
	}

	/**
	 * Gets the edit part from the workbench selection.
	 * 
	 * @return the selected edit part.
	 */
	protected EditPart getEditPart() {
		return (EditPart) getFirstSelectedObject();
	}

	/**
	 * Returns the elements in the given selection.
	 *
	 * @return a list of <code>EObject</code>
	 */
	protected List getElements(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			return (List) MEditingDomainGetter.getMEditingDomain(
				((IStructuredSelection) selection).toList()).runAsRead(
				new MRunnable() {

					public Object run() {
						List retval = new ArrayList();
						if (selection instanceof IStructuredSelection) {
							IStructuredSelection structuredSelection = (IStructuredSelection) selection;

							for (Iterator i = structuredSelection.iterator(); i
								.hasNext();) {
								Object next = i.next();

								View view = (View) ((IAdaptable) next)
									.getAdapter(View.class);
								if (view != null) {
									EObject eObject = ViewUtil
										.resolveSemanticElement(view);
									if (eObject != null) {
										retval.add(eObject);
									} else {
										retval.add(view);
									}
								}
							}
						}
						return retval;
					}
				});
		}
		return Collections.EMPTY_LIST;
	}

}
