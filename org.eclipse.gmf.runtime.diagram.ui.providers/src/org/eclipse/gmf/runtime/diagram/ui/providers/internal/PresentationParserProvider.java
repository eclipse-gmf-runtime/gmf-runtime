/*
 * Created on Mar 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.parsers.DescriptionParser;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PresentationParserProvider 
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
