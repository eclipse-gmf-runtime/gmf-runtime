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

import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesImages;
import org.eclipse.gmf.runtime.notation.ArrowType;
import org.eclipse.swt.widgets.Shell;

/**
 * The menu-like pop-up widget that allows the user to select an arrow type.
 * 
 * @author Anthony Hunter
 * @since 2.1
 */
public class ArrowTypePopup extends LineStylesPopup {

	private static class Ends {
		private ArrowType source;
		private ArrowType target;

		public Ends(ArrowType source, ArrowType target) {
			this.source = source;
			this.target = target;
		}

		public ArrowType getSource() {
			return source;
		}

		public ArrowType getTarget() {
			return target;
		}
	}

	private static final Ends ARROW_NONE = new Ends(ArrowType.NONE_LITERAL,
			ArrowType.NONE_LITERAL);

	private static final Ends ARROW_SOLID_BOTH = new Ends(
			ArrowType.SOLID_ARROW_LITERAL, ArrowType.SOLID_ARROW_LITERAL);

	private static final Ends ARROW_SOLID_SOURCE = new Ends(
			ArrowType.SOLID_ARROW_LITERAL, ArrowType.NONE_LITERAL);

	private static final Ends ARROW_SOLID_TARGET = new Ends(
			ArrowType.NONE_LITERAL, ArrowType.SOLID_ARROW_LITERAL);

	private static final Ends ARROW_OPEN_BOTH = new Ends(
			ArrowType.OPEN_ARROW_LITERAL, ArrowType.OPEN_ARROW_LITERAL);

	private static final Ends ARROW_OPEN_SOURCE = new Ends(
			ArrowType.OPEN_ARROW_LITERAL, ArrowType.NONE_LITERAL);

	private static final Ends ARROW_OPEN_TARGET = new Ends(
			ArrowType.NONE_LITERAL, ArrowType.OPEN_ARROW_LITERAL);

	/**
	 * Constructor for ArrowTypePopup.
	 * 
	 * @param parent
	 *            the parent shell.
	 */
	public ArrowTypePopup(Shell parent) {
		super(parent);
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.LineStylesPopup#initializeImageMap()
	 */
	protected void initializeImageMap() {
		imageMap.put(ARROW_NONE, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_NONE));
		imageMap.put(ARROW_OPEN_BOTH, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_OPEN_BOTH));
		imageMap.put(ARROW_OPEN_SOURCE, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_OPEN_SOURCE));
		imageMap.put(ARROW_OPEN_TARGET, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_OPEN_TARGET));
		imageMap.put(ARROW_SOLID_BOTH, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_SOLID_BOTH));
		imageMap.put(ARROW_SOLID_SOURCE, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_SOLID_SOURCE));
		imageMap.put(ARROW_SOLID_TARGET, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_ARROW_SOLID_TARGET));
	}

	/**
	 * Gets the arrow decoration type for the source the user selected. Could
	 * return null as the user may cancel the gesture.
	 * 
	 * @return the selected arrow type or null.
	 */
	public ArrowType getSelectedArrowTypeSource() {
		if (getSelectedItem() == null) {
			return null;
		} else {
			Ends selectedEnds = (Ends) getSelectedItem();
			return selectedEnds.getSource();
		}
	}

	/**
	 * Gets the arrow decoration type for the target the user selected. Could
	 * return null as the user may cancel the gesture.
	 * 
	 * @return the selected arrow type or null.
	 */
	public ArrowType getSelectedArrowTypeTarget() {
		if (getSelectedItem() == null) {
			return null;
		} else {
			Ends selectedEnds = (Ends) getSelectedItem();
			return selectedEnds.getTarget();
		}
	}
}
