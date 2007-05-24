/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.parts;

import java.lang.ref.WeakReference;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IPersistableElement;

/**
 * @author melaasar
 * @author mgoyal
 *
 * Basic implementation of the IDiagramEditorInput interface
 * based on the <code>FileEditorInput<code> implementation
 * 
 * This Editor input can be used only for diagrams that are 
 * managed by MSL. If the Diagram is floating without a resource
 * then DiagramEditorInput won't work properly.
 */
public class DiagramEditorInput implements IDiagramEditorInput {

	/** The editor's diagram */
	private WeakReference diagram;

	/**
	 * Method DiagramEditorInput.
	 * @param diagram
	 */
	public DiagramEditorInput( Diagram diagram ) {
		Assert.isNotNull(diagram);
		setDiagram(diagram);
	}

	public Diagram getDiagram() {
		return (Diagram)diagram.get();
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return false;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/**
	 * gets fully qualified name
	 * @return <code>String</code>
	 */
	public String getQName() {
		try {
			return (String) TransactionUtil.getEditingDomain(getDiagram())
				.runExclusive(new RunnableWithResult.Impl() {

					public void run() {
						setResult(EMFCoreUtil.getQualifiedName(getDiagram(), false));
					}
				});
		} catch (Exception e) {
			Trace.catching(
				DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
				getClass(),
				e.getMessage(),
				e);
			return null;
		}
	}
	
	
	/**
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		try {
			return (String) TransactionUtil.getEditingDomain(getDiagram())
				.runExclusive(new RunnableWithResult.Impl() {

					public void run() {
						setResult(EMFCoreUtil.getName(getDiagram()));
					}				
			});
		} catch (Exception e) {
			Trace.catching(
				DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
				getClass(),
				e.getMessage(),
				e);
			return null;
		}
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return getQName();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == Diagram.class)
			return getDiagram();
		if (adapter == IFile.class)
			return getStorageUnitFile();
		return null;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DiagramEditorInput))
			return false;
		DiagramEditorInput other = (DiagramEditorInput) obj;
		if(getDiagram() != null)
			return getDiagram().equals(other.getDiagram());
		else
			return getDiagram() == other.getDiagram();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if(getDiagram() != null)
			return getDiagram().hashCode();
		else
			return super.hashCode();
	}

	/**
	 * Sets the diagram.
	 * @param diagram The diagram to set
	 */
	protected void setDiagram(Diagram diagram) {
		this.diagram = new WeakReference(diagram);
	}

	/**
	  * gets the corresponding file resource in the workspace for the diagram. 
	  * Returns null if the element is not a storage unit.
	  * @return File The file resource in the workspace
	  */
	private IFile getStorageUnitFile() {
		try {
			final TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(getDiagram());
			if (editingDomain == null) {
			    return null;
			}
			return (IFile) editingDomain.runExclusive(new RunnableWithResult.Impl() {

				public void run() {
					Resource model = getDiagram().eResource();
						setResult(model != null ? WorkspaceSynchronizer
							.getFile(model)
							: null);
				}
			});
		} catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), e
					.getMessage(), e);
			return null;
		}
	}

}
