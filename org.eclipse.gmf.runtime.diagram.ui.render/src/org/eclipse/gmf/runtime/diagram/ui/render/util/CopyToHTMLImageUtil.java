package org.eclipse.gmf.runtime.diagram.ui.render.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * Implementation of a utility class able to export specified editparts to
 * multiple image files, i.e. each file containing a tile of the diagram - all
 * tiles combined will compose the complete image of specified editparts. An
 * HTML file is generated which builds a table of tiled images of editparts.
 * HTML file allows clients to view the complete image of specified editparts.
 * The utility is useful to export huge diagrams to an image, when they fail to
 * be exported to a single image file.
 * 
 * @author Alex Boyko
 * 
 */
public class CopyToHTMLImageUtil extends CopyToImageUtil {

	/**
	 * A Map of image file formats to thei corresponding safe tile sizes
	 */
	private HashMap<ImageFileFormat, Dimension> imageFormatToTileSizeMap = null;

	/**
	 * The delimiter for tiled image filename table indices (filename +
	 * delimiter + row + delimiter + column)
	 */
	private String tileImageFileNameIndexDelimiter = StringStatics.UNDER_SCORE;

	{
		imageFormatToTileSizeMap = new HashMap<ImageFileFormat, Dimension>(
				ImageFileFormat.VALUES.length);
		imageFormatToTileSizeMap.put(ImageFileFormat.GIF, new Dimension(3000,
				3000));
		imageFormatToTileSizeMap.put(ImageFileFormat.BMP, new Dimension(3000,
				3000));
		imageFormatToTileSizeMap.put(ImageFileFormat.JPG, new Dimension(3000,
				3000));
		imageFormatToTileSizeMap.put(ImageFileFormat.JPEG, new Dimension(3000,
				3000));
		imageFormatToTileSizeMap.put(ImageFileFormat.PNG, new Dimension(3000,
				3000));
		imageFormatToTileSizeMap.put(ImageFileFormat.SVG, new Dimension(0, 0));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil#copyToImage(org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart, org.eclipse.core.runtime.IPath, org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public DiagramGenerator copyToImage(DiagramEditPart diagramEP,
			IPath destination, ImageFileFormat format, IProgressMonitor monitor)
			throws CoreException {
		
		DiagramGenerator gen = getDiagramGenerator(diagramEP, format);
		Dimension dimension = (Dimension) MapModeUtil.getMapMode(
				diagramEP.getFigure()).DPtoLP(
				imageFormatToTileSizeMap.get(format));

		/*
		 * Destination is the destination for HTML file. Hence we need to come up with
		 * the names for image file(s)
		 */
		IPath destinationFolder = destination.removeLastSegments(1);
		String fileName = destination.removeFileExtension().lastSegment();

		/*
		 * Create image tile files and get the number of tiles, i.e. number of
		 * rows and columns
		 */
		Point tiles = exportImage(gen, diagramEP.getPrimaryEditParts(),
				destinationFolder, fileName, format, dimension.width,
				dimension.height, monitor);

		/*
		 * Create the HTML file
		 */
		createHTMLFileForTiledImage(destination, fileName, format.getName()
				.toLowerCase(), tiles.y, tiles.x);
		return gen;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil#copyToImage(org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart, java.util.List, org.eclipse.core.runtime.IPath, org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void copyToImage(DiagramEditPart diagramEP, List selection,
			IPath destination, ImageFileFormat format, IProgressMonitor monitor)
			throws CoreException {

		DiagramGenerator gen = getDiagramGenerator(diagramEP, format);

		Dimension dimension = (Dimension) MapModeUtil.getMapMode(
				diagramEP.getFigure()).DPtoLP(
				imageFormatToTileSizeMap.get(format));

		/*
		 * Destination is the destination for HTML file. Hence we need to come up with
		 * the names for image file(s)
		 */
		IPath destinationFolder = destination.removeLastSegments(1);
		String fileName = destination.removeFileExtension().lastSegment();

		/*
		 * Create image tile files and get the number of tiles, i.e. number of
		 * rows and columns
		 */
		Point tiles = exportImage(gen, selection, destinationFolder, fileName, format,
				dimension.width, dimension.height, monitor);
		
		/*
		 * Create the HTML file
		 */
		createHTMLFileForTiledImage(destination, fileName, format.getName()
				.toLowerCase(), tiles.y, tiles.x);
	}

	/**
	 * Export the editparts to an image file or files of supplied image format
	 * depending on the logical size of the tile. If there is only one tile -
	 * the image filename won't have indices appended. The method return the
	 * number of created tiles as a <code>Point</code>, where x represents
	 * number of columns and y number of rows.
	 * 
	 * 
	 * @param gen
	 *            diagram generator
	 * @param editParts
	 *            editparts
	 * @param destinationFolder
	 *            destination folder for image files
	 * @param fileName
	 *            common part of image file names
	 * @param imageFormat
	 *            image file format
	 * @param logTileWidth
	 *            tile width in logical units (not device units)
	 * @param logTileHeight
	 *            tile height in logical units (not device units)
	 * @param monitor
	 *            progress monitor
	 * @return <code>Point</code>, where x represents number of columns and y
	 *         represents number of rows
	 * @throws Error
	 * @throws CoreException
	 */
	private Point exportImage(DiagramGenerator gen, List editParts,
			IPath destinationFolder, String fileName,
			ImageFileFormat imageFormat, int logTileWidth, int logTileHeight,
			IProgressMonitor monitor) throws Error, CoreException {
		org.eclipse.swt.graphics.Rectangle diagramArea = gen
				.calculateImageRectangle(editParts);
		int rows = 1, columns = 1;
		if (logTileWidth <= 0 || logTileHeight <= 0) {
			IPath imageFileDestination = new Path(destinationFolder.toString())
					.append(fileName).addFileExtension(
							imageFormat.getName().toLowerCase());
			copyToImage(gen, editParts, diagramArea, imageFileDestination,
					imageFormat, monitor);
		} else {
			columns = (int) Math.ceil(diagramArea.width
					/ ((double) logTileWidth));
			rows = (int) Math.ceil(diagramArea.height
					/ ((double) logTileHeight));
			int jobsToDo = 6 * columns * rows + 1;
			monitor
					.beginTask(
							DiagramUIMessages.CopyToHTMLImageTask_exportingToHTML,
							jobsToDo);
			for (int i = 0; i < rows; i++) {
				int sourceY = i * logTileHeight + diagramArea.y;
				int sourceHeight = Math.min(logTileHeight, diagramArea.height
						- logTileHeight * i);
				for (int j = 0; j < columns; j++) {
					int sourceX = diagramArea.x + j * logTileWidth;
					int sourceWidth = Math.min(logTileWidth, diagramArea.width
							- logTileWidth * j);
					String tileFileName = fileName
							+ getTileImageFileNameIndexDelimiter() + i
							+ getTileImageFileNameIndexDelimiter() + j
							+ StringStatics.PERIOD
							+ imageFormat.getName().toLowerCase();
					IPath tilePath = new Path(destinationFolder.toOSString())
							.append(tileFileName);
					monitor.subTask(DiagramUIMessages.CopyToHTMLImageTask_generateImageFile + tilePath);
					copyToImage(gen, editParts,
							new org.eclipse.swt.graphics.Rectangle(sourceX,
									sourceY, sourceWidth, sourceHeight),
							tilePath, imageFormat, monitor);
				}
			}
		}
		return new Point(columns, rows);
	}

	/**
	 * Creates an HTML file that contains a table of image tiles. Assumed, that
	 * if table size is one, there are no indices appended to the tile iamge
	 * file name.
	 * 
	 * @param htmlFileLocation
	 *            location of the HTML file
	 * @param fileName
	 *            common part of the file name for image tiles
	 * @param fileExtension
	 *            extension of image files
	 * @param numRows
	 *            number of rows for the table
	 * @param numColumns
	 *            number of columns for the table
	 * @return <code>Status.OK_STATUS</code> if everything went without errors
	 */
	private IStatus createHTMLFileForTiledImage(IPath htmlFileLocation,
			String fileName, String fileExtension, int numRows, int numColumns) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					htmlFileLocation.toOSString()));
			out
					.write("<html>\n<body>\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"RIGHT\">\n");//$NON-NLS-1$
			if (numRows == 1 && numColumns == 1) {
				out.write("<tr>\n");//$NON-NLS-1$
				out.write("\t<td><img src=\"");//$NON-NLS-1$
				out.write(fileName + StringStatics.PERIOD + fileExtension);
				out.write("\"></td>\n");//$NON-NLS-1$
				out.write("</tr>\n");//$NON-NLS-1$				
			} else {
				for (int i = 0; i < numRows; i++) {
					out.write("<tr>\n");//$NON-NLS-1$
					for (int j = 0; j < numColumns; j++) {
						out.write("\t<td><img src=\"");//$NON-NLS-1$
						out.write(fileName
								+ getTileImageFileNameIndexDelimiter() + i
								+ getTileImageFileNameIndexDelimiter() + j
								+ StringStatics.PERIOD + fileExtension);
						out.write("\"></td>\n");//$NON-NLS-1$
					}
					out.write("</tr>\n");//$NON-NLS-1$
				}
			}
			out.write("</table>\n</body>\n</html>");//$NON-NLS-1$
			out.close();
			IFile file = ResourcesPlugin.getWorkspace().getRoot()
					.getFileForLocation(htmlFileLocation);
			if (file != null) {
				file.refreshLocal(IResource.DEPTH_ZERO, null);
				return FileModificationValidator
						.approveFileModification(new IFile[] { file });
			}
		} catch (IOException e) {
			return Status.CANCEL_STATUS;
		} catch (CoreException e) {
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	/**
	 * Gets the tile image file name indices delimiter
	 * 
	 * @return the delimiter
	 */
	public String getTileImageFileNameIndexDelimiter() {
		return tileImageFileNameIndexDelimiter;
	}

	/**
	 * Sets the tile image file name indices delimiter. The new value must not
	 * be empty string or null.
	 * 
	 * @param tileImageFileNameIndexDelimiter
	 *            new delimiter value
	 */
	public void setTileImageFileNameIndexDelimiter(
			String tileImageFileNameIndexDelimiter) {
		if (tileImageFileNameIndexDelimiter == null
				|| tileImageFileNameIndexDelimiter.length() == 0)
			throw new IllegalArgumentException();
		this.tileImageFileNameIndexDelimiter = tileImageFileNameIndexDelimiter;
	}

	/**
	 * Gets the map of image file formats to their corresponding safe tile sizes
	 * 
	 * @return the map
	 */
	public HashMap<ImageFileFormat, Dimension> getImageFormatToTileSizeMap() {
		return imageFormatToTileSizeMap;
	}
}
