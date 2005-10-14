/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.util.ActivityFilterProviderDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.ContributeToPaletteOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.IPaletteProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteProviderConfiguration;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramMEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteGroup;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteSeparator;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorPart;

/**
 * @author melaasar
 *
 * A service to contributes to the palette of a given editor with a given content
 */
public class PaletteService extends Service implements IPaletteProvider {

	/**
	 * @author schafe
	 * @author melaasar
	 *
	 * A descriptor for palette providers defined by a configuration
	 * element.
	 */
	protected static class ProviderDescriptor
		extends ActivityFilterProviderDescriptor {

		/** the provider configuration parsed from XML */
		private PaletteProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>ISemanticProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration =
				PaletteProviderConfiguration.parse(element);
			Assert.isNotNull(providerConfiguration);
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (!super.provides(operation)) {
				return false;
			}
			if (getPolicy() != null)
				return getPolicy().provides(operation);
			if (operation instanceof ContributeToPaletteOperation) {
				ContributeToPaletteOperation o =
					(ContributeToPaletteOperation) operation;

				return providerConfiguration.supports(
					o.getEditor(),
					o.getContent());
			}
			return false;
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.Service.ProviderDescriptor#getProvider()
		 */
		public IProvider getProvider() {
			if (provider == null) {
				IProvider newProvider = super.getProvider();
				if (provider instanceof IPaletteProvider) {
					IPaletteProvider defaultProvider =
						(IPaletteProvider) newProvider;
					defaultProvider.setContributions(getElement());
				}
				return newProvider;
			}
			return super.getProvider();
		}
	}

	
	/**
	 * Sets contribution
	 * Empty because contributions are stored in the providers
	 * 
	 * @param configElement
	 */
	public void setContributions(IConfigurationElement configElement) {
	//  
	}

	/** the singleton instance of the palette service */
	private final static PaletteService instance = new PaletteService();

	static {
		instance.configureProviders(DiagramUIPlugin.getPluginId(), "paletteProviders"); //$NON-NLS-1$
	}

	/** the standard group id */
	public final static String GROUP_STANDARD = "standardGroup"; //$NON-NLS-1$

	/** the standard separator id */
	public final static String SEPARATOR_STANDARD = "standardSeparator"; //$NON-NLS-1$

	/** the standard separator id */
	public final static String TOOL_SELECTION = "selectionTool"; //$NON-NLS-1$

	/**
	 *  Creates a new instance of the Palette Service
	 */
	protected PaletteService() {
		super();
	}

	/**
	 * gets the singleton instance
	 * @return <code>PaletteService</code>
	 */
	public static PaletteService getInstance() {
		return instance;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.IPaletteProvider#contributeToPalette(org.eclipse.ui.IEditorPart, java.lang.Object, org.eclipse.gef.palette.PaletteRoot)
	 */
	public void contributeToPalette(
		IEditorPart editor,
		Object content,
		PaletteRoot root) {

		PaletteGroup standardGroup = new PaletteGroup(GROUP_STANDARD, PresentationResourceManager.getI18NString("StandardGroup.Label")); //$NON-NLS-1$
		standardGroup.setDescription(PresentationResourceManager.getI18NString("StandardGroup.Description")); //$NON-NLS-1$
		root.add(standardGroup);

		PaletteSeparator standardSeparator = new PaletteSeparator(SEPARATOR_STANDARD);
		standardGroup.add(standardSeparator);

		ToolEntry selectTool = new SelectionToolEntry();
		selectTool.setId(TOOL_SELECTION);
		standardGroup.add(selectTool);
		root.setDefaultEntry(selectTool);

		execute(new ContributeToPaletteOperation(editor, content, root));
	}

	/**
	 * Executes the palette operation using 
	 * the REVERSE execution strategy.
	 * 
	 * @param operation
	 * @return List of results
	 */
	private List execute(IOperation operation) {
		return execute(ExecutionStrategy.REVERSE, operation);
	}

	/**
	 * Creates default palette root.
	 * 
	 * @param editor
	 *            the editor
	 * @param content
	 *            the palette content
	 * @return a new palette root with contributions from all providers
	 */
	public PaletteRoot createPalette(
		final IEditorPart editor,
		final Object content) {
		final PaletteRoot root = new PaletteRoot();
		try {
			DiagramMEditingDomainGetter.getMEditingDomain(editor).runAsRead( new MRunnable() {
		        public Object run() {
		            contributeToPalette(editor, content, root);
		            return null;
		        }
		    });
		} catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
					DiagramUIDebugOptions.EXCEPTIONS_CATCHING, PaletteService.class,
					"createPalette()", //$NON-NLS-1$
					e);
		}
		return root;
	}
	
	/**
	 * Updates the palette root given.
	 * 
	 * @param existingRoot
	 *            existing palette root in which to add/remove entries that are
	 *            now provided for or no longer provided for
	 * @param editor
	 *            the editor
	 * @param content
	 *            the palette content
	 */
	public void updatePalette(
		PaletteRoot existingRoot,
		final IEditorPart editor,
		final Object content) {
		
		PaletteRoot newRoot = createPalette(editor, content);
		updatePaletteContainerEntries(existingRoot, newRoot);
	}
	
	/**
	 * Updates the children of an existing palette container to match the
	 * palette entries in a new palette container by adding or removing new
	 * palette entries only. This method works recursively on any children that
	 * are palette container entries. Existing leaf palette entries that are to
	 * be kept remain the same -- they are not replaced with the new palette
	 * entry. This is so that palette state (such as whether a drawer is pinned
	 * or expanded) can be preserved when the palette is updated.
	 * 
	 * @param existingContainer
	 *            the palette container to be updated with new entries, have
	 *            obsolete entries removed, and whose existing entries will
	 *            remain the same
	 * @param newContainer
	 *            the new palette entries
	 */
	private void updatePaletteContainerEntries(
			PaletteContainer existingContainer, PaletteContainer newContainer) {

		HashMap existingEntryIds = new HashMap();
		for (Iterator iter = existingContainer.getChildren().iterator(); iter
			.hasNext();) {
			PaletteEntry entry = (PaletteEntry) iter.next();
			existingEntryIds.put(entry.getId(), entry);
		}

		int lastExistingEntryIndex = 0;
		// cycle through the new entries
		for (Iterator iter = newContainer.getChildren().iterator(); iter
			.hasNext();) {
			PaletteEntry newEntry = (PaletteEntry) iter.next();

			PaletteEntry existingEntry = (PaletteEntry) existingEntryIds
				.get(newEntry.getId());
			if (existingEntry != null) { // is already in existing container
				// update the index
				lastExistingEntryIndex = existingContainer.getChildren()
					.indexOf(existingEntry);

				// remove the entry that was just updated from the map
				existingEntryIds.remove(existingEntry.getId());

				if (existingEntry instanceof PaletteContainer
					&& newEntry instanceof PaletteContainer) {
					// look for new/deleted entries in
					// palette containers
					updatePaletteContainerEntries(
						(PaletteContainer) existingEntry,
						(PaletteContainer) newEntry);
				}
			} else { // this is a new entry that did not previously exist
				existingContainer.add(++lastExistingEntryIndex, newEntry);
			}
		}

		// remove existing entries that were not found in the new container
		for (Iterator iter = existingEntryIds.values().iterator(); iter
			.hasNext();) {
			PaletteEntry entry = (PaletteEntry) iter.next();
			existingContainer.remove(entry);
		}

	}
	
}
