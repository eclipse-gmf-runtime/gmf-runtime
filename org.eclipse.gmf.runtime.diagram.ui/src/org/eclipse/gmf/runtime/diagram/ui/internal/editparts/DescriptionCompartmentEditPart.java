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

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.ICellEditorValidator;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DescriptionDirectEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

/*
 * @canBeSeenBy %level1
 */
public class DescriptionCompartmentEditPart extends TextCompartmentEditPart {

	public DescriptionCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		WrapLabel label = new WrapLabel();
		label.setTextAlignment(PositionConstants.TOP);
		label.setLabelAlignment(PositionConstants.TOP);
		label.setTextWrap(true);
		return label;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart#isEditable()
	 */
	protected boolean isEditable() {
		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
			EditPolicy.DIRECT_EDIT_ROLE,
			new DescriptionDirectEditPolicy());
	}

	/**
	 * Method getParser.
	 * @return IParser
	 */
	public IParser getParser() {
		if (parser == null) {
			View view = getNotationView();
			String parserHint = ""; //$NON-NLS-1$
			if (view!=null)
				parserHint = view.getType();
			EObject object = getPrimaryView();
			ParserHintAdapter hintAdapter =
				new ParserHintAdapter(object, parserHint);
			parser = ParserService.getInstance().getParser(hintAdapter);

		}
		return parser;
	}

	/**
	 * Returns a validator for the user's edit text
	 * @return a validator
	 */
	public ICellEditorValidator getEditTextValidator() {
		return new ICellEditorValidator() {
			public String isValid(final Object value) {
				if (value instanceof String) {
					//final IElement element = resolveModelReference();

					final IParserEditStatus isValid[] = {null};
					final IParser descParser = getParser();
					try {
						MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {

							public Object run() {
								isValid[0] = descParser.isValidEditString(null,
									(String) value);
								return null;
							}
						});
					} catch (Exception e) {
						Log.error(DiagramUIPlugin.getInstance(), IStatus.ERROR,
							e.getMessage(), e);
					}

					return isValid[0].getCode() == ParserEditStatus.EDITABLE ? null
						: isValid[0].getMessage();
				}				
				return null;
			}
		};
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart#getLabelText()
	 */
	protected String getLabelText() {
		return getParser().getPrintString(new EObjectAdapter(getPrimaryView()),
			getParserOptions().intValue());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart#getEditText()
	 */
	public String getEditText() {
		return getParser().getEditString(new EObjectAdapter(getPrimaryView()),
			getParserOptions().intValue());
	}
	
	protected void refreshLabel() {
		super.refreshLabel();
	}
}
