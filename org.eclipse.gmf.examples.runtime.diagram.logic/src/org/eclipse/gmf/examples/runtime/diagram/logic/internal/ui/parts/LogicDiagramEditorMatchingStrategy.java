/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
