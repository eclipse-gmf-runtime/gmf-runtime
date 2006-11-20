/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.image.PartPositionInfo;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramSVGGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.ImageExporter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class to render a diagram to an image file.
 * 
 * @author Anthony Hunter, cmahoney
 */
public class CopyToImageUtil {

    /**
     * Creates a <code>DiagramEditPart</code> given the <code>Diagram</code>
     * without opening an editor.
     * 
     * @param diagram
     *            the <code>Diagram</code>
     * @param shell
     *            An out parameter for the shell that must be disposed after the
     *            copy to image operation has completed.
     * @param preferencesHint
     *            The preference hint that is to be used to find the appropriate
     *            preference store from which to retrieve diagram preference
     *            values. The preference hint is mapped to a preference store in
     *            the preference registry <@link DiagramPreferencesRegistry>.
     * @return the new populated <code>DiagramEditPart</code>
     */
    public DiagramEditPart createDiagramEditPart(Diagram diagram, Shell shell,
            PreferencesHint preferencesHint) {
        return OffscreenEditPartFactory.getInstance().createDiagramEditPart(
            diagram, shell, preferencesHint);
    }

    /**
     * Copies the diagram to an image file in the specified format.
     * 
     * @param diagram
     *            the diagram to be copied
     * @param destination
     *            the destination file, including path and file name
     * @param format
     *            the image file format
     * @param monitor
     *            progress monitor.
     * @param preferencesHint
     *            The preference hint that is to be used to find the appropriate
     *            preference store from which to retrieve diagram preference
     *            values. The preference hint is mapped to a preference store in
     *            the preference registry <@link DiagramPreferencesRegistry>.
     * @return A list of {@link PartPositionInfo} objects with details regarding
     *         each top-level editpart on the diagram represented in the image.
     * @exception CoreException
     *                if this method fails
     */
    public List copyToImage(Diagram diagram, IPath destination,
            ImageFileFormat format, IProgressMonitor monitor,
            PreferencesHint preferencesHint)
        throws CoreException {

        Trace.trace(DiagramUIRenderPlugin.getInstance(),
            "Copy diagram to Image " + destination + " as " + format); //$NON-NLS-1$ //$NON-NLS-2$ 

        Shell shell = new Shell();
        List partInfo = Collections.EMPTY_LIST;

        try {
            DiagramEditPart diagramEditPart = createDiagramEditPart(diagram,
                shell, preferencesHint);
            Assert.isNotNull(diagramEditPart);
            DiagramGenerator generator = copyToImage(diagramEditPart,
                destination, format, monitor);
            partInfo = generator.getDiagramPartInfo(diagramEditPart);
        } finally {
            shell.dispose();
        }

        return partInfo;
    }

    /**
     * Copies the diagram to an image file in the specified format.
     * 
     * @param diagramEP
     *            the diagram editpart
     * @param destination
     *            the destination file, including path and file name
     * @param format
     *            the image format to create.
     * @param monitor
     *            progress monitor.
     * @return The diagram generator used to copy the image.
     * @exception CoreException
     *                if this method fails
     */
    public DiagramGenerator copyToImage(DiagramEditPart diagramEP,
            IPath destination, ImageFileFormat format, IProgressMonitor monitor)
        throws CoreException {

        boolean found = false;
        DiagramGenerator gen = null;
        if (format.equals(ImageFileFormat.SVG)) {

            gen = new DiagramSVGGenerator(diagramEP);
            gen.createSWTImageDescriptorForDiagram();

            monitor.worked(1);

            saveSVGToFile(destination, (DiagramSVGGenerator) gen, monitor);
            return gen;
        } else if (format.equals(ImageFileFormat.JPEG)
                || format.equals(ImageFileFormat.PNG)) {

            String exportFormat = ImageExporter.JPEG_FILE;
            if (format.equals(ImageFileFormat.PNG))
                exportFormat = ImageExporter.PNG_FILE;

            gen = new DiagramImageGenerator(diagramEP);
            java.awt.Image image = gen.createAWTImageForDiagram();
            if (image instanceof BufferedImage) {
                ImageExporter.exportToFile(destination, (BufferedImage) image,
                    exportFormat, monitor);
                found = true;
            }
        }

        if (!found) {
            gen = new DiagramImageGenerator(diagramEP);
            Image image = gen.createSWTImageDescriptorForDiagram()
                .createImage();

            monitor.worked(1);

            saveToFile(destination, image, format, monitor);
            image.dispose();
        }

        monitor.worked(1);
        return gen;
    }

    /**
     * Copies the diagram to an image file in the specified format.
     * 
     * @param diagramEP
     *            the diagram edit part
     * @param selection
     *            selected shapes in the diagram.
     * @param destination
     *            the destination file, including path and file name
     * @param format
     *            the image format to create.
     * @param monitor
     *            progress monitor.
     * @exception CoreException
     *                if this method fails
     */
    public void copyToImage(DiagramEditPart diagramEP, List selection,
            IPath destination, ImageFileFormat format, IProgressMonitor monitor)
        throws CoreException {

        boolean found = false;
        if (format.equals(ImageFileFormat.SVG)) {

            DiagramSVGGenerator gen = new DiagramSVGGenerator(diagramEP);
            gen.createSWTImageDescriptorForParts(selection);

            monitor.worked(1);

            saveSVGToFile(destination, gen, monitor);
            found = true;
        } else if (format.equals(ImageFileFormat.JPEG)
            || format.equals(ImageFileFormat.PNG)) {

            String exportFormat = ImageExporter.JPEG_FILE;
            if (format.equals(ImageFileFormat.PNG))
                exportFormat = ImageExporter.PNG_FILE;

            java.awt.Image image = new DiagramImageGenerator(diagramEP)
                .createAWTImageForParts(selection);
            if (image instanceof BufferedImage) {
                ImageExporter.exportToFile(destination, (BufferedImage) image,
                    exportFormat, monitor);
                found = true;
            }
        }

        if (!found) {
            Image image = new DiagramImageGenerator(diagramEP)
                .createSWTImageDescriptorForParts(selection).createImage();

            monitor.worked(1);

            saveToFile(destination, image, format, monitor);
            image.dispose();
        }

        monitor.worked(1);
    }

    /**
     * Saves the image to a file.
     * 
     * @param destination
     *            the destination file, including path and file name
     * @param image
     *            the SWT image
     * @param imageFormat
     *            the selected image format
     * @param monitor
     *            progress monitor
     * @exception CoreException
     *                if this method fails
     */
    protected void saveToFile(IPath destination, Image image,
            ImageFileFormat imageFormat, IProgressMonitor monitor)
        throws CoreException {

        monitor.worked(1);
        
        ImageData imageData = image.getImageData();
        
        if (imageFormat.equals(ImageFileFormat.GIF) ||
                imageFormat.equals(ImageFileFormat.BMP))
            imageData = createImageData(image); 

        monitor.worked(1);
        createFile(destination);

        monitor.worked(1);
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.data = new ImageData[] {imageData};
        imageLoader.logicalScreenHeight = image.getBounds().width;
        imageLoader.logicalScreenHeight = image.getBounds().height;
        imageLoader.save(destination.toOSString(), imageFormat.getOrdinal());

        monitor.worked(1);
        refreshLocal(destination);
    }

    /**
     * Saves an SVG DOM to a file.
     * 
     * @param destination
     *            the destination file, including path and file name
     * @param generator
     *            the svg generator for a diagram, used to write
     * @param monitor
     *            the progress monitor
     * @exception CoreException
     *                if this method fails
     */
    protected void saveSVGToFile(IPath destination,
            DiagramSVGGenerator generator, IProgressMonitor monitor)
        throws CoreException {

        createFile(destination);
        monitor.worked(1);

        try {

            FileOutputStream os = new FileOutputStream(destination.toOSString());
            monitor.worked(1);

            generator.stream(os);
            monitor.worked(1);

            os.close();
            monitor.worked(1);
            refreshLocal(destination);
        } catch (IOException ex) {
            Log.error(DiagramUIRenderPlugin.getInstance(), IStatus.ERROR, ex
                .getMessage(), ex);
        }
    }

    /**
     * create a file in the workspace if the destination is in a project in the
     * workspace.
     * 
     * @param destination
     *            the destination file.
     * @exception CoreException
     *                if this method fails
     */
    private void createFile(IPath destination)
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
    }

    /**
     * refresh the file in the workspace if the destination is in a project in
     * the workspace.
     * 
     * @param destination
     *            the destination file.
     * @exception CoreException
     *                if this method fails
     */
    private void refreshLocal(IPath destination)
        throws CoreException {
        IFile file = ResourcesPlugin.getWorkspace().getRoot()
            .getFileForLocation(destination);
        if (file != null) {
            file.refreshLocal(IResource.DEPTH_ZERO, null);
        }
    }

    /**
     * Retrieve the image data for the image, using a palette of at most 256
     * colours.
     * 
     * @param image
     *            the SWT image.
     * @return new image data.
     */
    private ImageData createImageData(Image image) {

        ImageData imageData = image.getImageData();

        /**
         * If the image depth is 8 bits or less, then we can use the existing
         * image data.
         */
        if (imageData.depth <= 8) {
            return imageData;
        }

        /**
         * get an 8 bit imageData for the image
         */
        ImageData newImageData = get8BitPaletteImageData(imageData);

        /**
         * if newImageData is null, it has more than 256 colours. Use the web
         * safe pallette to get an 8 bit image data for the image.
         */
        if (newImageData == null) {
            newImageData = getWebSafePalletteImageData(imageData);
        }

        return newImageData;
    }

    /**
     * Retrieve an image data with an 8 bit palette for an image. We assume that
     * the image has less than 256 colours.
     * 
     * @param imageData
     *            the imageData for the image.
     * @return the new 8 bit imageData or null if the image has more than 256
     *         colours.
     */
    private ImageData get8BitPaletteImageData(ImageData imageData) {
        PaletteData palette = imageData.palette;
        RGB colours[] = new RGB[256];
        PaletteData newPaletteData = new PaletteData(colours);
        ImageData newImageData = new ImageData(imageData.width,
            imageData.height, 8, newPaletteData);

        int lastPixel = -1;
        int newPixel = -1;
        for (int i = 0; i < imageData.width; ++i) {
            for (int j = 0; j < imageData.height; ++j) {
                int pixel = imageData.getPixel(i, j);

                if (pixel != lastPixel) {
                    lastPixel = pixel;

                    RGB colour = palette.getRGB(pixel);
                    for (newPixel = 0; newPixel < 256; ++newPixel) {
                        if (colours[newPixel] == null) {
                            colours[newPixel] = colour;
                            break;
                        }
                        if (colours[newPixel].equals(colour))
                            break;
                    }

                    if (newPixel >= 256) {
                        /**
                         * Diagram has more than 256 colors, return null
                         */
                        return null;
                    }
                }

                newImageData.setPixel(i, j, newPixel);
            }
        }

        RGB colour = new RGB(0, 0, 0);
        for (int k = 0; k < 256; ++k) {
            if (colours[k] == null)
                colours[k] = colour;
        }

        return newImageData;
    }

    /**
     * If the image has less than 256 colours, simply create a new 8 bit palette
     * and map the colours to the new palatte.
     */
    private ImageData getWebSafePalletteImageData(ImageData imageData) {
        PaletteData palette = imageData.palette;
        RGB[] webSafePallette = getWebSafePallette();
        PaletteData newPaletteData = new PaletteData(webSafePallette);
        ImageData newImageData = new ImageData(imageData.width,
            imageData.height, 8, newPaletteData);

        int lastPixel = -1;
        int newPixel = -1;
        for (int i = 0; i < imageData.width; ++i) {
            for (int j = 0; j < imageData.height; ++j) {
                int pixel = imageData.getPixel(i, j);

                if (pixel != lastPixel) {
                    lastPixel = pixel;

                    RGB colour = palette.getRGB(pixel);
                    RGB webSafeColour = getWebSafeColour(colour);
                    for (newPixel = 0; newPixel < 216; ++newPixel) {
                        if (webSafePallette[newPixel].equals(webSafeColour))
                            break;
                    }

                    Assert.isTrue(newPixel < 216);
                }
                newImageData.setPixel(i, j, newPixel);
            }
        }

        return newImageData;
    }

    /**
     * Retrieves a web safe colour that closely matches the provided colour.
     * 
     * @param colour
     *            a colour.
     * @return the web safe colour.
     */
    private RGB getWebSafeColour(RGB colour) {
        int red = Math.round((colour.red + 25) / 51) * 51;
        int green = Math.round((colour.green + 25) / 51) * 51;
        int blue = Math.round((colour.blue + 25) / 51) * 51;
        return new RGB(red, green, blue);
    }

    /**
     * Retrieves a web safe pallette. Our palette will be 216 web safe colours
     * and the remaining filled with white.
     * 
     * @return array of 256 colours.
     */
    private RGB[] getWebSafePallette() {
        RGB[] colours = new RGB[256];
        int i = 0;
        for (int red = 0; red <= 255; red = red + 51) {
            for (int green = 0; green <= 255; green = green + 51) {
                for (int blue = 0; blue <= 255; blue = blue + 51) {
                    RGB colour = new RGB(red, green, blue);
                    colours[i++] = colour;
                }
            }
        }

        RGB colour = new RGB(0, 0, 0);
        for (int k = 0; k < 256; ++k) {
            if (colours[k] == null)
                colours[k] = colour;
        }

        return colours;
    }

}