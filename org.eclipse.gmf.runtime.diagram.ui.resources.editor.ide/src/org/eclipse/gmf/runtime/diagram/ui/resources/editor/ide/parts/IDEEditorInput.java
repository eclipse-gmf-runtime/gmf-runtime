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


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.parts;


import java.lang.ref.WeakReference;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * The editor input to use for the IDEEditor 
 * @author qili
 * @deprecated
 */
public class IDEEditorInput
	extends FileEditorInput
	implements IDiagramEditorInput {

	/** The editor's diagram */
	/** 
	 * RATLC00527385 Converting to WeakReference as DiagramEditorInput is held
	 * in the EditorHistory, which won't release the Diagram Object.
	 */
	private WeakReference diagram;

	/**
	 * Constructor
	 * @param file the file to use in this <code>IDEEditorInput</code>
	 * @param diagram the diagram use in this <code>IDEEditorInput</code>
	 */
	public IDEEditorInput(
		IFile file,
		Diagram diagram) {
		super(file);
		Assert.isNotNull(diagram);
		setDiagram(diagram);
	}

	/**
	 * Creates a new <code>IDEEditorInput</code> with the new file but with the same
	 * diagram as this one.
	 * 
	 * @param newFile the new file for the <code>IDEEditorInput</code> 
	 * @return IDEEditorInput the newly cloned <code>IDEEditorInput</code>
	 */
	public IDEEditorInput clone(IFile newFile) {
		return new IDEEditorInput(
			newFile,
			getDiagram());
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput#getDiagram()
	 */
	public Diagram getDiagram() {
		return (Diagram)diagram.get();
	}

	/**
	 * Sets the diagram.
	 * @param diagram The diagram to set
	 */
	protected void setDiagram(Diagram diagram) {
		this.diagram = new WeakReference(diagram);
	}

}
