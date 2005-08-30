/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.AbstractResourceManager;

/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 *
 * @author chmahone
 *
 */
public class PresentationResourceManager extends AbstractResourceManager {

	/**
	 * the image for handle collapse
	 */
	public static final String IMAGE_HANDLE_COLLAPSE = "expanded.gif"; //$NON-NLS-1$
	/**
	 * the image for handle expand
	 */
	public static final String IMAGE_HANDLE_EXPAND = "collapsed.gif"; //$NON-NLS-1$
	
	/**
	 * the image for handle incoming wet
	 */
	public static final String IMAGE_HANDLE_INCOMING_WEST = "handle_incoming_west.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing west
	 */
	public static final String IMAGE_HANDLE_OUTGOING_WEST = "handle_outgoing_west.gif"; //$NON-NLS-1$
	/**
	 * the image for handle incoming east
	 */	
	public static final String IMAGE_HANDLE_INCOMING_EAST = "handle_incoming_east.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing east
	 */	
	public static final String IMAGE_HANDLE_OUTGOING_EAST = "handle_outgoing_east.gif"; //$NON-NLS-1$
	/**
	 * the image for handle incoming south
	 */	
	public static final String IMAGE_HANDLE_INCOMING_SOUTH = "handle_incoming_south.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing south
	 */	
	public static final String IMAGE_HANDLE_OUTGOING_SOUTH = "handle_outgoing_south.gif"; //$NON-NLS-1$
	/**
	 * the image for handle incoming north
	 */	
	public static final String IMAGE_HANDLE_INCOMING_NORTH = "handle_incoming_north.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing north
	 */	
	public static final String IMAGE_HANDLE_OUTGOING_NORTH = "handle_outgoing_north.gif"; //$NON-NLS-1$
	
	/**
	 * the image for note
	 */	
	public static final String DESC_NOTE = "note.gif";//$NON-NLS-1$
	/**
	 * the image for large note
	 */	
	public static final String DESC_NOTE_LARGE = "note_24x24.gif";//$NON-NLS-1$
	/**
	 * the image for text
	 */	
	public static final String DESC_TEXT = "text.gif";//$NON-NLS-1$
	/**
	 * the image for large text
	 */	
	public static final String DESC_TEXT_LARGE = "text_24x24.gif";//$NON-NLS-1$
	/**
	 * the image for note attachment
	 */	
	public static final String DESC_NOTE_ATTACHMENT = "noteattachment.gif";//$NON-NLS-1$
	/**
	 * the image for large note attachment
	 */	
	public static final String DESC_NOTE_ATTACHMENT_LARGE = "noteattachment_24x24.gif";//$NON-NLS-1$
	
	/**
	 * Singleton instance for the resource manager
	 */
	private static AbstractResourceManager resourceManager =
		new PresentationResourceManager();

	/**
	 * Constructor for PresentationResourceManager.
	 */
	private PresentationResourceManager() {
		super();
	}

	/**
	 * Return singleton instance of the resource manager
	 * @return AbstractResourceManager
	 */
	public static AbstractResourceManager getInstance() {
		return resourceManager;
	}

	/**
	 * A shortcut method to get localized string
	 * @param key - resource bundle key. 
	 * @return - localized string value or a key if the bundle does not contain
	 * 			  this entry
	 */
	public static String getI18NString(String key) {
		return getInstance().getString(key);
	}


	protected void initializeResources() {
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager#initializeUIResources()
	 */
	protected void initializeUIResources() {
		initializeMessageResources();
		initializeImageResources();
	}

    /**
     * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#getPlugin()
     */
    protected Plugin getPlugin() {
        return DiagramUIPlugin.getInstance();
    }

}
