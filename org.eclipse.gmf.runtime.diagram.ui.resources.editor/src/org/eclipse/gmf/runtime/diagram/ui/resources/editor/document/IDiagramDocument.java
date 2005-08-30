/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * An interface defining a <code>IDocument</code> with content 
 * of <code>org.eclipse.gmf.runtime.notation.Diagram</code>
 * 
 * @author mgoyal
 */
public interface IDiagramDocument
	extends IDocument {
	/**
	 * Returns the diagram for this diagram document
	 * @return Diagram for this document
	 */
	Diagram getDiagram();
	
	/**
	 * @return The editing domain for this diagram document
	 */
	MEditingDomain getEditingDomain();
	
	/**
	 * Sets the EditingDomain for this document
	 */
	void setEditingDomain(MEditingDomain domain);
	
	/**
	 * Detaches the attached <code>Diagram</code> from this document.
	 *  
	 * @return the detached diagram.
	 */
	Diagram detachDiagram();
	
	/**
	 * Disables the diagram dirtied listener.
	 * 
	 * @return oldState of the listener
	 */
	boolean disableDiagramListener();
	
	/**
	 * Enables the diagram dirtied listener.
	 * 
	 * @return oldstate of the listener.
	 */
	boolean enableDiagramListener();
}
