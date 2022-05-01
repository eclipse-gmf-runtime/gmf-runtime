/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration.ObjectDescriptor;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.util.ActivityUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteTemplateEntry;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DiagramProvidersPlugin;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DiagramProvidersStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.IPaletteProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteDrawer;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteSeparator;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteStack;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.osgi.framework.Bundle;

import com.ibm.icu.util.StringTokenizer;

/**
 * The defaul palette provider. It reads XML palette contributions from the
 * provider's extension point and contributes them to an editor's palette
 * based on different contribution criteria
 * 
 * The provider class should not be subclassed since it does its contribution totally from XML
 * However, if programatic contribution is required, then the <code>IPaletteProvider</code>
 * interface should be implemented directly instead 
 * 
 * @author melaasar
 */
public class DefaultPaletteProvider
    extends AbstractProvider
    implements IPaletteProvider {

    /** constants corresponding to different symbols in the extention schema */
    private static final String CONTRIBUTION = "contribution"; //$NON-NLS-1$
    private static final String FACTORY_CLASS = "factoryClass"; //$NON-NLS-1$
    private static final String ENTRY = "entry"; //$NON-NLS-1$
    private static final String KIND = "kind"; //$NON-NLS-1$
    private static final String ID = "id"; //$NON-NLS-1$
    private static final String PATH = "path"; //$NON-NLS-1$
    private static final String LABEL = "label"; //$NON-NLS-1$
    private static final String DESCRIPTION = "description"; //$NON-NLS-1$
    private static final String SMALL_ICON = "small_icon"; //$NON-NLS-1$
    private static final String LARGE_ICON = "large_icon"; //$NON-NLS-1$
//  private static final String PERMISSION = "permission"; //$NON-NLS-1$
    private static final String EXPAND = "expand"; //$NON-NLS-1$
    private static final String FORCE = "force"; //$NON-NLS-1$
    private static final String CONTENT = "content"; //$NON-NLS-1$
    private static final String DEFINE_ONLY = "defineOnly"; //$NON-NLS-1$
    private static final String PREDEFINED_ENTRY = "predefinedEntry"; //$NON-NLS-1$
    private static final String REMOVE = "remove"; //$NON-NLS-1$

    /** palette entry kind enumeration */
    private static final String DRAWER = "drawer"; //$NON-NLS-1$
    private static final String STACK = "stack"; //$NON-NLS-1$
    private static final String SEPARATOR = "separator"; //$NON-NLS-1$
    private static final String TEMPLATE = "template"; //$NON-NLS-1$
    private static final String TOOL = "tool"; //$NON-NLS-1$
    private static final int ENUM_DRAWER = 0;
    private static final int ENUM_STACK = 1;
    private static final int ENUM_SEPARATOR = 2;
    private static final int ENUM_TEMPLATE = 3;
    private static final int ENUM_TOOL = 4;

    /** palette entry permission enumeration */
    private static final String NONE = "None"; //$NON-NLS-1$
    private static final String HIDEONLY = "HideOnly"; //$NON-NLS-1$
    private static final String LIMITED = "limited"; //$NON-NLS-1$
    private static final String FULL = "full"; //$NON-NLS-1$

    /**
     * A descriptor for XML-based palette contribution
     */
    private static class ContributionDescriptor {

        private PaletteFactoryProxy paletteFactory;
        private List entries = new ArrayList();

        /**
         * Reads XML entries for a contribution 
         * @param configElement
         */
        public ContributionDescriptor(IConfigurationElement configElement) {
            paletteFactory = new PaletteFactoryProxy(configElement);

            // read the palette entries
            IConfigurationElement configChildren[] =
                configElement.getChildren(ENTRY);

            for (int i = 0; i < configChildren.length; i++) {
                entries.add(new EntryDescriptor(configChildren[i]));
            }
            
            configChildren =
                configElement.getChildren(PREDEFINED_ENTRY);

            for (int i = 0; i < configChildren.length; i++) {
                entries.add(new PredefinedEntryDescriptor(configChildren[i]));
            }
        }

        /**
         * Contributes to the given palette root based on the given editor's
         * content
         * 
         * @param content
         * @param root
         * @param predefinedEntries
         *            map of predefined palette entries where the key is the
         *            palette entry id and the value is the palette entry
         */
        public void contribute(Object content, PaletteRoot root, Map predefinedEntries, String pluginID) {
            Iterator iter = entries.iterator();
            while (iter.hasNext()) {
                IEntryDescriptor descriptor = (IEntryDescriptor) iter.next();
                if (ActivityUtil.isEnabled(descriptor.getID(), pluginID)) {
                    descriptor.contribute(content, root, paletteFactory,
                        predefinedEntries);
                }
            }
        }
    }
    
    /**
     * An interface describing the types of palette entries in the schema.
     * 
     * @author cmahoney
     */
    private static interface IEntryDescriptor {

        /**
         * Contributes the palette entry based on the given content, starting
         * from the given root and into the given path
         * 
         * @param content
         * @param root
         * @param paletteFactory
         * @param predefinedEntries
         *            map of predefined palette entries where the key is the
         *            palette entry id and the value is the palette entry
         */
        public void contribute(Object content, PaletteRoot root,
                PaletteFactoryProxy paletteFactory, Map predefinedEntries);
        
        /**
         * Gets the ID of this entry descriptor.
         * 
         * @return the id
         */
        public String getID();
    }

    /**
     * A descriptor for an XML-based palette entry
     */
    private static class EntryDescriptor implements IEntryDescriptor {
        private Integer kind;
        private String id;
        private String path;
        private String label;
        private String description;
        private Integer permission;
        private ImageDescriptor small_icon;
        private ImageDescriptor large_icon;
        private boolean noIcon = false;
        private DrawerExpandHelper expandHelper;
        private boolean defineOnly;

        /**
         * Reads an XML palette entry and its attributes
         * @param configElement
         */
        public EntryDescriptor(IConfigurationElement configElement) {
            String kindStr = configElement.getAttribute(KIND);
            if (DRAWER.equals(kindStr))
                kind = new Integer(ENUM_DRAWER);
            else if (STACK.equals(kindStr))
                kind = new Integer(ENUM_STACK);
            else if (SEPARATOR.equals(kindStr))
                kind = new Integer(ENUM_SEPARATOR);
            else if (TEMPLATE.equals(kindStr))
                kind = new Integer(ENUM_TEMPLATE);
            else if (TOOL.equals(kindStr))
                kind = new Integer(ENUM_TOOL);
            else
                Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "No factory class name is provided"); //$NON-NLS-1$

            id = configElement.getAttribute(ID);
            if (id == null)
                Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "No factory class name is provided"); //$NON-NLS-1$

            defineOnly = Boolean.valueOf(
                configElement.getAttribute(DEFINE_ONLY)).booleanValue();

            path = configElement.getAttribute(PATH);
            if (path == null && !defineOnly)
                Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "Path must be provided when contributing a palette entry"); //$NON-NLS-1$

            label = configElement.getAttribute(LABEL);
            if (label == null)
                label = DiagramUIMessages.PaletteEntry_DefaultLabel;

            description = configElement.getAttribute(DESCRIPTION);

            if (NONE.equals(kindStr))
                permission =
                    new Integer(PaletteEntry.PERMISSION_NO_MODIFICATION);
            if (HIDEONLY.equals(kindStr))
                permission = new Integer(PaletteEntry.PERMISSION_HIDE_ONLY);
            if (LIMITED.equals(kindStr))
                permission =
                    new Integer(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
            if (FULL.equals(kindStr))
                permission =
                    new Integer(PaletteEntry.PERMISSION_FULL_MODIFICATION);

            String smallIconPath = configElement.getAttribute(SMALL_ICON);
            if (NONE.equals(smallIconPath)) {
                noIcon = true;
            } else {
                small_icon = findIconImageDescriptor(configElement, smallIconPath);
            }
            
            String largeIconPath = configElement.getAttribute(LARGE_ICON);
            large_icon = findIconImageDescriptor(configElement, largeIconPath);

            if (kind.intValue() == ENUM_DRAWER) {
                IConfigurationElement[] configChildren =
                    configElement.getChildren(EXPAND);
                if (configChildren.length > 0)
                    expandHelper = new DrawerExpandHelper(configChildren[0]);
                else
                    expandHelper = new DrawerExpandHelper(Boolean.FALSE);
            }
        }

        /**
         * Finds the image descriptor that is associated with the icon path.
         * @param configElement
         * @param smallIconPath
         * @return
         */
        private ImageDescriptor findIconImageDescriptor(IConfigurationElement configElement, String iconPath) {
            String pluginId = configElement.getDeclaringExtension().getNamespaceIdentifier();
            Bundle bundle = Platform.getBundle(pluginId);
            try
            {
                if (iconPath != null) {
                    URL fullPathString = FileLocator.find(bundle, new Path(iconPath), null);
                    fullPathString = fullPathString != null ? fullPathString : new URL(iconPath);
                    if (fullPathString != null) {
                        return ImageDescriptor.createFromURL(fullPathString);
                    }
                }
            }
            catch (MalformedURLException e)
            {
                Trace.catching(DiagramUIPlugin.getInstance(),
                    DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
                    DefaultPaletteProvider.class, e.getLocalizedMessage(), e); 
                Log.error(DiagramUIPlugin.getInstance(),
                    DiagramUIStatusCodes.RESOURCE_FAILURE, e.getMessage(), e);
            }
            
            return null;
        }

        public void contribute(
            Object content,
            PaletteRoot root,
            PaletteFactoryProxy paletteFactory, Map predefinedEntries) {
            if (kind == null || id == null || label == null)
                return;

            PaletteEntry paletteEntry = null;

            switch (kind.intValue()) {
                case ENUM_DRAWER :
                    PaletteDrawer drawer = new PaletteDrawer(id, label);
                    if (expandHelper.expand(content)) {
                        drawer.setInitialState(
                            PaletteDrawer.INITIAL_STATE_OPEN);
                    }
                    if (noIcon) {
                        drawer.setShowDefaultIcon(false);
                    }
                    paletteEntry = drawer;
                    break;
                case ENUM_STACK:
                    paletteEntry = new PaletteStack(id, label, description,
                        small_icon);
                    break;
                case ENUM_SEPARATOR :
                    paletteEntry = new PaletteSeparator(id);
                    break;
                case ENUM_TEMPLATE :
                    paletteEntry =
                        new PaletteTemplateEntry(id, label, paletteFactory);
                    break;
                case ENUM_TOOL :
                    paletteEntry =
                        new PaletteToolEntry(id, label, paletteFactory);
                    break;
            }

            if (paletteEntry != null) {
                paletteEntry.setDescription(description);
                paletteEntry.setSmallIcon(small_icon);
                paletteEntry.setLargeIcon(large_icon);
                if (permission != null)
                    paletteEntry.setUserModificationPermission(
                        permission.intValue());

                if (defineOnly) {
                    predefinedEntries.put(id, paletteEntry);
                } else {
                    appendPaletteEntry(root, predefinedEntries, path, paletteEntry);
                }
            }
        }

        public String getID() {
            return id;
        }

    }

    /**
     * A descriptor for an XML-based predefined palette entry. 
     */
    private static class PredefinedEntryDescriptor
        implements IEntryDescriptor {

        private String id;
        private String path;
        private DrawerExpandHelper expandHelper;
        private boolean remove;

        /**
         * Reads an XML palette entry and its attributes
         * @param configElement
         */
        public PredefinedEntryDescriptor(IConfigurationElement configElement) {
            id = configElement.getAttribute(ID);
            if (id == null) {
                Log.info(DiagramProvidersPlugin.getInstance(),
                    DiagramProvidersStatusCodes.SERVICE_FAILURE,
                    "No ID provided"); //$NON-NLS-1$
            }
 
            path = configElement.getAttribute(PATH);
 
            IConfigurationElement[] configChildren = configElement
                .getChildren(EXPAND);
            if (configChildren.length > 0)
                expandHelper = new DrawerExpandHelper(configChildren[0]);
            else
                expandHelper = new DrawerExpandHelper(Boolean.FALSE);
            
            remove = Boolean.valueOf(configElement.getAttribute(REMOVE))
                .booleanValue();
        }

        public void contribute(
            Object content,
            PaletteRoot root,
            PaletteFactoryProxy paletteFactory, Map predefinedEntries) {
            
            if (id == null)
                return;
            
            // first try to find it in the palette root
            PaletteEntry paletteEntry = findPaletteEntry(root, id);
            
            if (paletteEntry != null) {

                if (remove) {
                    paletteEntry.getParent().remove(paletteEntry);
                    return;
                }
                
                // Set expand state on drawers.
                if (paletteEntry instanceof PaletteDrawer
                    && expandHelper.expand(content)) {
                    ((PaletteDrawer) paletteEntry)
                        .setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
                }

            }
            
            // now check to see if it has been predefined only
            if (paletteEntry == null) {
                paletteEntry = findPredefinedEntry(predefinedEntries, id);
            }
            
            if (paletteEntry != null) {
                
                if (path != null) {
                    appendPaletteEntry(root, predefinedEntries, path,
                        paletteEntry);
                    
                    // Set expand state on drawers.
                    if (paletteEntry instanceof PaletteDrawer
                        && expandHelper.expand(content)) {
                        ((PaletteDrawer) paletteEntry)
                            .setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
                    }
                }
            }
        }

        public String getID() {
            return id;
        }
    }

    /**
     * Searches the predefined entries for a palette entry given the full path
     * as it was predefined.
     * 
     * @param predefinedEntries
     *            map of predefined palette entries where the key is the palette
     *            entry id and the value is the palette entry
     * @param path
     *            the path to the palette entry starting as it was predefined
     * @return the palette entry if one exists; null otherwise.
     */
    private static PaletteEntry findPredefinedEntry(Map predefinedEntries,
            String path) {
        StringTokenizer tokens = new StringTokenizer(path, "/"); //$NON-NLS-1$

        PaletteEntry root = (PaletteEntry) predefinedEntries.get(tokens
            .nextToken());

        while (tokens.hasMoreElements()) {
            if (root instanceof PaletteContainer)
                root = findChildPaletteEntry((PaletteContainer) root, tokens
                    .nextToken());
            else
                return null;
        }
        return root;
    }
    
    /**
     * Finds a palette container starting from the given root and using the
     * given path
     * 
     * @param root
     * @param aPath
     * @return the container or <code>null</code> if not found
     */
    private static PaletteEntry findPaletteEntry(PaletteEntry root, String aPath) {
        StringTokenizer tokens = new StringTokenizer(aPath, "/"); //$NON-NLS-1$
        while (tokens.hasMoreElements()) {
            if (root instanceof PaletteContainer)
                root =
                    findChildPaletteEntry(
                        (PaletteContainer) root,
                        tokens.nextToken());
            else
                return null;
        }
        return root;
    }

    /**
     * Finds a palette entry starting from the given container
     * and using the given path
     * @param root
     * @param path
     * @return the entry or <code>null</code> if not found
     */
    private static PaletteEntry findChildPaletteEntry(
        PaletteContainer container,
        String childId) {
        Iterator entries = container.getChildren().iterator();
        while (entries.hasNext()) {
            PaletteEntry entry = (PaletteEntry) entries.next();
            if (entry.getId().equals(childId))
                return entry;
        }
        return null;
    }
    
    /**
     * Appends the given palette entry to the appropriate location in either a
     * predefined palette entry or the palette root.
     * 
     * @param root
     * @param predefinedEntries
     *            map of predefined palette entries where the key is the palette
     *            entry id and the value is the palette entry
     * @param path
     * @param paletteEntry
     */
    private static void appendPaletteEntry(PaletteRoot root,
            Map predefinedEntries, String path, PaletteEntry paletteEntry) {
        PaletteEntry fEntry = findPaletteEntry(root, path);
        if (fEntry == null) {
            fEntry = findPredefinedEntry(predefinedEntries, path);
        }
        if (fEntry == null) 
            Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "Invalid palette entry path"); //$NON-NLS-1$                
        else if (fEntry instanceof PaletteContainer)
             ((PaletteContainer) fEntry).add(paletteEntry);
        else if (fEntry instanceof PaletteSeparator)
            appendTo((PaletteSeparator) fEntry, paletteEntry);
        else
            fEntry.getParent().add(
                fEntry.getParent().getChildren().indexOf(fEntry) + 1,
                paletteEntry);
    }
    
    /**
     * Appends the given entry to the end of the group of the given separator.
     * 
     * @param separator
     * @param entry
     */
    private static void appendTo(PaletteSeparator separator, PaletteEntry entry) {
        List children = separator.getParent().getChildren();
        int index = children.indexOf(separator);
        for (index++; index < children.size(); index++) {
            if (children.get(index) instanceof PaletteSeparator)
                break;
        }
        separator.getParent().add(index, entry);
    }

    /**
     * A proxy for a palette factory that instantiates the real factory
     * on demand (when a palette entry is selcted)
     */
    private static class PaletteFactoryProxy extends PaletteFactory.Adapter {

        private IConfigurationElement configElement;
        private PaletteFactory factory;

        public PaletteFactoryProxy(IConfigurationElement configElement) {
            this.configElement = configElement;
        }

        /**
         * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory#getTemplate(java.lang.String)
         */
        public Object getTemplate(String templateId) {
            if (factory == null) {
                try {
                    Object ext =
                        configElement.createExecutableExtension(FACTORY_CLASS);
                    factory = (PaletteFactory) ext;
                } catch (CoreException e) {
                    Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "No factory class name is provided"); //$NON-NLS-1$                  
                }
            }
            if (factory != null) {
                Object template = factory.getTemplate(templateId);
                if (template == null)
                    Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "No factory class name is provided"); //$NON-NLS-1$
                return template;
            }
            return null;
        }

        /**
         * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory#createTool(java.lang.String)
         */
        public Tool createTool(String toolId) {
            if (factory == null) {
                try {
                    Object ext =
                        configElement.createExecutableExtension(FACTORY_CLASS);
                    factory = (PaletteFactory) ext;
                } catch (CoreException e) {
                    Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "No factory class name is provided"); //$NON-NLS-1$                  
                }
            }
            if (factory != null) {
                Tool tool = factory.createTool(toolId);
                if (tool == null)
                    Log.info(DiagramProvidersPlugin.getInstance(), DiagramProvidersStatusCodes.SERVICE_FAILURE, "No factory class name is provided"); //$NON-NLS-1$
                return tool;
            }
            return null;
        }
    }

    /**
     * A helper class in expanding palette drawer. It reads
     * the relavent XML entries for that.
     */
    private static class DrawerExpandHelper {

        private Boolean force;
        private ObjectDescriptor content;

        /**
         * Initialize the helper with a foced value
         * @param force
         */
        public DrawerExpandHelper(Boolean force) {
            this.force = force;
        }

        /**
         * Initialize the helper from the config element in the XML
         * @param configElement
         */
        public DrawerExpandHelper(IConfigurationElement configElement) {
            String forceStr = configElement.getAttribute(FORCE);
            force =
                forceStr == null ? Boolean.FALSE : Boolean.valueOf(forceStr);

            IConfigurationElement[] configChildren =
                configElement.getChildren(CONTENT);
            if (configChildren.length > 0)
                content = new ObjectDescriptor(configChildren[0]);
        }

        /**
         * Determines whether to initially expand the palette drawer
         * @param targetContent
         * @return
         */
        public boolean expand(Object targetContent) {
            if (Boolean.TRUE.equals(force))
                return true;
            if (content != null && content.sameAs(targetContent))
                return true;
            return false;
        }
    }

    /**
     * The list of palette provider XML contributions
     */
    private List contributions = new ArrayList();
    
    /**
     * The pluginID of the XML contributions
     */
    private String pluginID;

    /**
     * 
     * Adds the configuration elements to the 
     * list of palette provider XML contributions 
     * 
     * @param configElement
     */
    public void setContributions(IConfigurationElement configElement) {
        
        pluginID = configElement.getContributor().getName();
        IConfigurationElement configChildren[] =
            configElement.getChildren(CONTRIBUTION);

        for (int i = 0; i < configChildren.length; i++) {
            contributions.add(new ContributionDescriptor(configChildren[i]));
        }
    }

    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.IPaletteProvider#contributeToPalette(org.eclipse.ui.IEditorPart, java.lang.Object)
     */
    public void contributeToPalette(
        IEditorPart editor,
        Object content,
        PaletteRoot root, Map predefinedEntries) {
        Iterator iter = contributions.iterator();
        while (iter.hasNext()) {
            ((ContributionDescriptor) iter.next()).contribute(content, root, predefinedEntries, pluginID);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
     */
    public boolean provides(IOperation operation) {
        return false; // all logic is done in the service
    }
    
    
}
