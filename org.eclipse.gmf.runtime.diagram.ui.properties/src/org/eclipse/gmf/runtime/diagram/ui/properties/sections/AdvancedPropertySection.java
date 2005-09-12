/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.sections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage;

import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.ui.properties.sections.UndoableModelPropertySheetEntry;

/**
 * The advanced property section displayed the "original" tabular property sheet
 * page.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public class AdvancedPropertySection extends AbstractModelerPropertySection {

    /**
     * the property sheet page for this section
     */
    protected PropertySheetPage page;

  
    /* (non-Javadoc)
     * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage)
     */
    public void createControls(final Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory()
                .createFlatFormComposite(parent);
        FormData data = null;

        String tableLabelStr = getTableLabel();
        CLabel tableLabel = null;
        if (tableLabelStr != null && tableLabelStr.length() > 0) {
            tableLabel = getWidgetFactory().createCLabel(composite,
                    tableLabelStr);
            data = new FormData();
            data.left = new FormAttachment(0, 0);
            data.top = new FormAttachment(0, 0);
            tableLabel.setLayoutData(data);
        }

        page = new PropertySheetPage();
        UndoableModelPropertySheetEntry root = new UndoableModelPropertySheetEntry(
                CommandManager.getDefault());
        root.setPropertySourceProvider(getPropertySourceProvider());
        page.setRootEntry(root);

        page.createControl(composite);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        if (tableLabel == null) {
            data.top = new FormAttachment(0, 0);
        } else {
            data.top = new FormAttachment(tableLabel, 0, SWT.BOTTOM);
        }
        data.bottom = new FormAttachment(100, 0);
        data.height = 100;
        data.width = 100;
        page.getControl().setLayoutData(data);

        setActionBars(aTabbedPropertySheetPage.getSite().getActionBars());
        
    }

    /**
     * Sets and prepares the actionBars for this section
     *  
     * @param actionBars the action bars for this page
     * @see org.eclipse.gmf.runtime.common.ui.properties.TabbedPropertySheetPage#setActionBars(org.eclipse.ui.IActionBars)
     */   
    public void setActionBars(IActionBars actionBars) {
        
        actionBars.getMenuManager().removeAll();
        actionBars.getToolBarManager().removeAll();
        actionBars.getStatusLineManager().removeAll();

        page.makeContributions(actionBars.getMenuManager(), actionBars
                .getToolBarManager(), actionBars.getStatusLineManager());
        
        actionBars.getToolBarManager().update(true);

    }

    /**
     * Returns the PropertySource provider. The default implementation returns
     * static adapter factory for the properties services. If the extending
     * class needs to use a different provider then this method has to be
     * overwriten.
     * 
     * @return The PropertySource provider
     */
    protected IPropertySourceProvider getPropertySourceProvider() {
        return propertiesProvider;
    }

    /**
     * Returns the label for the table. The default implementation returns null,
     * that is, there is no label.
     * 
     * @return The label for the table
     */
    protected String getTableLabel() {
        return null;
    }

   
    /* (non-Javadoc)
     * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    public void setInput(IWorkbenchPart part, ISelection selection) {
        page.selectionChanged(part, selection);
    }

    /* (non-Javadoc)
     * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#dispose()
     */
    public void dispose() {
        super.dispose();

        if (page != null) {
            page.dispose();
            page = null;
        }

    }

 
    /* (non-Javadoc)
     * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#refresh()
     */
    public void refresh() {

        page.refresh();
    }

   
    /* (non-Javadoc)
     * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#shouldUseExtraSpace()
     */
    public boolean shouldUseExtraSpace() {
        return true;
    }

    /**
     * Update if nessesary, upon receiving the model event.
     * 
     * @see #aboutToBeShown()
     * @see #aboutToBeHidden()
     * @param notification -
     *            even notification
     * @param element -
     *            element that has changed
     */
    public void update(final Notification notification, EObject element) {
    	if (!isDisposed()) {
			postUpdateRequest(new Runnable() {

				public void run() {
					if (!isDisposed() && !isNotifierDeleted(notification))
						refresh();
				}
			});
		}
	}

    /**
	 * Overwrites the default behavior to perform update on the create event.
	 * That must be done because proeprties of the advanced tab might be
	 * affected by creation of new elements.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.internal.events.IDemuxedMListener#handleElementCreatedEvent(org.eclipse.emf.common.notify.Notification,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
    public void handleElementCreatedEvent(Notification notification,
            EObject owner, EObject newElement) {
        update(notification, owner);
    }

    /**
     * Overwrites the default behavior to perform update on the delete event.
     * That must be done because proeprties of the advanced tab might be
     * affected by deletion of elements.
     * 
     * @see org.eclipse.gmf.runtime.emf.core.internal.events.IDemuxedMListener#handleElementDeletedEvent(org.eclipse.emf.common.notify.Notification,
     *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    public void handleElementDeletedEvent(Notification notification,
            EObject owner, EObject oldElement) {
        update(notification, owner);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#getFilter()
     */
    public MFilter getFilter() {
        MFilter lifeCycleEventFilter = new MFilter.Or(
                MFilter.ELEMENT_CREATED_FILTER,
                MFilter.ELEMENT_DELETED_FILTER);

        return new MFilter.Or(lifeCycleEventFilter,
                        MFilter.ELEMENT_MODIFIED_FILTER);
    }

   
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection#addToEObjectList(java.lang.Object)
     */
    protected boolean addToEObjectList(Object object) {
        /* not implemented */
    	return true;
    }

  
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection#handleException(org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException)
     */
    protected void handleException(MSLActionAbandonedException exception) {
        /* not implemented */
    }
}