/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractNotationPropertiesSection;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author dlander
 */
public abstract class AbstractLabeledCheckboxPropertySection
	extends AbstractNotationPropertiesSection {

	//	Abstract methods to provide us with basic data.
	// Command info. Visible to user in undo/redo
	public abstract String getCommandName();

	// Label string positioned left of control
	public abstract String getPrefixLabel();

	// ID to use in communicating with model
	public abstract String getNameLabel();

	// List of entries to populate with
	public abstract String getID();

	// Controls
	private Button checkbox;

	private CLabel leftLabel;

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.properties.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {

		try {
			executeAsReadAction(new Runnable() {

				public void run() {

					// Update display from model
					if (getSingleInput() instanceof GraphicalEditPart) {
						GraphicalEditPart ep = (GraphicalEditPart) getSingleInput();
						ENamedElement element = PackageUtil.getElement(getID());
						if (element instanceof EStructuralFeature){
							checkbox.setSelection(((Boolean)ep
								.getStructuralFeatureValue((EStructuralFeature)element)).booleanValue());
						}
					} else if (getSingleInput() instanceof ConnectionNodeEditPart) {
						ENamedElement element = PackageUtil.getElement(getID());
						ConnectionNodeEditPart ep = (ConnectionNodeEditPart) getSingleInput();
						if (element instanceof EStructuralFeature){
							checkbox.setSelection(((Boolean) ep
								.getStructuralFeatureValue((EStructuralFeature)element)).booleanValue());
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//blank
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.properties.ISection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.gmf.runtime.common.ui.properties.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);


		FormData data;

		// Create checkbox
		checkbox = getWidgetFactory().createButton(composite, getNameLabel(),
			SWT.CHECK);
		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(parent));
		data.top = new FormAttachment(0, 0);
		checkbox.setLayoutData(data);
		checkbox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {

				if (isReadOnly()) {
					refresh();
					return;
				}

				ArrayList commands = new ArrayList();

				Iterator it = getInput().iterator();

				while (it.hasNext()) {
					final AbstractEditPart aep = (AbstractEditPart) it.next();

					Resource res = ((View)aep.getModel()).eResource();

					commands.add(createCommand(getCommandName(), res,
						new Runnable() {

							public void run() {
								if (aep instanceof GraphicalEditPart) {
									GraphicalEditPart ep = (GraphicalEditPart) aep;
									ENamedElement element = PackageUtil.getElement(getID());
									if (element instanceof EStructuralFeature)
										ep.setStructuralFeatureValue((EStructuralFeature)element, Boolean.valueOf(
											checkbox.getSelection()));
								} else if (aep instanceof ConnectionNodeEditPart) {
									ConnectionNodeEditPart ep = (ConnectionNodeEditPart) aep;
									ENamedElement element = PackageUtil.getElement(getID());
									if (element instanceof EStructuralFeature)
										ep.setStructuralFeatureValue((EStructuralFeature)element, Boolean.valueOf(
											checkbox.getSelection()));
								}
							}
						}));
				}

				executeAsCompositeCommand(getCommandName(), commands);
			}
		});

		// Create label
		leftLabel = getWidgetFactory()
			.createCLabel(composite, getPrefixLabel()); 
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(checkbox, 0, SWT.CENTER);
		leftLabel.setLayoutData(data);

	}
}

