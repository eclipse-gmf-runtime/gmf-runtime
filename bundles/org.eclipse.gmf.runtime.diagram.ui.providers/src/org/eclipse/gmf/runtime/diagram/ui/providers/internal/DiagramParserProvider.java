/******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.diagram.ui.providers.parsers.DescriptionParser;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 */
public class DiagramParserProvider 
	extends AbstractProvider
	implements IParserProvider {

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider#getParser(org.eclipse.core.runtime.IAdaptable)
	 */
	public IParser getParser(IAdaptable hint) {
		String stringHint = (String) hint.getAdapter(String.class);
		if (stringHint.equals(CommonParserHint.DESCRIPTION))
			return DescriptionParser.getInstance();
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();

			String stringHint = (String) hint.getAdapter(String.class);
			if( stringHint == null ) {
				return false;
			}

			EObject object = (EObject) hint.getAdapter(EObject.class);
			if (object instanceof View && ((View) object).getStyle(NotationPackage.eINSTANCE.getDescriptionStyle()) != null) {
				if (CommonParserHint.DESCRIPTION.equals(stringHint))
					return true;
			}
		}
		return false;
	}
}
