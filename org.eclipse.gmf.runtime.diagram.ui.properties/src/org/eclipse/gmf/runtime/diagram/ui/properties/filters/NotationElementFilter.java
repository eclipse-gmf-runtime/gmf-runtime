package org.eclipse.gmf.runtime.diagram.ui.properties.filters;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A section filter which allow to remap type to notational element. For example
 * from ModelServerElement or GraphicalEditPart to an underlaying notation
 * element
 * 
 * @author nbalaba
 * 
 */
public class NotationElementFilter
	extends GenericModelElementFilter {

	/**
	 * Converts the input into an EObject, if it can be adapted. Otherwise
	 * returns null.
	 * 
	 * @param input
	 *            Input object to be converted to an EObject
	 * @return EObject converted from the input object or null if the input
	 *         object cannot be converted
	 */
	protected EObject getEObject(Object input) {

		if (input instanceof IAdaptable) {
			EObject eObj = (input instanceof IGraphicalEditPart) ? (EObject) ((IAdaptable) input)
				.getAdapter(View.class)
				: (EObject) ((IAdaptable) input).getAdapter(EObject.class);
			if (eObj != null && isSupportedMObjectType(eObj)) {
				return eObj;
			} else {
				return null;
			}
		}

		return null;
	}
}
