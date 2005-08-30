/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Trace;
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
		extends Service.ProviderDescriptor {

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
	    //empty ctor
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
	 * @param editor
	 * @param content
	 * @param type
	 * @return <code>PaletteRoot</code>
	 */
	public static PaletteRoot createPalette(
		final IEditorPart editor,
		final Object content,
		final PaletteType type) {
		final PaletteRoot root = new PaletteRoot();
		try {
			DiagramMEditingDomainGetter.getMEditingDomain(editor).runAsRead( new MRunnable() {
		        public Object run() {
		            getInstance().contributeToPalette(editor, content, root);
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

}
