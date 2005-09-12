/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.actions;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.dialogs.PropertiesDialog;
import org.eclipse.gmf.runtime.common.ui.services.properties.extended.PropertyPagePropertyDescriptor;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.EMFPropertiesDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.EMFPropertiesPlugin;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.l10n.EMFPropertiesResourceManager;

/**
 * Action responsible for showing the properties page dialog when the it is
 * invoked.
 * <P>
 * This action is always enabled. If there are no property pages applicable to
 * the selection, a message dialog is shown to indicate that there are no
 * property pages.
 * 
 * @author ldamus
 */
public class PropertyPageViewAction
	extends Action {

	/**
	 * The title of the message dialog shown when there are no properties to be
	 * shown.
	 */
	private static final String NO_PROPERTIES_TITLE = EMFPropertiesResourceManager
		.getInstance().getString(
			"PropertyPageViewAction.NoPropertiesMessageBox.Title"); //$NON-NLS-1$    

	/**
	 * The message shown when there are no properties.
	 */
	private static final String NO_PROPERTIES_MESSAGE = EMFPropertiesResourceManager
		.getInstance().getString(
			"PropertyPageViewAction.NoPropertiesMessageBox.Message"); //$NON-NLS-1$ 

	/**
	 * The property page action label.
	 */
	private static final String PROPERTY_PAGE_ACTION_LABEL = EMFPropertiesResourceManager
		.getInstance().getString("PropertyPageViewAction.label"); //$NON-NLS-1$

	/**
	 * The property page action tooltip.
	 */
	private static final String PROPERTY_PAGE_ACTION_TOOLTIP = EMFPropertiesResourceManager
		.getInstance().getString("PropertyPageViewAction.tooltip"); //$NON-NLS-1$

	/**
	 * The property page action image.
	 */
	private static final ImageDescriptor PROPERTY_PAGE_ACTION_IMAGE = EMFPropertiesResourceManager
		.getInstance().getImageDescriptor("property_page.gif"); //$NON-NLS-1$

	/**
	 * Constructs a new action with the default label, image and tooltip.
	 */
	public PropertyPageViewAction() {
		super(PROPERTY_PAGE_ACTION_LABEL, PROPERTY_PAGE_ACTION_IMAGE);
		setToolTipText(PROPERTY_PAGE_ACTION_TOOLTIP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Trace.trace(EMFPropertiesPlugin.getDefault(),
			EMFPropertiesDebugOptions.METHODS_ENTERING,
			"PropertyPageViewActionDelegate.doRun Entering"); //$NON-NLS-1$
		IWorkbenchPage page = EMFPropertiesPlugin.getActivePage();
		if (page != null) {
			final ISelection selection = page.getSelection();
			if (selection != null && selection instanceof IStructuredSelection) {
				try {
					OperationUtil.runAsRead(new Runnable() {

						public void run() {

							// build the pages for the property dialog
							List propertyPages = getMergedPropertyPages((IStructuredSelection) selection);

							if (!propertyPages.isEmpty()) {
								// sort the pages
								Collections.sort(propertyPages,
									new Comparator() {

										public int compare(Object o1, Object o2) {
											IPreferencePage p1 = (IPreferencePage) o1;
											IPreferencePage p2 = (IPreferencePage) o2;
											String s1 = p1.getTitle();
											String s2 = p2.getTitle();
											return Collator.getInstance()
												.compare(s1, s2);
										}
									});

								// add the pages and invoke the property dialog
								PropertiesDialog dialog = new PropertiesDialog(
									Display.getCurrent().getActiveShell(),
									new PreferenceManager());

								for (Iterator iter = propertyPages.iterator(); iter
									.hasNext();) {
									dialog.getPreferenceManager().addToRoot(
										new PreferenceNode(StringStatics.BLANK,
											(IPreferencePage) iter.next()));
								}

								dialog.create();
								dialog.open();
							} else {
								MessageDialog.openInformation(Display
									.getCurrent().getActiveShell(),
									NO_PROPERTIES_TITLE, NO_PROPERTIES_MESSAGE);
							}
						}
					});
				} catch (MSLActionAbandonedException e) {
					e.printStackTrace();
				}
			}
		}
		Trace.trace(EMFPropertiesPlugin.getDefault(),
			EMFPropertiesDebugOptions.METHODS_EXITING,
			"PropertyPageViewActionDelegate.doRun Exiting"); //$NON-NLS-1$
	}

	/**
	 * Gets the property pages common to the given selection
	 * 
	 * @param selection
	 *            the selection
	 * @return List the list of property pages common to the given selection
	 */
	private List getMergedPropertyPages(IStructuredSelection selection) {
		// build the merged properties, common to the entire selection
		List mergedDescriptors = computeMergedPropertyDescriptors(selection);

		// build the merged pages from the merged properties
		List mergedPages = new ArrayList();
		if (mergedDescriptors != null) { // if there were no objects to provide
			// us with even property sources
			for (Iterator i = mergedDescriptors.iterator(); i.hasNext();) {
				PropertyPagePropertyDescriptor descriptor = (PropertyPagePropertyDescriptor) i
					.next();
				List pages = descriptor.createPropertyPages();
				pages.removeAll(mergedPages);
				mergedPages.addAll(pages);
			}
		}
		return mergedPages;
	}

	/**
	 * Return the intersection of all the <code>IPropertyDescriptor</code> s
	 * for the objects.
	 */
	private List computeMergedPropertyDescriptors(IStructuredSelection selection) {
		if (selection.size() == 0)
			return new ArrayList(0);

		// get all descriptors from each object
		Map[] propertyDescriptorMaps = new Map[selection.size()];

		Iterator i = selection.iterator();
		for (int index = 0; i.hasNext(); index++) {
			Object object = i.next();
			IPropertySource source = (IPropertySource) ((IAdaptable) object)
				.getAdapter(IPropertySource.class);

			if (source == null) {
				// if one of the selected items is not a property source
				// then we show no properties
				return new ArrayList(0);
			}
			// get the property descriptors keyed by id
			propertyDescriptorMaps[index] = computePropertyDescriptorsFor(source);
		}

		//		 intersect
		Map intersection = propertyDescriptorMaps[0];
		for (int p = 1; p < propertyDescriptorMaps.length; p++) {
			// get the current ids
			Object[] ids = intersection.keySet().toArray();
			for (int j = 0; j < ids.length; j++) {
				Object object = propertyDescriptorMaps[p].get(ids[j]);
				if (object == null
					||
					// see if the descriptors (which have the same id) are
					// compatible
					!((IPropertyDescriptor) intersection.get(ids[j]))
						.isCompatibleWith((IPropertyDescriptor) object))
					intersection.remove(ids[j]);
			}
		}

		return new ArrayList(intersection.values());
	}

	/**
	 * Returns an map of property descritptors (keyed on id) for the 
	 * given property source.
	 *
	 * @source a property source for which to obtain descriptors
	 * @return a table of decriptors keyed on their id
	 */
	private Map computePropertyDescriptorsFor(IPropertySource source) {
		IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
		Map result = new HashMap(descriptors.length * 2 + 1);
		for (int i = 0; i < descriptors.length; i++) {
			if (descriptors[i] instanceof PropertyPagePropertyDescriptor)
				result.put(descriptors[i].getId(), descriptors[i]);
		}
		return result;
	}
}