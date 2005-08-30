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

import java.lang.ref.WeakReference;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IPersistableElement;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import com.ibm.xtools.notation.Diagram;

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
		final String[] name = new String[1];
		try {
			MEditingDomainGetter.getMEditingDomain(getDiagram()).runAsRead(new MRunnable() {
				public Object run() {
					name[0] = EObjectUtil.getQName(getDiagram(), false);
					return null;
				}
			});
		} catch (Exception e) {
			Trace.catching(
				DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
				getClass(),
				e.getMessage(),
				e);
			name[0] = null;
		}
		return name[0];
	}
	
	
	/**
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		final String[] name = new String[1];
		try {
			MEditingDomainGetter.getMEditingDomain(getDiagram()).runAsRead(new MRunnable() {
				public Object run() {
					name[0] = EObjectUtil.getName(getDiagram()); //((IElement) getDiagram()).getFullyQualifiedName(false);
					return null;
				}				
			});
		} catch (Exception e) {
			Trace.catching(
				DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
				getClass(),
				e.getMessage(),
				e);
			name[0] = null;
		}
		return name[0];
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
		final IFile[] file = new IFile[1];
		try {
			final MEditingDomain editingDomain = MEditingDomainGetter.getMEditingDomain(getDiagram());
			editingDomain.runAsRead(new MRunnable() {

				public Object run() {
					Resource model = getDiagram().eResource();
					String path = editingDomain.getResourceFileName(model);
					file[0] = model != null && path != null
						&& path.length() != 0 ? ResourcesPlugin.getWorkspace()
						.getRoot().getFileForLocation(
							new Path(path))
						: null;

					return null;
				}
			});
		} catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), e
					.getMessage(), e);
			file[0] = null;
		}
		return file[0];
	}

}
