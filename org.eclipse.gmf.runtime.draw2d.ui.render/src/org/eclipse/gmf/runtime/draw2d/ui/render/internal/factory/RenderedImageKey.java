/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.swt.graphics.RGB;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 * This class defines the unique key used to get the appropriate RenderedImage from the map.
 */
public final class RenderedImageKey extends RenderInfoImpl {

    public RenderedImageKey() {
        super();
    }

    public RenderedImageKey(RenderInfo info) {
        this(info, 0, null);
    }

    public RenderedImageKey(RenderInfo info, long checksum, Object extraData) {
        super(info);
        this.checksum = checksum;
        this.extraData = extraData;
    }

    private long checksum = 0;
    private Object extraData = null;

    /**
     * @return Long value that is the checksum
     */
    public long getChecksum() {
        return checksum;
    }

    /**
     * @return <code>Object</code> that is extra data to be cached for the rendered image.  The
     * extra data is always unique with respect to the checksum so it doesn't have to be
     * considered in the hashcode calculation.
     */
    public Object getExtraData() {
        return extraData;
    }

    /**
     * Sets the extra data to bee cached for the rendered image.  The
     * extra data should always unique with respect to the checksum so it doesn't have to be
     * considered in the hashcode calculation.
     * @param extraData
     */
    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    /**
     * Retrieves a hash code value for this output operation. This method is 
     * supported for the benefit of hashtables such as those provided by 
     * <code>java.util.Hashtable</code>.
     * 
     * @return A hash code value for this output operation.
     * 
     * @see Object#hashCode()
     */
    public int hashCode() {
        int hashCode = new Long(getChecksum()).hashCode();
        hashCode += super.hashCode();

        return hashCode;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {

        RenderedImageKey imagekey = null;
        if (object instanceof RenderedImageKey) {
            imagekey = (RenderedImageKey) object;
        }

        if (imagekey != null
            && getChecksum() == imagekey.getChecksum()
            && super.equals(imagekey)) {
            return true;
        }

        return false;
    }

    public void setValues(int width, int height, boolean maintainAspectRatio, boolean antialias, RGB fill, RGB outline) {
        
        // if the colors have changed, the document needs to be recreated
        if (fill != null && !fill.equals(getBackgroundColor())) {
            setExtraData(null);
        }
        if (outline != null && !outline.equals(getForegroundColor())) {
            setExtraData(null);
        }
        
        super.setValues(width, height, maintainAspectRatio, antialias, fill, outline);

    }
    
    
}
