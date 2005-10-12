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

package org.eclipse.gmf.runtime.draw2d.ui.internal.l10n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;


/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 *
 * @author chmahone
 *
 */
public class Draw2dResourceManager extends AbstractUIResourceManager {
		 
	public static final String LEFT_BOTTOM_IMAGE = "leftbottom.gif"; //$NON-NLS-1$
	public static final String RIGHT_BOTTOM_IMAGE = "rightbottom.gif"; //$NON-NLS-1$
	public static final String RIGHT_IMAGE = "right.gif"; //$NON-NLS-1$
	public static final String TOP_RIGHT_IMAGE = "topright.gif"; //$NON-NLS-1$
	public static final String BOTTOM_IMAGE = "bottom.gif"; //$NON-NLS-1$
	
	public static final String DOWN_ARROW_IMAGE = "downarrow.gif"; //$NON-NLS-1$
	public static final String UP_ARROW_IMAGE = "uparrow.gif"; //$NON-NLS-1$
	public static final String LEFT_ARROW_IMAGE = "leftarrow.gif"; //$NON-NLS-1$
	public static final String RIGHT_ARROW_IMAGE = "rightarrow.gif"; //$NON-NLS-1$
	public static final String UP_PRESSED_ARROW_IMAGE = "uppressedarrow.gif"; //$NON-NLS-1$
	public static final String DOWN_PRESSED_ARROW_IMAGE = "downpressedarrow.gif"; //$NON-NLS-1$
	public static final String LEFT_PRESSED_ARROW_IMAGE = "leftpressedarrow.gif";//$NON-NLS-1$
	public static final String RIGHT_PRESSED_ARROW_IMAGE = "rightpressedarrow.gif";//$NON-NLS-1$
	public static final String UP_GRAY_ARROW_IMAGE = "upgrayarrow.gif";//$NON-NLS-1$
	public static final String DOWN_GRAY_ARROW_IMAGE = "downgrayarrow.gif";//$NON-NLS-1$
	public static final String LEFT_GRAY_ARROW_IMAGE = "leftgrayarrow.gif";//$NON-NLS-1$
	public static final String RIGHT_GRAY_ARROW_IMAGE = "rightgrayarrow.gif";//$NON-NLS-1$

	/**
	 * Singleton instance for the resource manager
	 */
	private static Draw2dResourceManager resourceManager =
		new Draw2dResourceManager();

	/**
	 * Constructor for PresentationResourceManager.
	 */
	private Draw2dResourceManager() {
		super();
	}

	/**
	 * Return singleton instance of the resource manager
	 * @return AbstractResourceManager
	 */
	public static Draw2dResourceManager getInstance() {
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

	/**
	 * @see com.ibm.xtools.common.l10n.AbstractResourceManager#initializeResources()
	 */
	protected void initializeResources() {
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager#initializeUIResources()
	 */
	protected void initializeUIResources() {
		initializeMessageResources();
	}	

    /**
     * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#getPlugin()
     */
    protected Plugin getPlugin() {
        return Draw2dPlugin.getInstance();
    }

    private Map fonts = null;
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager#getFont(org.eclipse.swt.graphics.Device, org.eclipse.swt.graphics.FontData)
	 */
	public Font getFont(Device device, FontData fd) {
		if (fonts == null)
			fonts = new HashMap();
			
		Object value = fonts.get(fd.toString());
		if (value != null) {
			return (Font) value;
		}
		Font newFont = new Font(device, fd);
		fonts.put(fd.toString(), newFont);
		return newFont;
	}
	
	/**
	 * Removes all fonts currently in the cache and dispose of them
	 */
	public void clearFontCache() {
		if (fonts != null) {
			List keys = new ArrayList(fonts.keySet());
			Iterator keyiter = keys.iterator();
			while (keyiter.hasNext()) {
				Font font = (Font)fonts.remove(keyiter.next());
				font.dispose();
			}
		}
	}
}
