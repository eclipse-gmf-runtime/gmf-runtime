/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesImages;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesMessages;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.ArrowType;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.LineType;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Tabbed property section to add line styles to the colours and fonts property
 * section.
 * 
 * @author ahunter
 */
public class ColoursAndFontsAndLineStylesPropertySection extends
		ColorsAndFontsPropertySection {

	protected static final String LINE_WIDTH_COMMAND_NAME = DiagramUIPropertiesMessages.LineStylesPropertySection_LineWidth
			+ StringStatics.SPACE + VALUE_CHANGED_STRING;

	protected static final String LINE_TYPE_COMMAND_NAME = DiagramUIPropertiesMessages.LineStylesPropertySection_LineType
			+ StringStatics.SPACE + VALUE_CHANGED_STRING;

	protected static final String ARROW_TYPE_COMMAND_NAME = DiagramUIPropertiesMessages.LineStylesPropertySection_ArrowType
			+ StringStatics.SPACE + VALUE_CHANGED_STRING;

	protected static final String SOURCE_ARROW_TYPE_COMMAND_NAME = DiagramUIPropertiesMessages.LineStylesPropertySection_SourceArrow_Type
			+ StringStatics.SPACE + VALUE_CHANGED_STRING;

	protected static final String TARGET_ARROW_TYPE_COMMAND_NAME = DiagramUIPropertiesMessages.LineStylesPropertySection_TargetArrow_Type
			+ StringStatics.SPACE + VALUE_CHANGED_STRING;

	protected Group lineStylesGroup;

	protected Button lineWidthButton;

	protected Button lineTypeButton;

	protected Button arrowTypeButton;

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractNotationPropertiesSection#initializeControls(org.eclipse.swt.widgets.Composite)
	 */
	protected void initializeControls(Composite parent) {
		composite = getWidgetFactory().createFlatFormComposite(parent);
		FormLayout layout = (FormLayout) composite.getLayout();
		layout.spacing = 3;

		Composite groups = getWidgetFactory().createComposite(composite);
		groups.setLayout(new GridLayout(2, false));
		createFontsAndColorsGroups(groups);
		colorsAndFontsGroup.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));
		createLineStylesGroup(groups);
		lineStylesGroup.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));
	}

	/**
	 * Create the line styles group.
	 * 
	 * @param parent -
	 *            parent composite
	 */
	protected void createLineStylesGroup(Composite parent) {
		lineStylesGroup = getWidgetFactory()
				.createGroup(
						parent,
						DiagramUIPropertiesMessages.LineStylesPropertySection_LineStyles);
		GridLayout layout = new GridLayout(3, false);
		lineStylesGroup.setLayout(layout);

		lineWidthButton = new Button(lineStylesGroup, SWT.PUSH);
		lineWidthButton.setImage(DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_WIDTH));
		lineWidthButton
				.setToolTipText(DiagramUIPropertiesMessages.LineStylesPropertySection_LineWidth);
		lineWidthButton.getAccessible().addAccessibleListener(
				new AccessibleAdapter() {
					public void getName(AccessibleEvent e) {
						e.result = DiagramUIPropertiesMessages.LineStylesPropertySection_LineWidth;
					}
				});
		lineWidthButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				changeLineWidth(lineWidthButton);
			}
		});

		lineTypeButton = new Button(lineStylesGroup, SWT.PUSH);
		lineTypeButton.setImage(DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_TYPE));
		lineTypeButton
				.setToolTipText(DiagramUIPropertiesMessages.LineStylesPropertySection_LineType);
		lineTypeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				changeLineType(lineTypeButton);
			}
		});
		lineTypeButton.getAccessible().addAccessibleListener(
				new AccessibleAdapter() {
					public void getName(AccessibleEvent e) {
						e.result = DiagramUIPropertiesMessages.LineStylesPropertySection_LineType;
					}
				});

		arrowTypeButton = new Button(lineStylesGroup, SWT.PUSH);
		arrowTypeButton.setImage(DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_TYPE));
		arrowTypeButton
				.setToolTipText(DiagramUIPropertiesMessages.LineStylesPropertySection_ArrowType);
		arrowTypeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				changeArrowType(arrowTypeButton);
			}
		});
		arrowTypeButton.getAccessible().addAccessibleListener(
				new AccessibleAdapter() {
					public void getName(AccessibleEvent e) {
						e.result = DiagramUIPropertiesMessages.LineStylesPropertySection_ArrowType;
					}
				});
	}

	/**
	 * Change the arrow type.
	 * 
	 * @param button
	 *            the button used to place the menu.
	 */
	protected void changeArrowType(Button button) {

		ArrowTypePopup popup = new ArrowTypePopup(button.getParent().getShell());
		Rectangle r = button.getBounds();
		Point location = button.getParent().toDisplay(r.x, r.y);
		popup.open(new Point(location.x, location.y + r.height));
		if (popup.getSelectedArrowTypeSource() == null) {
			return;
		}
		final ArrowType selectedArrowTypeSource = popup
				.getSelectedArrowTypeSource();
		final EStructuralFeature arrowTypeSourceFeature = (EStructuralFeature) PackageUtil
				.getElement(Properties.ID_ARROW_SOURCE);
		final ArrowType selectedArrowTypeTarget = popup
				.getSelectedArrowTypeTarget();
		final EStructuralFeature arrowTypeTargetFeature = (EStructuralFeature) PackageUtil
				.getElement(Properties.ID_ARROW_TARGET);

		List commands = new ArrayList();
		Iterator it = getInput().iterator();

		while (it.hasNext()) {
			final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();

			commands.add(createCommand(SOURCE_ARROW_TYPE_COMMAND_NAME,
					((View) ep.getModel()).eResource(), new Runnable() {

						public void run() {
							ENamedElement element = PackageUtil
									.getElement(Properties.ID_ARROW_SOURCE);
							if (element instanceof EStructuralFeature)
								ep.setStructuralFeatureValue(
										arrowTypeSourceFeature,
										selectedArrowTypeSource);
						}
					}));
			commands.add(createCommand(TARGET_ARROW_TYPE_COMMAND_NAME,
					((View) ep.getModel()).eResource(), new Runnable() {

						public void run() {
							ENamedElement element = PackageUtil
									.getElement(Properties.ID_ARROW_TARGET);
							if (element instanceof EStructuralFeature)
								ep.setStructuralFeatureValue(
										arrowTypeTargetFeature,
										selectedArrowTypeTarget);
						}
					}));
		}

		if (!commands.isEmpty()) {
			executeAsCompositeCommand(ARROW_TYPE_COMMAND_NAME, commands);
		}
	}

	/**
	 * Change the line type.
	 * 
	 * @param button
	 *            the button used to place the menu.
	 */
	protected void changeLineType(Button button) {

		LineTypePopup popup = new LineTypePopup(button.getParent().getShell());
		Rectangle r = button.getBounds();
		Point location = button.getParent().toDisplay(r.x, r.y);
		popup.open(new Point(location.x, location.y + r.height));
		if (popup.getSelectedLineType() == null) {
			return;
		}
		final LineType selectedLineType = popup.getSelectedLineType();
		final EStructuralFeature feature = (EStructuralFeature) PackageUtil
				.getElement(Properties.ID_LINE_TYPE);

		List commands = new ArrayList();
		Iterator it = getInput().iterator();

		while (it.hasNext()) {
			final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();

			commands.add(createCommand(LINE_TYPE_COMMAND_NAME, ((View) ep
					.getModel()).eResource(), new Runnable() {

				public void run() {
					ENamedElement element = PackageUtil
							.getElement(Properties.ID_LINE_TYPE);
					if (element instanceof EStructuralFeature)
						ep.setStructuralFeatureValue(feature, selectedLineType);
				}
			}));
		}
		if (!commands.isEmpty()) {
			executeAsCompositeCommand(LINE_TYPE_COMMAND_NAME, commands);
		}
	}

	/**
	 * Change the line width.
	 * 
	 * @param button
	 *            the button used to place the menu.
	 */
	protected void changeLineWidth(Button button) {

		LineWidthPopup popup = new LineWidthPopup(button.getParent().getShell());
		Rectangle r = button.getBounds();
		Point location = button.getParent().toDisplay(r.x, r.y);
		popup.open(new Point(location.x, location.y + r.height));
		if (popup.getSelectedLineWidth() == -1) {
			return;
		}
		final int selectedLineWidth = popup.getSelectedLineWidth();
		final EStructuralFeature feature = (EStructuralFeature) PackageUtil
				.getElement(Properties.ID_LINE_WIDTH);

		List commands = new ArrayList();
		Iterator it = getInput().iterator();

		while (it.hasNext()) {
			final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();

			commands.add(createCommand(LINE_WIDTH_COMMAND_NAME, ((View) ep
					.getModel()).eResource(), new Runnable() {

				public void run() {
					ENamedElement element = PackageUtil
							.getElement(Properties.ID_LINE_WIDTH);
					if (element instanceof EStructuralFeature)
						ep
								.setStructuralFeatureValue(feature,
										selectedLineWidth);
				}
			}));
		}
		if (!commands.isEmpty()) {
			executeAsCompositeCommand(LINE_WIDTH_COMMAND_NAME, commands);
		}
	}

	/*
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	public void refresh() {
		super.refresh();
		if (!isDisposed()) {
			executeAsReadAction(new Runnable() {

				public void run() {

					IGraphicalEditPart graphicalEditPart = getSingleInput();
					if (graphicalEditPart != null) {
						boolean isReadOnly = isReadOnly();

						LineStyle lineStyle = (LineStyle) graphicalEditPart
								.getNotationView().getStyle(
										NotationPackage.eINSTANCE
												.getLineStyle());
						boolean enableLineWidth = (lineStyle != null)
								&& !isReadOnly
								&& (lineStyle.getLineWidth() != -1);
						lineWidthButton.setEnabled(enableLineWidth);

						Style lineTypeStyle = graphicalEditPart
								.getNotationView().getStyle(
										NotationPackage.eINSTANCE
												.getLineTypeStyle());
						boolean enableLineType = (lineTypeStyle != null)
								&& !isReadOnly;
						lineTypeButton.setEnabled(enableLineType);

						Style arrowTypeStyle = graphicalEditPart
								.getNotationView().getStyle(
										NotationPackage.eINSTANCE
												.getArrowStyle());
						boolean enableArrowType = (arrowTypeStyle != null)
								&& !isReadOnly;
						arrowTypeButton.setEnabled(enableArrowType);
					}
				}
			});
		}
	}
}
