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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;

import java.lang.ref.WeakReference;

import org.eclipse.core.resources.IFile;

import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * Implementation of an editor input that's based on both an IFile and a
 * Diagram.
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 */
public class FileResourceDiagramEditorInput extends FileResourceEditorInput implements IFileResourceDiagramEditorInput {
	
	/**
	 * The editor's diagram stored as a weak reference
	 */
	private WeakReference diagram;
	
	/**
	 * Constructor for FileResourceEditorInput
	 * 
	 * @param file, IFile for this editor input
	 * @param diagram, Diagram for this editor input
	 */
	public FileResourceDiagramEditorInput(
		IFile file,
		Diagram diagram) {
		
		super(file);
		
		assert diagram != null;
		setDiagram(diagram);
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
	
	/**
	 * Method clone.
	 * 
	 * @param newFile, clone this editor input to the specified IFile, keeping
	 * the same diagram object.
	 * @return Object
	 */
	public Object clone(IFile newFile) {
		return new FileResourceDiagramEditorInput(
			newFile, getDiagram());
	}	

}
