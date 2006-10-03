/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * An implementation of <code>IDiagramDocument</code>,
 * 
 * @author mgoyal
 */
public final class DiagramDocument
	extends AbstractDocument
	implements IDiagramDocument {

	public DiagramDocument() {
		completeInitialization();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDiagramDocument#getDiagram()
	 */
	public Diagram getDiagram() {
		return (Diagram)getContent();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.diagramdocument.IDiagramDocument#detachDiagram()
	 */
	public Diagram detachDiagram() {
		Object oldContent = content;
		content = null;
		return (Diagram)oldContent;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.AbstractDocument#completeInitialization()
	 */
	protected void completeInitialization() {
		super.completeInitialization();
	}

	/**
	 * The editing domain for this document.
	 */
	private TransactionalEditingDomain fDomain = null;
	
	public TransactionalEditingDomain getEditingDomain() {
		return fDomain;
	}

	public void setEditingDomain(TransactionalEditingDomain domain) {
		fDomain = domain;
	}
}
