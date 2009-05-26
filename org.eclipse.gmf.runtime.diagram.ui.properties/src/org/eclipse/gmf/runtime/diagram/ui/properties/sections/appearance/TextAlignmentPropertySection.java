/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
import java.util.List;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.FontHelper;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesImages;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesMessages;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractNotationPropertiesSection;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.TextAlignment;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Property section to represent text alignment properties for a shape.
 * 
 * @author Anthony Hunter
 */
public class TextAlignmentPropertySection extends
		AbstractNotationPropertiesSection {

	protected static final String ALIGN_LEFT = DiagramUIPropertiesMessages.TextAlignmentPropertySection_AlignLeft;

	protected static final String ALIGN_CENTER = DiagramUIPropertiesMessages.TextAlignmentPropertySection_AlignCenter;

	static protected final String ALIGN_RIGHT = DiagramUIPropertiesMessages.TextAlignmentPropertySection_AlignRight;

	protected static final String TEXT_ALIGNMENT = DiagramUIPropertiesMessages.TextAlignmentPropertySection_TextAlignment;

	protected Button alignLeftButton;

	protected Button alignRightButton;

	protected Button alignCenterButton;

	protected Group textAlignmentGroup;

	/**
	 * Center align the text for the selected editparts.
	 */
	protected void alignCenter() {

		setSelectedButton(TextAlignment.CENTER_LITERAL);

		List<ICommand> commands = new ArrayList<ICommand>();
		Iterator<?> it = getInput().iterator();

		while (it.hasNext()) {
			final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();
			commands.add(createCommand(ALIGN_CENTER, ((View) ep.getModel())
					.eResource(), new Runnable() {

				public void run() {
					ep.setStructuralFeatureValue(NotationPackage.eINSTANCE
							.getTextStyle_TextAlignment(),
							TextAlignment.CENTER_LITERAL);
				}
			}));
		}

		executeAsCompositeCommand(ALIGN_CENTER, commands);

	}

	/**
	 * Left align the text for the selected editparts.
	 */
	protected void alignLeft() {

		setSelectedButton(TextAlignment.LEFT_LITERAL);

		List<ICommand> commands = new ArrayList<ICommand>();
		Iterator<?> it = getInput().iterator();

		while (it.hasNext()) {
			final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();
			commands.add(createCommand(ALIGN_LEFT, ((View) ep.getModel())
					.eResource(), new Runnable() {

				public void run() {
					ep.setStructuralFeatureValue(NotationPackage.eINSTANCE
							.getTextStyle_TextAlignment(),
							TextAlignment.LEFT_LITERAL);
				}
			}));
		}

		executeAsCompositeCommand(ALIGN_LEFT, commands);

	}

	/**
	 * Right align the text for the selected editparts.
	 */
	protected void alignRight() {

		setSelectedButton(TextAlignment.RIGHT_LITERAL);

		List<ICommand> commands = new ArrayList<ICommand>();
		Iterator<?> it = getInput().iterator();

		while (it.hasNext()) {
			final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();
			commands.add(createCommand(ALIGN_RIGHT, ((View) ep.getModel())
					.eResource(), new Runnable() {

				public void run() {
					ep.setStructuralFeatureValue(NotationPackage.eINSTANCE
							.getTextStyle_TextAlignment(),
							TextAlignment.RIGHT_LITERAL);
				}
			}));
		}

		executeAsCompositeCommand(ALIGN_RIGHT, commands);

	}

	/**
	 * Create the text alignment group.
	 * 
	 * @param parent -
	 *            parent composite
	 */
	protected void createTextAlignmentGroup(Composite parent) {
		textAlignmentGroup = getWidgetFactory().createGroup(parent,
				TEXT_ALIGNMENT);
		GridLayout layout = new GridLayout(3, false);
		textAlignmentGroup.setLayout(layout);

		alignLeftButton = new Button(textAlignmentGroup, SWT.TOGGLE);
		alignLeftButton.setImage(DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_TEXT_ALIGNMENT_LEFT));
		alignLeftButton.setToolTipText(ALIGN_LEFT);
		alignLeftButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				alignLeft();
			}
		});

		alignCenterButton = new Button(textAlignmentGroup, SWT.TOGGLE);
		alignCenterButton.setImage(DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_TEXT_ALIGNMENT_CENTER));
		alignCenterButton.setToolTipText(ALIGN_CENTER);
		alignCenterButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				alignCenter();
			}
		});

		alignRightButton = new Button(textAlignmentGroup, SWT.TOGGLE);
		alignRightButton.setImage(DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_TEXT_ALIGNMENT_RIGHT));
		alignRightButton.setToolTipText(ALIGN_RIGHT);
		alignRightButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				alignRight();
			}
		});

		setGroupWidth();
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection#digIntoGroups()
	 */
	protected boolean digIntoGroups() {
		return true;
	}
	
	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractNotationPropertiesSection#initializeControls(org.eclipse.swt.widgets.Composite)
	 */
	protected void initializeControls(Composite parent) {
		super.initializeControls(parent);
		createTextAlignmentGroup(composite);
	}

	/*
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	public void refresh() {
		super.refresh();
		if (!isDisposed()) {
			IGraphicalEditPart ep = getSingleInput();
			if (ep != null) {
				Object object = ep
						.getStructuralFeatureValue(NotationPackage.eINSTANCE
								.getTextStyle_TextAlignment());
				if (object instanceof TextAlignment) {
					setSelectedButton((TextAlignment) object);
				}
			}
		}
	}

	/**
	 * We want the width of the text alignment group to match the width of the
	 * colors and fonts group. Unfortunately there is a limitation of the tabbed
	 * properties framework not being able to make sections the same width when
	 * displayed on the same tab. To do this, we create the same widest widgets
	 * same that appear on the colors and fonts group and hide them to be 100%
	 * sure the widths of the two groups match.
	 */
	private void setGroupWidth() {
		Composite familySize = getWidgetFactory().createComposite(
				textAlignmentGroup);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.heightHint = 0;
		familySize.setLayoutData(gridData);
		familySize.setVisible(false);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		familySize.setLayout(layout);
		CCombo fontFamilyCombo = getWidgetFactory().createCCombo(familySize,
				SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		fontFamilyCombo.setItems(FontHelper.getFontNames());
		CCombo fontSizeCombo = getWidgetFactory().createCCombo(familySize,
				SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		fontSizeCombo.setItems(FontHelper.getFontSizes());
	}

	/**
	 * Set the state of the text alignment buttons based on the selected text
	 * alignment.
	 * 
	 * @param textAlignment
	 *            the selected text alignment.
	 */
	private void setSelectedButton(TextAlignment textAlignment) {
		if (TextAlignment.LEFT_LITERAL.equals(textAlignment)) {
			alignLeftButton.setSelection(true);
			alignCenterButton.setSelection(false);
			alignRightButton.setSelection(false);
		} else if (TextAlignment.CENTER_LITERAL.equals(textAlignment)) {
			alignLeftButton.setSelection(false);
			alignCenterButton.setSelection(true);
			alignRightButton.setSelection(false);
		} else if (TextAlignment.RIGHT_LITERAL.equals(textAlignment)) {
			alignLeftButton.setSelection(false);
			alignCenterButton.setSelection(false);
			alignRightButton.setSelection(true);
		}
	}
}
