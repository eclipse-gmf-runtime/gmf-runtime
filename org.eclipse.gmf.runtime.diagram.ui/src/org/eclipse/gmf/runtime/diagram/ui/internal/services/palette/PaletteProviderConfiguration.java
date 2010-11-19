/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.palette;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;
import org.eclipse.ui.IEditorPart;

/**
 * @author melaasar
 *
 * A provider configuration for the PaletteService. It helps in filtering out
 * and delay loading unrelated providers.
 */
public class PaletteProviderConfiguration extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String EDITOR = "editor"; //$NON-NLS-1$
	private static final String CONTENT = "content"; //$NON-NLS-1$

	/** the target editor */
	private EditorDescriptor editor;
	/** the target editor's content */
	private ObjectDescriptor content;

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static PaletteProviderConfiguration parse(IConfigurationElement configElement) {
		Assert.isNotNull(configElement, "null provider configuration element"); //$NON-NLS-1$
		return new PaletteProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private PaletteProviderConfiguration(IConfigurationElement configElement) {
		IConfigurationElement configChildren[];

		// read the editor object if any
		configChildren = configElement.getChildren(EDITOR);
		if (configChildren.length > 0)
			editor = new EditorDescriptor(configChildren[0]);

		// read the content object if any
		configChildren = configElement.getChildren(CONTENT);
		if (configChildren.length > 0)
			content = new ObjectDescriptor(configChildren[0]);
	}

	/**
	 * Determines if the provider understands the given context
	 * 
	 * @param targetEditor The target editor
	 * @param targetContent The target editor's content
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(IEditorPart targetEditor, Object targetContent) {
		if (editor != null && !editor.sameAs(targetEditor))
			return false;
		if (content != null && !content.sameAs(targetContent))
			return false;
		return true;
	}

	/**
	 * An descriptor for an editor in XML by a contribution item provider. 
	 */
	private static class EditorDescriptor extends ObjectDescriptor{
		/** the target id */
		private final String targetId;

		/**
		 * Initializes a new editor descriptor by reading the configuration element
		 * 
		 * @param configElement The contribution configuration element
		 */
		public EditorDescriptor(IConfigurationElement configElement) {
			super(configElement);
			targetId = configElement.getAttribute(ID);
		}

		/**
		 * Determines whether this contribution is applicable to the given editor
		 * 
		 * @param editor The target editor
		 * @return <code>true</code> if applicable <code>false</code> if not
		 */
		public boolean sameAs(Object object) {
			if (!(object instanceof IEditorPart))
				return false;
			
			if (targetId != null)
				if (!targetId.equals(((IEditorPart)object).getSite().getId()))
					return false;
			
			return super.sameAs(object);
		}

	}

}
