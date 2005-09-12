/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.TextCellEditorEx;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 */
public class LabelDirectEditPolicy
	extends DirectEditPolicy {

	/**
	 * We need an adapter that will be able to hold both a model 
	 * and an view
	 */
	class EObjectAdapterEx
		extends EObjectAdapter {

		private View view = null;

		/**
		 * constructor
		 * @param element	element to be wrapped
		 * @param view	view to be wrapped
		 */
		public EObjectAdapterEx(EObject element, View view) {
			super(element);
			this.view = view;
		}

		public Object getAdapter(Class adapter) {
			Object o = super.getAdapter(adapter);
			if (o != null)
				return o;
			if (adapter.equals(View.class)) {
				return view;
			}
			return null;
		}
	}

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest edit) {
		if (edit.getCellEditor() instanceof TextCellEditorEx)
			if (!((TextCellEditorEx) edit.getCellEditor()).hasValueChanged())
				return null;
			
		String labelText = (String) edit.getCellEditor().getValue();
		
		//for CellEditor, null is always returned for invalid values
		if (labelText == null) {
			return null;
		}
		
		TextCompartmentEditPart compartment = (TextCompartmentEditPart) getHost();
		View view = compartment.getNotationView();
		EObjectAdapter elementAdapter = null ;
		if (view !=null)
			elementAdapter = new EObjectAdapterEx(ViewUtil.resolveSemanticElement(view),
				view);
		else
			elementAdapter = new EObjectAdapterEx((EObject)compartment.getModel(),
				null);
		// check to make sure an edit has occurred before returning a command.
		String prevText = compartment.getParser().getEditString(elementAdapter,
			compartment.getParserOptions().intValue());
		if (!prevText.equals(labelText)) {
			ICommand iCommand = 
				compartment.getParser().getParseCommand(elementAdapter, labelText, 0);
			return new EtoolsProxyCommand(iCommand);
		}

		return null;
	}

	/**
	 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((TextCompartmentEditPart) getHost()).getLabel().setText(value);
	}

}