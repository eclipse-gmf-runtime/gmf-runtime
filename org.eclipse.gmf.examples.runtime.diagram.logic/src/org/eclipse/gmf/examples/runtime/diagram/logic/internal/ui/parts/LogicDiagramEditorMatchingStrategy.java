/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.ui.parts;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditorMatchingStrategy;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * @author sshaw
 *
 */
public class LogicDiagramEditorMatchingStrategy
	extends DiagramDocumentEditorMatchingStrategy {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditorMatchingStrategy#getDefaultDomain()
	 */
	public MEditingDomain getDefaultDomain() {
		return MEditingDomain.INSTANCE;
	}

}
