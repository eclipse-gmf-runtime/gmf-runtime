/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DescriptionDirectEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * @since 1.2
 */
public class DescriptionCompartmentEditPart extends TextCompartmentEditPart {

	public DescriptionCompartmentEditPart(View view) {
		super(view);
	}

	protected ILabelDelegate createLabelDelegate() {
        ILabelDelegate labelDelegate = super.createLabelDelegate();
        labelDelegate.setTextJustification(PositionConstants.LEFT);
        labelDelegate.setAlignment(PositionConstants.TOP);
        labelDelegate.setTextAlignment(PositionConstants.TOP);
        labelDelegate.setTextWrapOn(true);        
        return labelDelegate;
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

					final IParser descParser = getParser();
					try {
						IParserEditStatus isValid = (IParserEditStatus) getEditingDomain()
							.runExclusive(new RunnableWithResult.Impl() {

									public void run() {
										setResult(descParser.isValidEditString(
											null, (String) value));
									}
								});
						return isValid.getCode() == ParserEditStatus.EDITABLE ? null
							: isValid.getMessage();
					} catch (Exception e) {
						Log.error(DiagramUIPlugin.getInstance(), IStatus.ERROR,
							e.getMessage(), e);
					}
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
