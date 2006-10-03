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


package org.eclipse.gmf.runtime.diagram.ui.properties.providers;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.properties.ApplyModifiersOperation;
import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;
import org.eclipse.gmf.runtime.common.ui.services.properties.IPropertiesModifier;
import org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.IReadOnlyDiagramPropertySheetPageContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * Install this properties provider for Browse Diagrams where the selected item
 * must not be editable.
 * 
 * @author Wayne Diu, wdiu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.properties.*
 */

public class ReadOnlyDiagramPropertiesModifier
	extends AbstractProvider
	implements IPropertiesModifier {

	/**
	 * Return true if the <code>DiagramPropertiesProvider</code> would return true for
	 * this operation and we are on a Browse Diagram.
	 * 
	 * @param operation
	 *            IOperation that we will check if we provide for
	 *	 
	 */
	public boolean provides(IOperation operation) {

		if (operation instanceof ApplyModifiersOperation) {

			IWorkbench workbench = PlatformUI.getWorkbench();

			if (workbench != null) {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				if (window != null) {
					IWorkbenchPage page = window.getActivePage();
                    
                    if (page != null) {
                        IEditorPart part = page.getActiveEditor();

                        return part instanceof IReadOnlyDiagramPropertySheetPageContributor
                            || (part instanceof DiagramEditor && !((DiagramEditor) part)
                                .isWritable());
                    }

				}
			}

		}

		return false;
	}

	/**
	 * Does not allow editing the property source by write protecting
	 * descriptors. Sets the read-only attribute to <code>true</code>
	 *
	 
	 */
	public void apply(ICompositePropertySource propertySource) {
		if (propertySource != null) {
			IPropertyDescriptor[] descriptors = propertySource
				.getPropertyDescriptors();
			for (int i = 0; i < descriptors.length; i++){
				if( descriptors[i] instanceof ICompositeSourcePropertyDescriptor)
					((ICompositeSourcePropertyDescriptor) descriptors[i])
					.setReadOnly(true);
			}

		}

	}

}