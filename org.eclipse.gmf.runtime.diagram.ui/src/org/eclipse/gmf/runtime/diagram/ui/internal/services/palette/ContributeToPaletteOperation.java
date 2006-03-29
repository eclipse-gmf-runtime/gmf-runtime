/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.palette;

import java.util.Map;

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
    /** map of predefined entries, populated by each provider */
    private final Map predefinedEntries;

	/**
	 * Constructs a new ContributeToPalette operation from an editor and its content
	 * 
	 * @param editor The given editor
	 * @param content The editor's contents
	 * @param root
     * @param predefinedEntries
     *            map of predefined palette entries where the key is the palette
     *            entry id and the value is the palette entry
	 */
	public ContributeToPaletteOperation(
		IEditorPart editor,
		Object content,
		PaletteRoot root, 
        Map predefinedEntries) {
		Assert.isNotNull(editor);
		Assert.isNotNull(content);
		Assert.isNotNull(root);
        Assert.isNotNull(predefinedEntries);

		this.editor = editor;
		this.content = content;
		this.root = root;
        this.predefinedEntries = predefinedEntries;
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
			getPaletteRoot(), getPredefinedEntries());
		return null;
	}

	/**
	 * Gets the palette root
	 * @return
	 */
	public PaletteRoot getPaletteRoot() {
		return root;
	}

    /**
     * Gets the predefined entries map.
     * @return
     */
    public Map getPredefinedEntries() {
        return predefinedEntries;
    }
}
