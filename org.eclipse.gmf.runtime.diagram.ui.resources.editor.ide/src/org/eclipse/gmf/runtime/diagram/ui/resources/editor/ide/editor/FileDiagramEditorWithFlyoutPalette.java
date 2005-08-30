/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * FileDiagramEditor With a flyout palette
 * @author mgoyal
 *
 */
public class FileDiagramEditorWithFlyoutPalette
	extends FileDiagramEditor {

	/**
	 * Constructs a FileDiagramEditorWithFlyoutPalette and default
	 * editing domain.
	 */
	public FileDiagramEditorWithFlyoutPalette() {
		this(MEditingDomain.INSTANCE);
	}

	/**
	 * Constructs a FileDiagramEditor with Flyout Palette and any given
	 * editing domain.
	 * 
	 * @param domain EditingDomain to be used.
	 */
	public FileDiagramEditorWithFlyoutPalette(MEditingDomain domain) {
		super(domain, true);
	}
}
