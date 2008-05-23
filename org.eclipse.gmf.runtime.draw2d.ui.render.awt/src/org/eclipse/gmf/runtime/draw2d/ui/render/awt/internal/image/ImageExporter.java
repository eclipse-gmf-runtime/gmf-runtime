/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.Draw2dRenderPlugin;


/**
 * Class utility for exporting to JPEG, TIFF and PNG file formats.
 * @author sshaw
 */
public class ImageExporter {
    
    static public final String JPEG_FILE = "JPEG"; //$NON-NLS-1$
    static public final String PNG_FILE = "PNG"; //$NON-NLS-1$
    
    /**
     * Allows export of an image to specific supported image file formats.
     * 
     * @param destination the <code>IPath</code> that is the path of the destination file to be created.
     * @param image the <code>BufferedImage</code> class to be exported to the image file format.
     * @param imageFormat the <code>String</code> that is the type of image file format to export to.  Can
     * be one of <code>JPEG_FILE</code> or <code>PNG_FILE</code>
     * @param monitor the <code>IProgressMonitor</code> that will update the progress monitor during the operation.
     */
    public static void exportToFile(IPath destination, BufferedImage image,
            String imageFormat, IProgressMonitor monitor) 
        throws CoreException {

        IStatus fileModificationStatus = createFile(destination);
        
        if (!fileModificationStatus.isOK()) {
        	// can't write to the file
        	return;
        }
        
        try {
            FileOutputStream os = new FileOutputStream(destination.toOSString());
            exportToOutputStream(os, image, imageFormat, monitor);
            os.close();
            monitor.worked(1);
            refreshLocal(destination);
        } catch (IOException ex) {
            Log.error(Draw2dRenderPlugin.getInstance(), IStatus.ERROR, ex
                .getMessage(), ex);
            IStatus status =
                new Status(IStatus.ERROR, "exportToFile", IStatus.OK, //$NON-NLS-1$
                    ex.getMessage(), null);
            throw new CoreException(status);
        }
    }
    
    /**
     * Allows export of an image to specific supported image file formats.
	 *
     * @param stream stream to write the data to
     * @param image the image
     * @param imageFormat image format
     * @param monitor progress monitor
     * @throws CoreException
     */
    public static void exportToOutputStream(OutputStream stream, BufferedImage image,
			String imageFormat, IProgressMonitor monitor) throws CoreException {
		monitor.worked(1);
		ImageTranscoder imageTranscoder = null;
		BufferedImage newImg = image;

		if (imageFormat == JPEG_FILE) {
			imageTranscoder = new JPEGTranscoder();
			if (image.getType() != BufferedImage.TYPE_INT_RGB) {
				newImg = new BufferedImage(image.getWidth(), image.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = newImg.getGraphics();
				g.drawImage(image, 0, 0, null);
				g.dispose();
			}
			imageTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
					new Float(0.65));
		} else if (imageFormat == PNG_FILE) {
			imageTranscoder = new PNGTranscoder();
		} else {
			throw new IllegalArgumentException();
		}

		TranscoderOutput to = new TranscoderOutput(stream);

		try {
			imageTranscoder.writeImage(newImg, to);
		} catch (TranscoderException e) {
			Log.error(Draw2dRenderPlugin.getInstance(), IStatus.ERROR, e
					.getMessage(), e);
			IStatus status = new Status(IStatus.ERROR,
					"exportToStream", IStatus.OK, //$NON-NLS-1$
					e.getMessage(), null);
			throw new CoreException(status);
		}
		monitor.worked(1);
	}
    
    /**
	 * create a file in the workspace if the destination is in a project in the
	 * workspace.
	 * 
	 * @param destination
	 *            the destination file.
	 * @return the status from validating the file for editing
	 * @exception CoreException
	 *                if this method fails
	 */
    private static IStatus createFile(IPath destination)
        throws CoreException {
        IFile file = ResourcesPlugin.getWorkspace().getRoot()
            .getFileForLocation(destination);
        if (file != null && !file.exists()) {
            File osFile = new File(destination.toOSString());
            if (osFile.exists()) {
                file.refreshLocal(IResource.DEPTH_ZERO, null);
            } else {
                ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
                    IResource.DEPTH_INFINITE, null);
                InputStream input = new ByteArrayInputStream(new byte[0]);
                file.create(input, false, null);
            }
        }
        
        if (file != null) {
        	return FileModificationValidator.approveFileModification(new IFile[] {file});
        }
        return Status.OK_STATUS;
    }

    /**
     * refresh the file in the workspace if the destination is in a project in
     * the workspace.
     * 
     * @param destination
     *            the destination file.
     * @exception CoreException if this method fails
     */
    private static void refreshLocal(IPath destination)
        throws CoreException {
        IFile file = ResourcesPlugin.getWorkspace().getRoot()
            .getFileForLocation(destination);
        if (file != null) {
            file.refreshLocal(IResource.DEPTH_ZERO, null);
        }
    }
}
