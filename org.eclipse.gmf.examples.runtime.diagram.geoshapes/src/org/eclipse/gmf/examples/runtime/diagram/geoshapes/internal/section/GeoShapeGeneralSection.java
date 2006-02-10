/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.section;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n.ExampleDiagramGeoshapeMessages;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.TextChangeHelper;
import org.eclipse.gmf.runtime.notation.DescriptionStyle;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Eliza Tworkowska
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 */
public class GeoShapeGeneralSection
	extends AbstractModelerPropertySection {

	// Labels
	protected static final String DESCRIPTION_LABEL = ExampleDiagramGeoshapeMessages.GeoShapeGeneralDetails_GeoShapeDescriptionLabel_text;

	// properties
	protected static final String DESCRIPTION = ExampleDiagramGeoshapeMessages.GeoShapeGeneralDetails_GeoShapeDescriptionChangeCommand_text;

	// commads - used as a label for Edit -> Undo/Redo, used message format for
	// concatenating strings
	private static final String DESCRIPTION_PROPERTY_CHANGE_COMMAND_NAME = DESCRIPTION + VALUE_CHANGED_STRING;

	/** Text to be displayed in the description text field. */
	private Text _descriptionText = null;

	/** The description text cached from the model. */
	private String _descriptionCache = null;

	/**
	 * Listener for changes to text field events.
	 */
	private TextChangeHelper listener = new TextChangeHelper() {

		public void textChanged(Control control) {
			if (control.equals(_descriptionText)) {
				setDescription();
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.properties.ISection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.gmf.runtime.common.ui.properties.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);

		Composite composite = getWidgetFactory().createFlatFormComposite(parent);
		
		// FormData is a layout
		FormData data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(parent, new String[] {DESCRIPTION_LABEL}));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		
		_descriptionText = getWidgetFactory().createText(composite, StringStatics.BLANK);
		_descriptionText.setLayoutData(data);

		CLabel descriptionLabel = getWidgetFactory().createCLabel(composite,DESCRIPTION_LABEL);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(_descriptionText,
			-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(_descriptionText, 0, SWT.CENTER);
		descriptionLabel.setLayoutData(data);

		// Attach listener
		
		// when user starts typing things
		listener.startListeningTo(_descriptionText);
		listener.startListeningForEnter(_descriptionText);

		//if (isReadOnly()) {
		//	_descriptionText.setEditable(false);
		//}
	}

	/**
	 * When is this method called: after user presses Enter in a name field.
	 * What it does: updates the model.
	 */
	protected synchronized void setDescription() {

		System.out.println("===>setDescription"); //$NON-NLS-1$
		
		final String newDescription = _descriptionText.getText();	
		System.out.println(newDescription);
		
		if (!newDescription.equals(_descriptionCache)) {
			// 
			// 1. Name of the command
			// 2. Resource that will be modified as a result of running this commands.
			// 3. Runnable - logic of the command.
			//
			String name = DESCRIPTION_PROPERTY_CHANGE_COMMAND_NAME;
			Resource diagram = null;
			Runnable r = new Runnable() {
				public void run() {
					//System.out.println("===>In runnable");
				
					// Get eObject associated with current selection
					EObject vEObject = (EObject) getEObjectList().get(0);
				
					// Our current selection will be a node = ex. triangle
					View node = (View) vEObject;
				
					// Get diagram on which this node is visualized
					//Diagram d = node.getDiagram();
	
					EClass eClass = NotationPackage.eINSTANCE.getDescriptionStyle();
				
					Style style = node.getStyle(eClass);
					DescriptionStyle description = (DescriptionStyle) style;
				
					if (description != null)
						description.setDescription(newDescription);
				
					System.out.println("Done"); //$NON-NLS-1$
				
				}
			};
		
			//System.out.println("SIZE: " + getEObjectList().size());
		
		
			// Create 
			ICommand c = createCommand(name, diagram, r);
		
			ArrayList commands = new ArrayList();
			commands.add(c);
		
			executeAsCompositeCommand(DESCRIPTION_PROPERTY_CHANGE_COMMAND_NAME, commands);
			
			_descriptionCache = newDescription;
		}
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.common.ui.properties.view.ISection#dispose()
	 */
	public void dispose() {
		listener.stopListeningTo(_descriptionText);
		super.dispose();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.properties.view.ITabbedPropertySection#refresh()
	 */

	public void refresh() {
		System.out.println("===>Refresh"); //$NON-NLS-1$
		
		listener.startNonUserChange();
		try {
			executeAsReadAction(new Runnable() {

				public void run() {
						
					DescriptionStyle description = (DescriptionStyle) ((Node) getEObject())
						.getStyle(NotationPackage.eINSTANCE
							.getDescriptionStyle());

					//String current = description.getDescription();
					
					if (description != null) {
						_descriptionText.setText(description.getDescription());
					}
					
					//System.out.println("Finished refresh");

				}
			});
		} finally {
			listener.finishNonUserChange();
		}
	}
}
