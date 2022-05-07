/******************************************************************************
 * Copyright (c) 2002, 2003, 2006, 2021 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal;

import java.awt.Color;

import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author melaasar
 */
public class Draw2dRenderPlugin extends AbstractUIPlugin {

    /** the plugin singleton */
    private static Plugin singleton;

    /**
     * Method getInstance.
     *
     * @return Plugin
     */
    public static Plugin getInstance() {
        return singleton;
    }

    /**
     * Retrieves the unique identifier of this plug-in.
     *
     * @return A non-empty string and is unique within the plug-in registry.
     */
    public static String getPluginId() {
        return getInstance().getBundle().getSymbolicName();
    }

    public Draw2dRenderPlugin() {
        super();
        if (singleton == null) {
            singleton = this;
        }

        // force loading of AWT - bugzilla 119649
        initAWT();
        registerBatikImageFormats();
    }

    private void initAWT() {
        Color initColor = new Color(0, 0, 0);
        initColor.getRed();
    }

    private void registerBatikImageFormats() {
        ImageWriterRegistry.getInstance().register(new org.apache.batik.ext.awt.image.codec.imageio.ImageIOPNGImageWriter());
        ImageWriterRegistry.getInstance().register(new org.apache.batik.ext.awt.image.codec.imageio.ImageIOTIFFImageWriter());
        ImageWriterRegistry.getInstance().register(new org.apache.batik.ext.awt.image.codec.imageio.ImageIOJPEGImageWriter());

        ImageTagRegistry.getRegistry().register(new org.apache.batik.ext.awt.image.codec.png.PNGRegistryEntry());
        ImageTagRegistry.getRegistry().register(new org.apache.batik.ext.awt.image.codec.imageio.ImageIOJPEGRegistryEntry());
        ImageTagRegistry.getRegistry().register(new org.apache.batik.ext.awt.image.codec.imageio.ImageIOPNGRegistryEntry());
        ImageTagRegistry.getRegistry().register(new org.apache.batik.ext.awt.image.codec.imageio.ImageIOTIFFRegistryEntry());
    }

}
