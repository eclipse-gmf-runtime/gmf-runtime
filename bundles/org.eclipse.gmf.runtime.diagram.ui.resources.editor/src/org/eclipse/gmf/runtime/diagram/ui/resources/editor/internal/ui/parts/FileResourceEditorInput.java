/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

/**
 * An IFile based editor input that does not extend FileEditorInput.
 * 
 * @author wdiu, Wayne Diu
 */
public class FileResourceEditorInput
	implements IPathEditorInput, IPersistableElement {
	
	/**
	 * The path key for the memento
	 */
	private static final String MEMENTO_PATH_KEY = "path"; //$NON-NLS-1$
	
	/**
	 * The factory id for the IPersistableElement 
	 */
	private static final String PERSISTABLE_ELEMENT_FACTORY_ID = "XToolsEditorInputFactoryID"; //$NON-NLS-1$

	/**
	 * IFile for this editor input
	 */
	private IFile file;

	/**
	 * Constructor for FileResourceEditorInput
	 * 
	 * @param file, IFile for this editor input
	 */
	public FileResourceEditorInput(
		IFile file) {
		assert file != null;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return file.exists();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(file.getName());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return file.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return file.getFullPath().makeRelative().toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IFile.class)
			return file;
		return file.getAdapter(adapter);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPathEditorInput#getPath()
	 */
	public IPath getPath() {
		return file.getLocation();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPersistableElement#getFactoryId()
	 */
	public String getFactoryId() {
		return PERSISTABLE_ELEMENT_FACTORY_ID;
	}

	/* (non-Javadoc)
	 * Method declared on IPersistableElement.
	 */
	public void saveState(IMemento memento) {
		memento.putString(MEMENTO_PATH_KEY, file.getFullPath().toString());
	}
}
