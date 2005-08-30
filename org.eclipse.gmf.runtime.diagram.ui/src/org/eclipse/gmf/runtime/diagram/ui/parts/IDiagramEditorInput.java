/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.ui.IEditorInput;

import com.ibm.xtools.notation.Diagram;

/**
 * Diagram Editor Input interface.
 * 
 * @author melaasar  
 */
public interface IDiagramEditorInput extends IEditorInput {

	/**
	 * Method getDiagram.
	 * @return Diagram
	 */
	public Diagram getDiagram();

}
