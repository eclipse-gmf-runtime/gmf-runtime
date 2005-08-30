/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.properties.sections;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.ResourceManager;
import com.ibm.xtools.notation.DescriptionStyle;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.View;

/**
 * The general section displayed for diagrams in the properties view
 * 
 * @author nbalaba
 */
public class DiagramGeneralSection
	extends AbstractBasicTextPropertySection {

	/** Name label for diagram general section. */
	protected static final String NAME_LABEL = ResourceManager
		.getI18NString("DiagramGeneralDetails.nameLabel.text"); //$NON-NLS-1$

	/** Type label. */
	protected static final String DIAGRAM_TYPE_LABEL = ResourceManager
		.getI18NString("DiagramGeneralDetails.diagramTypeLabel.text"); //$NON-NLS-1$

	/** Description label. */
	protected static final String DESCRIPTION_LABEL = ResourceManager
		.getI18NString("DiagramGeneralDetails.diagramDescriptionLabel.text"); //$NON-NLS-1$

	/**
	 * The labels used by the General Diagram section
	 */
	public static final String[] GENERAL_DIAGRAM_LABELS = {NAME_LABEL,
		DIAGRAM_TYPE_LABEL, DESCRIPTION_LABEL};

	/** Name label. */
	protected static final String NAME = ResourceManager
		.getI18NString("DiagramGeneralDetails.nameChangeCommand.text"); //$NON-NLS-1$

	/** Description label. */
	protected static final String DESCRIPTION = ResourceManager
		.getI18NString("DiagramGeneralDetails.diagramDescriptionChangeCommand.text"); //$NON-NLS-1$

	// commads
	private static final String NAME_PROPERTY_CHANGE_COMMAND_NAME = NAME
		+ VALUE_CHANGED_STRING;

	private static final String DESCRIPTION_PROPERTY_CHANGE_COMMAND_NAME = DESCRIPTION
		+ VALUE_CHANGED_STRING;

	/**
	 * The Type label field.
	 */
	private CLabel typeText;

	/**
	 * The description label field.
	 */
	private Text descriptionText;

	/**
	 * The Name text cached from the model.
	 */
	private String descriptionCache;

	/**
	 * User pressed Enter key after editing name field - update the model
	 */
	protected synchronized void setDescription() {

		final String newDescription = descriptionText.getText();
		if (!newDescription.equals(descriptionCache)) {
			ArrayList commands = new ArrayList();

			for (Iterator i = getEObjectList().iterator(); i.hasNext();) {
				final EObject next = (EObject) i.next();
				commands.add(createCommand(
					DESCRIPTION_PROPERTY_CHANGE_COMMAND_NAME, next,
					new Runnable() {

						public void run() {
							DescriptionStyle description = (DescriptionStyle) ((Diagram) next)
								.getStyle(NotationPackage.eINSTANCE
									.getDescriptionStyle());
							if (description != null)
								description.setDescription(newDescription);
						}
					}));
			}

			executeAsCompositeCommand(DESCRIPTION_PROPERTY_CHANGE_COMMAND_NAME,
				commands);

			descriptionCache = newDescription;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#dispose()
	 */
	public void dispose() {
		getListener().stopListeningTo(descriptionText);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#refreshUI()
	 */
	protected void refreshUI() {
		super.refreshUI();

		Diagram diagram = (Diagram) getEObject();

		typeText.setText(getDiagramType(diagram));

		DescriptionStyle description = (DescriptionStyle) diagram
			.getStyle(NotationPackage.eINSTANCE.getDescriptionStyle());

		if (description != null)
			descriptionText.setText(description.getDescription());

	}

	/**
	 * Gets the string to be displayed in the diagram type field.
	 * @param diagram the diagram in question
	 * @return the string to be displayed
	 */
	protected String getDiagramType(Diagram diagram) {
		return diagram.getType();
	}

	/**
	 * Enforce the correct type selection - we are only intersted in Diagram
	 * objects
	 * 
	 */
	protected EObject unwrap(Object object) {

		EObject o = super.unwrap(object);
		if (o instanceof Diagram)
			return o;

		return null;
	}

	/**
	 * Adapt the object to an EObject - if possible
	 * 
	 * @param object
	 *            object from a diagram or ME
	 * @return EObject
	 */
	protected EObject adapt(Object object) {
		if (object instanceof IAdaptable) {
			if (object instanceof IGraphicalEditPart)// diagram case
				return (EObject) ((IAdaptable) object).getAdapter(View.class);
			// ME case
			return (EObject) ((IAdaptable) object).getAdapter(EObject.class);

		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection#isCurrentSelection(org.eclipse.emf.common.notify.Notification,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	protected boolean isCurrentSelection(Notification notification,
			EObject element) {
		if (getEObjectList().size() > 0) {
			EObject eventObject = element;

			// check for annotations
			if (element instanceof EAnnotation || element instanceof Diagram) {
				eventObject = element.eContainer();
			}

			if (eventObject == null) {
				// the annotation has been removed - check the old owner
				Object tmpObj = notification.getOldValue();
				if (tmpObj != null && tmpObj instanceof EObject) {
					eventObject = (EObject) tmpObj;
				} else {
					return false;
				}
			}

			return getEObjectList().contains(eventObject);

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#getPropertyNameLabel()
	 */
	protected String getPropertyNameLabel() {
		return NAME_LABEL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#setPropertyValue(org.eclipse.swt.widgets.Control)
	 */
	protected void setPropertyValue(Control control) {
		if (control == getTextWidget())
			super.setPropertyValue(control);
		else
			setDescription();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#setPropertyValue(org.eclipse.emf.ecore.EObject,
	 *      java.lang.String)
	 */
	protected void setPropertyValue(EObject object, Object value) {
		((Diagram) object).setName((String) value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#getPropertyValueString()
	 */
	protected String getPropertyValueString() {
		return ((Diagram) getEObject()).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#getPropertyChangeCommandName()
	 */
	protected String getPropertyChangeCommandName() {
		return NAME_PROPERTY_CHANGE_COMMAND_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractBasicTextPropertySection#getPropertyNameStringsArray()
	 */
	protected String[] getPropertyNameStringsArray() {
		return GENERAL_DIAGRAM_LABELS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.properties.ISection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.gmf.runtime.common.ui.properties.TabbedPropertySheetPage)
	 */
	public void doCreateControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.doCreateControls(parent, aTabbedPropertySheetPage);

		FormData data;

		typeText = getWidgetFactory().createCLabel(getSectionComposite(),
			StringStatics.BLANK);
		data = new FormData();
		data.left = new FormAttachment(getTextWidget(), 0, SWT.LEFT);
		data.right = new FormAttachment(getTextWidget(), 0, SWT.RIGHT);
		data.top = new FormAttachment(getTextWidget(),
			ITabbedPropertyConstants.VSPACE, SWT.BOTTOM);
		typeText.setLayoutData(data);

		CLabel typeLabel = getWidgetFactory().createCLabel(
			getSectionComposite(), DIAGRAM_TYPE_LABEL);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeText,
			-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(typeText, 0, SWT.CENTER);
		typeLabel.setLayoutData(data);

		descriptionText = getWidgetFactory().createText(getSectionComposite(),
			StringStatics.BLANK,
			SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		data = new FormData();
		data.left = new FormAttachment(typeText, 0, SWT.LEFT);
		data.right = new FormAttachment(typeText, 0, SWT.RIGHT);
		data.top = new FormAttachment(typeText,
			ITabbedPropertyConstants.VSPACE, SWT.BOTTOM);
		data.bottom = new FormAttachment(100, 0);
		data.height = 100;
		data.width = 100;
		descriptionText.setLayoutData(data);

		CLabel descriptionLabel = getWidgetFactory().createCLabel(
			getSectionComposite(), DESCRIPTION_LABEL);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(descriptionText,
			-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(typeText, 0, SWT.LEFT);

		descriptionLabel.setLayoutData(data);

		getListener().startListeningTo(descriptionText);
		getListener().startListeningForEnter(descriptionText);

		if (isReadOnly())
			descriptionText.setEditable(false);

	}
}