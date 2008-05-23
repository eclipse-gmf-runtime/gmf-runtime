/******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.image.PartPositionInfo;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.util.DiagramEditorUtil;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.widgets.Shell;

/**
 * Implementation of a utility class able to export specified editparts to
 * multiple image files, i.e. each file containing a tile of the diagram - all
 * tiles combined will compose the complete image of specified editparts. An
 * HTML file is generated which builds a table of tiled images of editparts.
 * HTML file allows clients to view the complete image of specified editparts.
 * The utility is useful to export huge diagrams to an image, when they fail to
 * be exported to a single image file.
 * 
 * <li><b>Note:</b> for tiled images method <code>#copyToImage(Diagram, IPath,
 * ImageFileFormat, IProgressMonitor, PreferencesHint)}</code> returns a matrix, the
 * dimension of which is equal to the total number of tiles rows and columns.
 * Each cell of the matrix is a list of <code>PartPositionInfo</code>
 * corresponding to the tile with the same index.</li>
 * 
 * @author Alex Boyko
 * 
 */
public class CopyToHTMLImageUtil extends CopyToImageUtil {

	/**
	 * A Map of image file formats to their corresponding safe tile sizes
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
		imageFormatToTileSizeMap.put(ImageFileFormat.PDF, new Dimension(0, 0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil#copyToImage(org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart,
	 *      org.eclipse.core.runtime.IPath,
	 *      org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public DiagramGenerator copyToImage(DiagramEditPart diagramEP,
			IPath destination, ImageFileFormat format, IProgressMonitor monitor)
			throws CoreException {

		ExportInfo exportInfo = copyToImageAndReturnInfo(diagramEP, diagramEP
				.getPrimaryEditParts(), destination, format, monitor);

		/*
		 * Create the HTML file
		 */
		createHTMLFileForTiledImage(destination, exportInfo);

		return exportInfo.diagramGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil#copyToImage(org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart,
	 *      java.util.List, org.eclipse.core.runtime.IPath,
	 *      org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void copyToImage(DiagramEditPart diagramEP, List selection,
			IPath destination, ImageFileFormat format, IProgressMonitor monitor)
			throws CoreException {

		ExportInfo exportInfo = copyToImageAndReturnInfo(diagramEP, selection,
				destination, format, monitor);

		/*
		 * Create the HTML file
		 */
		createHTMLFileForTiledImage(destination, exportInfo);
	}
	
	/**
	 * Generates image files and returns the HTML content as a String
	 * 
	 * @param diagram diagram model
	 * @param destination a path to image files with common image file name
	 * @param format image format
	 * @param monitor progress monitor
	 * @return HTML content as a string
	 * @throws CoreException
	 */
	public String generateHTMLImage(Diagram diagram, IPath destination, ImageFileFormat format, IProgressMonitor monitor) throws CoreException {
		ExportInfo exportInfo = null;
		DiagramEditor openedDiagramEditor = DiagramEditorUtil
				.findOpenedDiagramEditorForID(ViewUtil.getIdStr(diagram));
		if (openedDiagramEditor != null) {
			DiagramEditPart diagramEditPart = openedDiagramEditor
					.getDiagramEditPart();
			exportInfo = copyToImageAndReturnInfo(diagramEditPart,
					diagramEditPart.getPrimaryEditParts(), destination, format,
					monitor);
		} else {
			Shell shell = new Shell();
			try {
				DiagramEditPart diagramEditPart = createDiagramEditPart(
						diagram, shell, null);
				Assert.isNotNull(diagramEditPart);
				exportInfo = copyToImageAndReturnInfo(diagramEditPart,
						diagramEditPart.getPrimaryEditParts(), destination,
						format, monitor);
			} finally {
				shell.dispose();
			}
		}
		return createHTMLString(exportInfo);
	}

	/**
	 * Export the editparts to an image file or files of supplied image format
	 * depending on the logical size of the tile. The method return the number
	 * of created tiles as a <code>Point</code>, where x represents number of
	 * columns and y number of rows.
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
	private ExportInfo exportImage(DiagramGenerator gen, List editParts,
			IPath destinationFolder, String fileName,
			ImageFileFormat imageFormat, Dimension logTileSize,
			IProgressMonitor monitor) throws Error, CoreException {
		org.eclipse.swt.graphics.Rectangle diagramArea = gen
				.calculateImageRectangle(editParts);
		int rows = 1, columns = 1;
		int logTileWidth = logTileSize.width;
		int logTileHeight = logTileSize.height;
		if (logTileWidth <= 0) {
			logTileWidth = diagramArea.width;
		}
		if (logTileHeight <= 0) {
			logTileHeight = diagramArea.height;
		}
		columns = (int) Math.ceil(diagramArea.width / ((double) logTileWidth));
		rows = (int) Math.ceil(diagramArea.height / ((double) logTileHeight));
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
				monitor
						.subTask(DiagramUIMessages.CopyToHTMLImageTask_generateImageFile
								+ tilePath);
				copyToImage(gen, editParts,
						new org.eclipse.swt.graphics.Rectangle(sourceX,
								sourceY, sourceWidth, sourceHeight), tilePath,
						imageFormat, monitor);
			}
		}
		return new ExportInfo(gen, new Point(columns, rows), fileName, destinationFolder, imageFormat, new Dimension(logTileWidth, logTileHeight));
	}

	/**
	 * Creates an HTML file that contains a table of image tiles.
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
			ExportInfo info) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					htmlFileLocation.toOSString()));
			out
					.write(createHTMLString(info));
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
	
	private String createHTMLString(ExportInfo info) {
		Assert.isNotNull(info);
		StringBuffer buffer = new StringBuffer(
				"<html>\n<body>\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"CENTER\">\n");//$NON-NLS-1$
		String commonFileNamePath = new Path("file://", info.directory.toString()).append(info.commonTileFileName).makeAbsolute().toString(); 
		for (int i = 0; i < info.tiles.y; i++) {
			buffer.append("<tr>\n");//$NON-NLS-1$
			for (int j = 0; j < info.tiles.x; j++) {
				String fileName = commonFileNamePath
						+ getTileImageFileNameIndexDelimiter() + i
						+ getTileImageFileNameIndexDelimiter() + j
						+ StringStatics.PERIOD
						+ info.imageFormat.getName().toLowerCase();
				if (ImageFileFormat.SVG.equals(info.imageFormat)) {
					buffer.append("\t<td>\n\t\t<object data=\"");//$NON-NLS-1$
					buffer.append(fileName);
					buffer.append("\" type=\"image/svg+xml\" width=\"");//$NON-NLS-1$
					buffer.append(info.tileSize.width);//$NON-NLS-1$
					buffer.append("\" height=\"");
					buffer.append(info.tileSize.height);
					buffer.append("\">\n");//$NON-NLS-1$
					buffer.append("\t\t<embed src=\"");//$NON-NLS-1$
					buffer.append(fileName);
					buffer.append("\" type=\"image/svg+xml\" width=\"");//$NON-NLS-1$
					buffer.append(info.tileSize.width);
					buffer.append("\" height=\"");//$NON-NLS-1$
					buffer.append(info.tileSize.height);
					buffer.append("\"/></td>\n");
				} else {
					buffer.append("\t<td><img src=\"");//$NON-NLS-1$
					buffer.append(fileName);
					buffer.append("\"/></td>\n");//$NON-NLS-1$
				}
			}
			buffer.append("</tr>\n");//$NON-NLS-1$
		}
		buffer.append("</table>\n</body>\n</html>");//$NON-NLS-1$
		return buffer.toString();
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

	/**
	 * Defines the data structure for the info of exported diagram
	 * 
	 * @author Alex Boyko
	 * 
	 */
	public class ExportInfo {
		final public DiagramGenerator diagramGenerator;
		final public Point tiles;
		final public String commonTileFileName;
		final public IPath directory;
		final public ImageFileFormat imageFormat;
		final public Dimension tileSize;

		ExportInfo(DiagramGenerator diagramGenerator, Point tiles,
				String commonTileFileName, IPath directory, ImageFileFormat imageFormat, Dimension tileSize) {
			this.diagramGenerator = diagramGenerator;
			this.tiles = tiles;
			this.commonTileFileName = commonTileFileName;
			this.directory = directory;
			this.imageFormat = imageFormat;
			this.tileSize = tileSize;
		}
	}

	/**
	 * Exports the diagram to tiled images files and returns the info about the
	 * exported diagram (total number of rows and columns for tiles, generator
	 * and common part of image files name, i.e. without indices)
	 * 
	 * @param diagramEP
	 *            diafram editpart
	 * @param selection
	 *            selected editparts
	 * @param destination
	 *            destination path
	 * @param format
	 *            image format
	 * @param monitor
	 *            progress monitor
	 * @return <code>ExportInfo</code> of images
	 * @throws CoreException
	 */
	public ExportInfo copyToImageAndReturnInfo(DiagramEditPart diagramEP,
			List selection, IPath destination, ImageFileFormat format,
			IProgressMonitor monitor) throws CoreException {
		DiagramGenerator gen = getDiagramGenerator(diagramEP, format);
		
		IMapMode mm = MapModeUtil.getMapMode(
				diagramEP.getFigure()); 

		Dimension dimension = (Dimension) mm.DPtoLP(
				imageFormatToTileSizeMap.get(format).getCopy());

		/*
		 * Destination is the destination for HTML file. Hence we need to come
		 * up with the names for image file(s)
		 */
		IPath destinationFolder = destination.removeLastSegments(1);
		String fileName = destination.removeFileExtension().lastSegment();

		/*
		 * Create image tile files and get the number of tiles, i.e. number of
		 * rows and columns
		 */
		ExportInfo info  = exportImage(gen, selection, destinationFolder, fileName,
				format, dimension, monitor);
		
		/*
		 * The tile dimension returned with the ExportInfo object is in logical units. We need to translate it
		 * to the device units - pixels. We can't simply use the pixel tile size from the imageFormatToTilesSizeMap
		 * since tile size (x,y), where x<=0 and y<=0 will use the diagram width and/or height 
		 */
		mm.LPtoDP(info.tileSize);

		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil#copyToImage(org.eclipse.gmf.runtime.notation.Diagram,
	 *      org.eclipse.core.runtime.IPath,
	 *      org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat,
	 *      org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint)
	 */
	public List copyToImage(Diagram diagram, IPath destination,
			ImageFileFormat format, IProgressMonitor monitor,
			PreferencesHint preferencesHint) throws CoreException {
		Trace.trace(DiagramUIRenderPlugin.getInstance(),
				"Copy diagram to Image " + destination + " as " + format); //$NON-NLS-1$ //$NON-NLS-2$

		ExportInfo exportInfo = null;
		List partsInfo = Collections.EMPTY_LIST;

		DiagramEditor openedDiagramEditor = DiagramEditorUtil
				.findOpenedDiagramEditorForID(ViewUtil.getIdStr(diagram));
		if (openedDiagramEditor != null) {
			DiagramEditPart diagramEditPart = openedDiagramEditor
					.getDiagramEditPart();
			exportInfo = copyToImageAndReturnInfo(diagramEditPart,
					diagramEditPart.getPrimaryEditParts(), destination, format,
					monitor);
			partsInfo = exportInfo.diagramGenerator
					.getDiagramPartInfo(diagramEditPart);
		} else {
			Shell shell = new Shell();
			try {
				DiagramEditPart diagramEditPart = createDiagramEditPart(
						diagram, shell, preferencesHint);
				Assert.isNotNull(diagramEditPart);
				exportInfo = copyToImageAndReturnInfo(diagramEditPart,
						diagramEditPart.getPrimaryEditParts(), destination,
						format, monitor);
				partsInfo = exportInfo.diagramGenerator
						.getDiagramPartInfo(diagramEditPart);
			} finally {
				shell.dispose();
			}
		}

		/*
		 * Transform the list of partsInfo for a diagram exported to a single
		 * image into a matrix of partsInfo lists, the rows of columns of which
		 * correspond to rows and columns of tiled images
		 */
		return createTilesPartsInfoList(exportInfo);
	}

	/**
	 * Creates and returns a matrix each cell of which correspond to a tiled
	 * image with the same index. Each cell of a matrix, therefore, is a
	 * partsInfo list for a tiled image (chunk of a diagram) corresponding to
	 * the index of the cell.
	 * 
	 * @param exportInfo
	 *            export info
	 * @param partsInfo
	 *            partsInfo list for the whole diagram (single image assumed)
	 * @param format
	 *            image format
	 * @return A matrix of partsInfo lists, where each cell contains partsInfo
	 *         list for a tiled image of the same index
	 */
	public static List<List<List<PartPositionInfo>>> createTilesPartsInfoList(
			ExportInfo exportInfo) {
		List partsInfo = exportInfo.diagramGenerator.getDiagramPartInfo();
		List<List<List<PartPositionInfo>>> tilesPartsInfoList = Collections.EMPTY_LIST;
		if (exportInfo.diagramGenerator != null && exportInfo.tiles.x > 0
				&& exportInfo.tiles.y > 0 && partsInfo != null) {
			/*
			 * Create the matrix
			 */
			tilesPartsInfoList = initializeTilesPartsInfoList(
					exportInfo.tiles.y, exportInfo.tiles.x);
			/*
			 * If it's 1 tile only than just use partsInfo list already created
			 * for it
			 */
			if (exportInfo.tiles.x == 1 && exportInfo.tiles.y == 1) {
				tilesPartsInfoList.get(0).set(0, partsInfo);
			} else {
				Dimension tileSize = exportInfo.tileSize;
				Rectangle defaultTile = new Rectangle(new Point(), tileSize);
				/*
				 * Iterate through each part and split it in different tiles if
				 * necessary
				 */
				for (Iterator itr = partsInfo.iterator(); itr.hasNext();) {
					PartPositionInfo info = (PartPositionInfo) itr.next();
					if (info.getPolyline() == null) {
						/*
						 * It's a shape
						 */
						Point startCell = new Point();
						startCell.x = info.getPartX() / tileSize.width;
						startCell.y = info.getPartY() / tileSize.height;
						Point endCell = new Point();
						endCell.x = (info.getPartX() + info.getPartWidth())
								/ tileSize.width;
						endCell.y = (info.getPartY() + info.getPartHeight())
								/ tileSize.height;
						for (int i = startCell.y; i <= endCell.y; i++) {
							for (int j = startCell.x; j <= endCell.x; j++) {
								Rectangle shapeRect = new Rectangle(info
										.getPartX(), info.getPartY(), info
										.getPartWidth(), info.getPartHeight());
								shapeRect.translate(-j * tileSize.width, -i
										* tileSize.height);
								Rectangle intersection = shapeRect
										.intersect(defaultTile);
								PartPositionInfo newInfo = new PartPositionInfo();
								newInfo.setSemanticElement(info
										.getSemanticElement());
								newInfo.setPartHeight(intersection.height);
								newInfo.setPartWidth(intersection.width);
								newInfo.setPartX(intersection.x);
								newInfo.setPartY(intersection.y);
								tilesPartsInfoList.get(i).get(j).add(newInfo);
							}
						}
					} else {
						/*
						 * It's a connection. Connections location info is a
						 * polygon with points in Clock-Wise (CW) order. We need
						 * to split the connection in line segments to process
						 * its info. (@see
						 * DiagramGenerator#getDiagramPartInfo()) First half of
						 * points in the list are points offset from the
						 * original connection bend points in the same
						 * direction. We need to consider line segments of the
						 * connection. Hence we need to consider original line
						 * segment and the offset one and split it between
						 * different tiles
						 */
						for (int i = 1; i <= info.getPolyline().size() / 2; i += 2) {
							/*
							 * Upper points are the end points of the offset
							 * line segment
							 */
							Point upperStartPt = info.getPolyline().get(i - 1);
							Point upperEndPt = info.getPolyline().get(i);
							/*
							 * Lower points are the end points of the original
							 * line segment
							 */
							Point lowerEndPt = info.getPolyline().get(
									info.getPolyline().size() - 1 - i);
							Point lowerStartPt = info.getPolyline().get(
									info.getPolyline().size() - 1 - (i + 1));
							/*
							 * A set of cells laying inside and intersected by
							 * the polygon formed by the end points of the
							 * original and offset line segments
							 */
							HashSet<Point> cells = new HashSet<Point>();
							/*
							 * Create maps of line segments (vectors) created by
							 * intersections of edges of the polygon with tile
							 * borders. Cell indices are mapped to line segments
							 * (vectors). The line segments or vectors
							 * directions are CW such that they match the
							 * direction of the polygon points in the polyLine
							 * list from the single image partsInfo list.
							 */
							HashMap<Point, LineSeg> upperLineSegs = getMapOfLineSegments(
									upperStartPt, upperEndPt, tileSize, cells);
							HashMap<Point, LineSeg> lowerLineSegs = getMapOfLineSegments(
									lowerStartPt, lowerEndPt, tileSize, cells);
							HashMap<Point, LineSeg> upperToLowerLineSegs = getMapOfLineSegments(
									upperEndPt, lowerStartPt, tileSize, cells);
							HashMap<Point, LineSeg> lowerToUpperLineSegs = getMapOfLineSegments(
									lowerEndPt, upperStartPt, tileSize, cells);
							/*
							 * Create a connection polygon for each tile
							 * intersected by the polygon. We assume there are
							 * no cells completely contained within this
							 * polygon!!! Tile dimension are much larger than
							 * the offset of the secondary (referred as offset)
							 * line segment
							 */
							for (Iterator<Point> ptItr = cells.iterator(); ptItr
									.hasNext();) {
								Point cell = ptItr.next();
								LineSeg upperSeg = upperLineSegs.get(cell);
								LineSeg lowerSeg = lowerLineSegs.get(cell);
								LineSeg upperToLowerSeg = upperToLowerLineSegs
										.get(cell);
								LineSeg lowerToUpperSeg = lowerToUpperLineSegs
										.get(cell);
								List<LineSeg> cwListOfLineSegs = new ArrayList<LineSeg>(
										4);
								if (upperSeg != null) {
									cwListOfLineSegs.add(upperSeg);
								}
								if (upperToLowerSeg != null) {
									cwListOfLineSegs.add(upperToLowerSeg);
								}
								if (lowerSeg != null) {
									cwListOfLineSegs.add(lowerSeg);
								}
								if (lowerToUpperSeg != null) {
									cwListOfLineSegs.add(lowerToUpperSeg);
								}
								PartPositionInfo newInfo = new PartPositionInfo();
								newInfo.setSemanticElement(info
										.getSemanticElement());
								newInfo.setPolyline(createCellPolyline(
										tileSize, cwListOfLineSegs));
								tilesPartsInfoList.get(cell.y).get(cell.x).add(
										newInfo);
							}
						}
					}
				}
			}
		}
		return tilesPartsInfoList;
	}

	/**
	 * Creates a matrix of the dimension given through rows and columns
	 * parameters. Each cell of the matrix is a <code>PartPositionInfo</code>
	 * list
	 * 
	 * @param rows
	 *            roes
	 * @param columns
	 *            columns
	 * @return the matrix
	 */
	private static List<List<List<PartPositionInfo>>> initializeTilesPartsInfoList(
			int rows, int columns) {
		List<List<List<PartPositionInfo>>> tilesPartsInfoList = new ArrayList<List<List<PartPositionInfo>>>(
				rows);
		for (int i = 0; i < rows; i++) {
			List<List<PartPositionInfo>> row = new ArrayList<List<PartPositionInfo>>(
					columns);
			for (int j = 0; j < columns; j++) {
				row.add(new LinkedList<PartPositionInfo>());
			}
			tilesPartsInfoList.add(row);
		}
		return tilesPartsInfoList;
	}

	/**
	 * A class to make comparisons between points laying on the same line.
	 * Comparison criteria is the closeness of points to the segmentOrigin point
	 * 
	 * @author aboyko
	 * 
	 */
	private static class LineSegmentPointsComparator implements Comparator<Point> {

		private Point segmentOrigin;

		public LineSegmentPointsComparator(Point segmentOrigin) {
			this.segmentOrigin = segmentOrigin;
		}

		public int compare(Point p1, Point p2) {
			if (p1.x == p2.x) {
				return Math.abs(p1.y - segmentOrigin.y)
						- Math.abs(p2.y - segmentOrigin.y);
			}
			return Math.abs(p1.x - segmentOrigin.x)
					- Math.abs(p2.x - segmentOrigin.x);
		}

	}

	/**
	 * Creates a map of cell indices to line segments. Cell indices stand for
	 * cells intersected by the line segments originating at parameter
	 * startPoint and ending at endPoint parameter and line segment is the chunk
	 * of the original line segment laying within a particular tile. Coordinates
	 * of the start and end point of the segment are relative to the tile. All
	 * intersected tiles are inserted into <code>cells</code> set
	 * 
	 * @param startPoint
	 *            line segment's start point
	 * @param endPoint
	 *            line segment's end point
	 * @param tileSize
	 *            the tile dimension
	 * @param cells
	 *            set of cells to be updated
	 * @return the map of cell indices to line segments contained within
	 *         corresponding cells
	 */
	private static HashMap<Point, LineSeg> getMapOfLineSegments(Point startPoint,
			Point endPoint, Dimension tileSize, HashSet<Point> cells) {
		HashMap<Point, LineSeg> map = new HashMap<Point, LineSeg>();
		Point startCell = new Point();
		startCell.x = startPoint.x / tileSize.width;
		startCell.y = startPoint.y / tileSize.height;
		Point endCell = new Point();
		endCell.x = endPoint.x / tileSize.width;
		endCell.y = endPoint.y / tileSize.height;
		if (startCell.equals(endCell)) {
			/*
			 * If within the same cell then just translate the points, update
			 * the cells set and return
			 */
			map.put(startCell, new LineSeg(startPoint.getCopy().translate(
					-startCell.x * tileSize.width,
					-startCell.y * tileSize.height), endPoint.getCopy()
					.translate(-startCell.x * tileSize.width,
							-startCell.y * tileSize.height)));
			cells.add(startCell);
		} else {
			/*
			 * If the end points are not within the same cell then calculate the
			 * line equation and, using that equation, calculate the
			 * intersection points with tile borders
			 */
			double[] equation = LineSeg.getLineEquation(startPoint.x,
					startPoint.y, endPoint.x, endPoint.y);
			List<Point> linePoints = new ArrayList<Point>(2
					+ Math.abs(startCell.x - endCell.x)
					+ Math.abs(startCell.y - endCell.y));
			if (equation[1] != 0) {
				for (int x = tileSize.width
						* (Math.min(startCell.x, endCell.x) + 1); x <= tileSize.width
						* Math.max(startCell.x, endCell.x); x += tileSize.width) {
					linePoints.add(new Point(x, Math
							.round((equation[2] - equation[0] * x)
									/ equation[1])));
				}
			}
			if (equation[0] != 0) {
				for (int y = tileSize.height
						* (Math.min(startCell.y, endCell.y) + 1); y <= tileSize.height
						* Math.max(startCell.y, endCell.y); y += tileSize.height) {
					linePoints.add(new Point(Math
							.round((equation[2] - equation[1] * y)
									/ equation[0]), y));
				}
			}
			/*
			 * Sort the intersection points such that points closer to the start
			 * of the original line segment are located at the start of the
			 * list.
			 */
			Collections.sort(linePoints, new CopyToHTMLImageUtil.LineSegmentPointsComparator(
					startPoint));
			/*
			 * Add the ends of the original line segments to the start and end
			 * of the list
			 */
			linePoints.add(0, startPoint.getCopy());
			linePoints.add(endPoint.getCopy());
			/*
			 * Now calculate the indices of the cells intersected by the
			 * original line segment and fill the map with the values of line
			 * segments laying in the corresponding cells. Line segments are
			 * relative to the corresponding cell
			 */
			Point currentCell = startCell;
			Iterator<Point> pointsItr = linePoints.iterator();
			Point originPoint = pointsItr.next();
			for (; pointsItr.hasNext();) {
				Point terminusPoint = pointsItr.next();
				Point translatedOrigin = originPoint.getCopy().translate(
						-currentCell.x * tileSize.width,
						-currentCell.y * tileSize.height);
				Point translatedTerminus = terminusPoint.getCopy().translate(
						-currentCell.x * tileSize.width,
						-currentCell.y * tileSize.height);
				map.put(currentCell.getCopy(), new LineSeg(translatedOrigin,
						translatedTerminus));
				cells.add(currentCell.getCopy());
				if (translatedTerminus.x == 0) {
					currentCell.x--;
				}
				if (translatedTerminus.y == 0) {
					currentCell.y--;
				}
				if (translatedTerminus.x == tileSize.width) {
					currentCell.x++;
				}
				if (translatedTerminus.y == tileSize.height) {
					currentCell.y++;
				}
				originPoint = terminusPoint;
			}
		}
		return map;
	}

	/**
	 * It is assumed that segments parameter contains either line segments where
	 * the end of one segments is the start of the next one or the end of one
	 * segments and the start of the next one lay on the cell's edges. The
	 * method in this case will create a polygon by connecting line segments
	 * that don't meet each other via cell's edges in the clock wise direction
	 * 
	 * @param cellSize
	 *            dimension of the cell
	 * @param segments
	 *            list of line segments
	 * @return a list of polygon points.
	 */
	private static List<Point> createCellPolyline(Dimension cellSize,
			List<LineSeg> segments) {
		List<Point> result = new LinkedList<Point>();
		if (segments.size() > 0) {
			LineSeg currentSegment = segments.get(0);
			for (int i = 0; i < segments.size(); i++) {
				LineSeg nextSegment = segments.get((i + 1) % segments.size());
				result.add(currentSegment.getTerminus());
				if (!currentSegment.getTerminus().equals(
						nextSegment.getOrigin())) {
					List<Point> connectingPoints = connectLineSegmentsEndsViaCellEdges(
							cellSize, currentSegment, nextSegment);
					for (Iterator<Point> itr = connectingPoints.iterator(); itr
							.hasNext();) {
						result.add(itr.next());
					}
					result.add(nextSegment.getOrigin());
				}
				currentSegment = nextSegment;
			}
			result.add(result.get(0));
		}
		return result;
	}

	/**
	 * Creates a list of connecting points that help to connect 2 line segments
	 * via cell's edges clock wise
	 * 
	 * @param cellSize
	 *            dimesnion of the cell
	 * @param lineSeg1
	 *            1st line segment
	 * @param lineSeg2
	 *            2nd line segment
	 * @return the list of connecting points
	 */
	private static List<Point> connectLineSegmentsEndsViaCellEdges(Dimension cellSize,
			LineSeg lineSeg1, LineSeg lineSeg2) {
		Point current = lineSeg1.getTerminus();
		Point next = lineSeg2.getOrigin();
		List<Point> result = new LinkedList<Point>();
		List<Point> cwCellVertices = createClockwiseListOfCellVertices(cellSize);
		int currentIdx = indexOfCellEdgePointClockwise(cellSize, current);
		int nextIdx = indexOfCellEdgePointClockwise(cellSize, next);
		for (int i = currentIdx; i != nextIdx;) {
			result.add(cwCellVertices.get(i));
			i = (i + 1) % cwCellVertices.size();
		}
		return result;
	}

	/**
	 * Creates a list of rectangular cell vertices in the clock wise order
	 * 
	 * @param cellSize
	 *            cell's dimension
	 * @return a list of vertices in the clock wise order
	 */
	private static List<Point> createClockwiseListOfCellVertices(Dimension cellSize) {
		List<Point> cellVertices = new ArrayList<Point>(4);
		cellVertices.add(new Point());
		cellVertices.add(new Point(cellSize.width, 0));
		cellVertices.add(new Point(cellSize.width, cellSize.height));
		cellVertices.add(new Point(0, cellSize.height));
		return cellVertices;
	}

	/**
	 * Returns the index of a point in the list of cell's vertices order in the
	 * clock wise manner
	 * 
	 * @param cellSize
	 *            cell's dimension
	 * @param pt
	 *            point
	 * @return index of the in the list of clock wise vertices
	 */
	private static int indexOfCellEdgePointClockwise(Dimension cellSize, Point pt) {
		if (pt.x == 0) {
			return 0;
		} else if (pt.y == 0) {
			return 1;
		} else if (pt.x == cellSize.width) {
			return 2;
		} else if (pt.y == cellSize.height) {
			return 3;
		}
		return 0;
	}

}
