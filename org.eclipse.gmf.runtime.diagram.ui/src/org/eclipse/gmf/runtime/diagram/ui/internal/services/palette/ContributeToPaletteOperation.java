/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.palette;

import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * The contribute to palette operation
 */
public class ContributeToPaletteOperation implements IOperation {
	/** the editor hosting the palette */
	private final IEditorPart editor;
	/** the editor's contents */
	private final Object content;
	/** the palette root */
	private final PaletteRoot root;

	/**
	 * Constructs a new ContributeToPalette operation from an editor and its content
	 * 
	 * @param editor The given editor
	 * @param content The editor's contents
	 */
	public ContributeToPaletteOperation(
		IEditorPart editor,
		Object content,
		PaletteRoot root) {
		Assert.isNotNull(editor);
		Assert.isNotNull(content);
		Assert.isNotNull(root);

		this.editor = editor;
		this.content = content;
		this.root = root;
	}

	/**
	 * Gets the editor's contents
	 * 
	 * @return The editor's contents
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * Gets the editor hosting the palette
	 * 
	 * @return The editor's hosting the palette
	 */
	public IEditorPart getEditor() {
		return editor;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		((IPaletteProvider) provider).contributeToPalette(
			getEditor(),
			getContent(),
			getPaletteRoot());
		return null;
	}

	/**
	 * Gets the palette root
	 * @return
	 */
	public PaletteRoot getPaletteRoot() {
		return root;
	}

}
