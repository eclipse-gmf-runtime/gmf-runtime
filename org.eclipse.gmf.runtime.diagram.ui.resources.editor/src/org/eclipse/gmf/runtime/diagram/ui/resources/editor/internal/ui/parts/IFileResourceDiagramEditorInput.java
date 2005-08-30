/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;

import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;


/**
 * An editor input that's based on both an IFile and a Diagram.
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 * @deprecated
 */
public interface IFileResourceDiagramEditorInput extends IDiagramEditorInput, IFileResourceEditorInput {

	//This interface contains no additional methods

}
