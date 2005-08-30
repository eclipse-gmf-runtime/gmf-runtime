package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * @author mgoyal
 *
 */
public interface MEditingDomainElement {
	/**
	 * @return The editing domain for this diagram document
	 */
	MEditingDomain getEditingDomain();
}
