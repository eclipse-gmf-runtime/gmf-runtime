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

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.GridLayerEx;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.PageBreakEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.PageBreaksFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.DiagramRuler;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.util.MeasurementUnitHelper;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeTypes;
import org.eclipse.gmf.runtime.gef.ui.internal.editparts.AnimatableZoomManager;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * RootEditPart which manages the Diagram's layers and creates the discrete zoom
 * levels for the <code>ZoomManager</code>.
 * 
 * @author jcorchis
 */
public class DiagramRootEditPart
	extends ScalableFreeformRootEditPart
	implements ZoomableEditPart, IDiagramPreferenceSupport {
	
	private WrapperMapMode mm;
	
	/**
	 * @author sshaw
	 * This pattern is necessary because, the constructor of the ScalableFreeformRootEditPart forces
	 * the scalable layered pane class to be instantiated where it gets initialized with the MapMode
	 * of the this root editpart.  However, we haven't had a chance to initialize the mapmode value yet since
	 * super must be called first.  So, this pattern allows us to set the mapmode into this container after
	 * super is called, but still have the scalable layered pane initialized with the mapmode value.
	 */
	private class WrapperMapMode implements IMapMode {

		public WrapperMapMode() {
			super();
		}

		IMapMode containedMM = MapModeTypes.DEFAULT_MM;
		public void setContainedMapMode(IMapMode mm) {
			this.containedMM = mm;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#DPtoLP(int)
		 */
		public int DPtoLP(int deviceUnit) {
			return containedMM.DPtoLP(deviceUnit);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#DPtoLP(org.eclipse.draw2d.geometry.Translatable)
		 */
		public Translatable DPtoLP(Translatable t) {
			return containedMM.DPtoLP(t);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#LPtoDP(int)
		 */
		public int LPtoDP(int logicalUnit) {
			return containedMM.LPtoDP(logicalUnit);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#LPtoDP(org.eclipse.draw2d.geometry.Translatable)
		 */
		public Translatable LPtoDP(Translatable t) {
			return containedMM.LPtoDP(t);
		}
		
	}
	
	/**
	 * Default constructor
	 */
	public DiagramRootEditPart() {
		super();
	}
	
	/**
	 * @param mu the <code>MeasurementUnit</code> that is used to display all contents
	 * within the root edit part.
	 */
	public DiagramRootEditPart(MeasurementUnit mu) {
		super();
		
		if (getMapMode() != null)
			mm.setContainedMapMode(MeasurementUnitHelper.getMapMode(mu));
	}

	/**
	 * GEF does not scale the FEEDBACK_LAYER but we do.
	 */
	class FeedbackLayer
		extends FreeformLayer
	{
		FeedbackLayer() {
			setEnabled(false);
		}
	}
    
	/**
	 * Listener for the workspace preference store.
	 */
	private class PreferenceStoreListener implements IPropertyChangeListener {
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			handlePreferenceStorePropertyChanged(event);
			
		}
	}
	
	static protected class DiagramScalableFreeformLayeredPane extends
		org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane implements ZoomListener {
	
		public DiagramScalableFreeformLayeredPane(IMapMode mm) {
			super(mm);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
		 */
		public void zoomChanged(double zoom) {
			ScaledGraphics.resetFontCache();
		}
	}

	private DiagramRuler verticalRuler, horizontalRuler;
	private AnimatableZoomManager zoomManager;
	private double[] zoomLevels = {.05, .1, .25, .5, .75, 1, 1.25, 1.5, 1.75, 2, 4};
	private PageBreakEditPart pageBreakEditPart;
	private PreferenceStoreListener listener = new PreferenceStoreListener();
		
	/* Keep layers to enable anti-aliasing */
	private ScalableFreeformLayeredPane layers;

	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connections, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private PreferencesHint preferencesHint = PreferencesHint.USE_DEFAULTS;
	private int printableLayerIndex;
	private GridLayer gridLayer;
	
	/**
	 * Initializes the preferenceStore property change
	 * listener.
	 */
	private void initPreferenceStoreListener() {
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getPreferencesHint().getPreferenceStore();
		preferenceStore.addPropertyChangeListener(listener);
	}
	
	/**
	 * This method removes all listeners to the notational world (views, figures, editpart...etc)
	 * Override this method to remove notational listeners down the hierarchy
	 */
	private void removePreferenceStoreListener() {
		//		remove preferenceStore listener
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getPreferencesHint().getPreferenceStore();
		preferenceStore.removePropertyChangeListener(listener);
		listener = null;
	}
	
    /**
     * Identifies the layer containing the page breaks figure.
     */
    final public static String PAGE_BREAKS_LAYER = "Page Breaks Layer"; //$NON-NLS-1$   
 
	/**
	 * Identifies the layers containing printable decoration layer.
	 */
	final public static String DECORATION_PRINTABLE_LAYER = "Decoration Printable Layer"; //$NON-NLS-1$   
	/**
	 * Identifies the layers containing Unprintable decoration layer.
	 */
	final public static String DECORATION_UNPRINTABLE_LAYER = "Decoration Unprintable Layer"; //$NON-NLS-1$   
   
    /* (non-Javadoc)
     * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#createPrintableLayers()
     */
    protected LayeredPane createPrintableLayers() {
    	FreeformLayeredPane layeredPane = new FreeformLayeredPane();
              
    	layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
    	layeredPane.add(new ConnectionLayerEx(), CONNECTION_LAYER);
		layeredPane.add(new FreeformLayer(), DECORATION_PRINTABLE_LAYER);

        return layeredPane;        
    }

    protected void moveGridLayer(boolean inFront) {
    	if (layers.getChildren().indexOf(gridLayer) > printableLayerIndex && (! inFront)) {    	
    		layers.remove(gridLayer);
    		layers.add(gridLayer,GRID_LAYER, printableLayerIndex);
    	} else if (layers.getChildren().indexOf(gridLayer) <= printableLayerIndex && inFront) {
    		layers.remove(gridLayer);
    		layers.add(gridLayer,GRID_LAYER, printableLayerIndex+1);
    	}
    }
    
	/**
    * Creates and returns the scalable layers of this EditPart
    * 
    * @return ScalableFreeformLayeredPane Pane that contains the scalable layers
    */
    protected ScalableFreeformLayeredPane createScaledLayers() {
    	
    	layers = createScalableFreeformLayeredPane();

        layers.add(new FreeformLayer(), PAGE_BREAKS_LAYER);
        printableLayerIndex = layers.getChildren().size();
        layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
                
        gridLayer = createGridLayer();
        
        layers.add(gridLayer, GRID_LAYER);
        
        layers.add(new FreeformLayer(), DECORATION_UNPRINTABLE_LAYER);
        return layers;
    }
   
    /**
     * Creates the <code>ScalableFreeformLayeredPane</code>.
	 * @return the new <code>ScalableFreeformLayeredPane</code>
	 */
	protected ScalableFreeformLayeredPane createScalableFreeformLayeredPane() {
		return new DiagramScalableFreeformLayeredPane(getMapMode());
	}

	/**
     * Override to set a non-default zoom levels
     * @return the ZoomManager with the non-default zoom range
     */
    public ZoomManager getZoomManager() {
		if (zoomManager == null) {
			zoomManager = new AnimatableZoomManager((ScalableFigure)getScaledLayers(),
											((Viewport)getFigure()));		
			zoomManager.setZoomLevels(zoomLevels);	
			refreshEnableZoomAnimation(zoomManager);
		}
		
		return zoomManager;
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart#zoomTo(double, org.eclipse.draw2d.geometry.Point)
	 */
	public void zoomTo(double zoom, Point center) {
		zoomManager.zoomTo(zoom, center);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart#zoomTo(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void zoomTo(Rectangle rect) {
		zoomManager.zoomTo(rect);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart#zoomIn()
	 */
	public void zoomIn() {
		zoomManager.zoomIn();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart#zoomIn(org.eclipse.draw2d.geometry.Point)
	 */
	public void zoomIn(Point center) {
		zoomManager.zoomTo(zoomManager.getNextZoomLevel(), center);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart#zoomOut()
	 */
	public void zoomOut() {
		zoomManager.zoomOut();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart#zoomOut(org.eclipse.draw2d.geometry.Point)
	 */
	public void zoomOut(Point center) {
		zoomManager.zoomTo(zoomManager.getPreviousZoomLevel(), center);
	}
	
    /**
     * Convience method to access the workspace viewer preferences.
     * @return PreferenceStore the workspace viewer preference store
     */
    protected IPreferenceStore getWorkspaceViewerPreferences() {
		if (getViewer() == null) return null;
    	return ((DiagramGraphicalViewer) getViewer())
			.getWorkspaceViewerPreferenceStore();
	}


	/**
	 * Get the Grid Spacing from the Plug-ins preference store
	 * 
	 * @return grid spacing value.
	 */
	public double getGridSpacing() {
		
		double gridSpacing = 0;
		// Check the workspace properties
		if (getWorkspaceViewerPreferences() != null)
			gridSpacing = getWorkspaceViewerPreferences().getDouble(WorkspaceViewerProperties.GRIDSPACING);
		
		// If the workspace property is not set then get the global preference value
		if (gridSpacing == 0) {
			IPreferenceStore pluginStore = (IPreferenceStore) getPreferencesHint().getPreferenceStore();
			gridSpacing = pluginStore.getDouble(IPreferenceConstants.PREF_GRID_SPACING);
		}
		return gridSpacing;
	}

	/**
	 * Sets the grid line style.  
	 * @param color 
	 * 
	 * @param style
	 */
	public void setGridStyle(int style) {
		if (gridLayer instanceof GridLayerEx) {
			((GridLayerEx) gridLayer).setLineStyle(style);			
		}
		gridLayer.repaint();
	}

	
	/**
	 * Sets the grid line color.  
	 * @param color 
	 * 
	 * @param gridSpacing
	 */
	public void setGridColor(Integer rgbValue) {
		gridLayer.setForegroundColor(FigureUtilities.integerToColor(rgbValue));
	}

	/**
	 * This method is called to set the grid spacing.  The units used
	 * for grid spacing are the same as the rulers current units.
	 * 
	 * @param gridSpacing
	 */
	public void setGridSpacing(double gridSpacing) {
		
		int rulerUnits = RulerProvider.UNIT_INCHES;
		if (getWorkspaceViewerPreferences() != null)
			rulerUnits = getWorkspaceViewerPreferences().getInt(WorkspaceViewerProperties.RULERUNIT);
		
		// Get the Displays DPIs
		double dotsPerInch = Display.getDefault().getDPI().x;
		int spacingInPixels = 0;

		// Evaluate the Grid Spacing based on the ruler units
		switch( rulerUnits) {
			case RulerProvider.UNIT_INCHES:
				spacingInPixels = (int)Math.round(dotsPerInch * gridSpacing);
				break;

			case RulerProvider.UNIT_CENTIMETERS:
				spacingInPixels = (int)Math.round( dotsPerInch * gridSpacing / 2.54 );
				break;

			default:
				spacingInPixels = (int)gridSpacing;
		}

		int spacing = getMapMode().DPtoLP(spacingInPixels);
		getViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING,
			new Dimension(spacing, spacing));
	}


	/**
	 * Returns the PageBreakEditPart controlled by this RootEditPart.
	 * @return the <code>PageBreakEditPart</code>
	 */
	public PageBreakEditPart getPageBreakEditPart() {
		if (pageBreakEditPart == null) {
			pageBreakEditPart = new PageBreakEditPart();
			pageBreakEditPart.setParent(this);
		}
		return pageBreakEditPart;
	}	
	
	/**
	 * Refreshes the page breaks.
	 */
	protected void refreshPageBreaks() {
		if (getWorkspaceViewerPreferences().getBoolean(WorkspaceViewerProperties.VIEWPAGEBREAKS))
			showPageBreaks();
		else
			hidePageBreaks();
	}
	
	/**
	 * Adds the pagebreaks figure to the <code>PAGE_BREAKS_LAYER</code>
	 */
	private void showPageBreaks() {
		getLayer(PAGE_BREAKS_LAYER).add(getPageBreakEditPart().getFigure());
		Point p =
			new Point(
				getWorkspaceViewerPreferences().getInt(
					WorkspaceViewerProperties.PAGEBREAK_X),
				getWorkspaceViewerPreferences().getInt(
					WorkspaceViewerProperties.PAGEBREAK_Y));
		getPageBreakEditPart().set(
			p,
			PageInfoHelper.getChildrenBounds(
				(DiagramEditPart) getContents(),
				PageBreaksFigure.class));		
	}	
	
	/**
	 * Removes the pagebreaks figure from the <code>PAGE_BREAKS_LAYER</code>
	 */
	private void hidePageBreaks() {
		if (getLayer(PAGE_BREAKS_LAYER)
			.getChildren()
			.contains(getPageBreakEditPart().getFigure())) {
			getLayer(PAGE_BREAKS_LAYER).remove(
				getPageBreakEditPart().getFigure());

			getPageBreakEditPart().updatePreferenceStore();
		}
	}	
	
	/**
	 * Handler for the workspace preference store.  Updates the page breaks if 
	 * WorksapceViewerProperties.VIEWPAGEBREAKS value is modified.
	 * @param event
	 */
	private void handlePreferenceStorePropertyChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
		if (WorkspaceViewerProperties.VIEWPAGEBREAKS.equals(event.getProperty())) {
			refreshPageBreaks();
		} else if (isPageSizeChange(event.getProperty())) {
			getPageBreakEditPart().calculatePageBreakFigureBounds(false);
			refreshPageBreaks();			
		} else if (WorkspaceViewerProperties.VIEWGRID.equals(event.getProperty())) {		
			// Set the state of the Grid Enabled Property
			getViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, event.getNewValue());
		} else if (WorkspaceViewerProperties.SNAPTOGRID.equals(event.getProperty())) {			
			// Set the state of the Snap to Grid Property
			getViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, event.getNewValue());
		} else if (WorkspaceViewerProperties.GRIDORDER.equals(event.getProperty())) {
			// Set the grid level
			moveGridLayer(((Boolean) event.getNewValue()).booleanValue());
		} else if (WorkspaceViewerProperties.GRIDSPACING.equals(event.getProperty())) {
			// Set the grid spacing			
			Double spacing = (Double) event.getNewValue();
			setGridSpacing(spacing.doubleValue());
		} else if (WorkspaceViewerProperties.VIEWRULERS.equals(event.getProperty())) {			
			// Set the state of the Ruler Enabled Property
			getViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY,
				event.getNewValue()); 
		} else if (WorkspaceViewerProperties.RULERUNIT.equals(event.getProperty())) { 
			Object newValue = event.getNewValue();
			int rulerUnits;
			
			if (newValue.getClass() == Integer.class) {
				rulerUnits = ((Integer) newValue).intValue();
				setRulers(rulerUnits);
			} else if (newValue.getClass() == String.class) {
				try {
					rulerUnits = Integer.parseInt((String) newValue);
					setRulers(rulerUnits);
				} catch (NumberFormatException e) {
					  Log.error( DiagramUIPlugin.getInstance(),
					  	DiagramUIStatusCodes.RESOURCE_FAILURE, e.toString() );
				}				
			} else {
				Log.error( DiagramUIPlugin.getInstance(),
				  	DiagramUIStatusCodes.RESOURCE_FAILURE,
					newValue.getClass().getName());
			}

			// Refresh the Rulers
			Boolean oldValue = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			getViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.FALSE);
			getViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, oldValue );
			
			// Update the Grids
			double spacing = getGridSpacing();
			setGridSpacing(spacing);			
			
		} else if (WorkspaceViewerProperties.GRIDLINECOLOR.equals(event.getProperty())) {
			Integer newValue = (Integer) event.getNewValue();
			// Set the grid line color
			setGridColor(newValue);
		}  else if (WorkspaceViewerProperties.GRIDLINESTYLE.equals(event.getProperty())) {
			Integer newValue = (Integer) event.getNewValue();
			// Set the grid line style
			setGridStyle(newValue.intValue());
		} else if (event.getProperty().equals(IPreferenceConstants.PREF_ENABLE_ANIMATED_ZOOM)){
			refreshEnableZoomAnimation(getZoomManager());
		} else if (event.getProperty().equals(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)){
			refreshEnableAntiAlias();
		}	
	}
	
	/**
	 * @param rulerUnits
	 */
	private void setRulers(int rulerUnits) {
		if( getVerticalRuler() != null ) {
			getVerticalRuler().setUnit( rulerUnits );
		} else {
			setVerticalRuler(new DiagramRuler(false, rulerUnits, null));
		}
		if( getHorizontalRuler() != null ) {
			getHorizontalRuler().setUnit( rulerUnits );
		} else {
			setHorizontalRuler(new DiagramRuler(true, rulerUnits, null));			
		}
	}

	/**
	 * 
	 */
	private void refreshEnableZoomAnimation(ZoomManager zoomMangr) {
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getPreferencesHint().getPreferenceStore();
		boolean animatedZoom = preferenceStore.getBoolean(
			IPreferenceConstants.PREF_ENABLE_ANIMATED_ZOOM);
		zoomMangr.setZoomAnimationStyle(animatedZoom ? ZoomManager.ANIMATE_ZOOM_IN_OUT : ZoomManager.ANIMATE_NEVER);
	}

	/**
	 * Refresh visuals in order to enable anti-aliasing
	 */
	public void refreshVisuals() {
        // Set the anti-aliasing
        refreshEnableAntiAlias();
	} 
	
	/**
	 * Refreshes anti-alias status on the diagram
	 */
	protected void refreshEnableAntiAlias() {
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getPreferencesHint().getPreferenceStore();
		boolean antiAlias = preferenceStore.getBoolean(
			IPreferenceConstants.PREF_ENABLE_ANTIALIAS);
		if (layers instanceof org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane)
			((org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane) layers).setAntiAlias(antiAlias);
	}

	/**
	 * Refreshes ruler units on the diagram
	 */
	protected void refreshRulerUnits() {
		if (getWorkspaceViewerPreferences() != null)
			setRulers(getWorkspaceViewerPreferences().getInt(WorkspaceViewerProperties.RULERUNIT));
	}
	
	/**
	 * This is a workspace property request.  It does not use a <code>Command</code>
	 * to execute since it does not change the model.
	 * @param request 
	 */
	public void performRequest(Request request) {
		if (request.getType().equals(RequestConstants.REQ_RECALCULATE_PAGEBREAKS)) {		
			getPageBreakEditPart().calculatePageBreakFigureBounds();	
		} 
	}
	
	/**
	 * Adds a listener to the workspace preference store for changes related to the
	 * page breaks and diagram grid.
	 */
	public void activate() {
		super.activate();	
		
		if (getWorkspaceViewerPreferences() != null)
			getWorkspaceViewerPreferences().addPropertyChangeListener(listener);	
		
		initPreferenceStoreListener();
		
		ScalableFreeformLayeredPane pane = getLayers();
		
		refreshEnableAntiAlias();
		
		initWorkspaceViewerProperties();
		
		refreshRulerUnits();
		
		if (pane instanceof ZoomListener) {
			getZoomManager().addZoomListener((ZoomListener)pane);
		}
	}
	
	private static final int LIGHT_GRAY_RGB = 12632256;
	
	/**
	 * Initializes the workspace viewer property that are stored per diagram
	 */
	private void initWorkspaceViewerProperties() {		
		IPreferenceStore wsPrefStore = getWorkspaceViewerPreferences();
		
		if (wsPrefStore != null) {
			if (! wsPrefStore.contains(WorkspaceViewerProperties.GRIDORDER)) {
				wsPrefStore.setValue(WorkspaceViewerProperties.GRIDORDER, true);			
			} else {
				moveGridLayer(wsPrefStore.getBoolean(WorkspaceViewerProperties.GRIDORDER));
			}
			if (! wsPrefStore.contains(WorkspaceViewerProperties.GRIDLINECOLOR)) {
				wsPrefStore.setValue(WorkspaceViewerProperties.GRIDLINECOLOR, LIGHT_GRAY_RGB);			
			} else {
				setGridColor(new Integer(wsPrefStore.getInt(WorkspaceViewerProperties.GRIDLINECOLOR)));
			}
			if (! wsPrefStore.contains(WorkspaceViewerProperties.GRIDLINESTYLE)) {
				wsPrefStore.setValue(WorkspaceViewerProperties.GRIDLINESTYLE, SWT.LINE_CUSTOM);			
			} else {
				setGridStyle(wsPrefStore.getInt(WorkspaceViewerProperties.GRIDLINESTYLE));
			}
			
			if ((! wsPrefStore.contains(WorkspaceViewerProperties.RULERUNIT)) || 
					(! wsPrefStore.contains(WorkspaceViewerProperties.GRIDSPACING))) {
				IPreferenceStore preferenceStore =
					(IPreferenceStore) getPreferencesHint().getPreferenceStore();			
				// Set the ruler unit to 999 in order to force the setting 
				// and then change it to the appropriate value
				wsPrefStore.setValue(WorkspaceViewerProperties.RULERUNIT, 999);						
				wsPrefStore.setValue(WorkspaceViewerProperties.RULERUNIT, preferenceStore.getInt(IPreferenceConstants.PREF_RULER_UNITS));						
				wsPrefStore.setValue(WorkspaceViewerProperties.GRIDSPACING, preferenceStore.getDouble(IPreferenceConstants.PREF_GRID_SPACING));			
			}
		}
	}

	/**
	 * 
	 */
	public void deactivate() {
		if (getPageBreakEditPart() != null)
			getPageBreakEditPart().updatePreferenceStore();
		
		if (getWorkspaceViewerPreferences() != null)
			getWorkspaceViewerPreferences().removePropertyChangeListener(listener);
		removePreferenceStoreListener();
		
		ScalableFreeformLayeredPane pane = getLayers();
		if (pane instanceof ZoomListener) {
			getZoomManager().removeZoomListener((ZoomListener)pane);
		}
		
		super.deactivate();
	}
	
	/**
	 * Method that returns <code>true</code> if the given parameter corresponds to a
	 * workspace viewer property that affects the size of the page breaks.
	 * @param s workspace viewer property
	 * @return <code>true</code> if the String corresponds to a <code>WorkspaceViewerProperties</code>
	 * that affects the size of the <code>PageBreaksFigure</code>
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties
	 */
	private boolean isPageSizeChange(String s) {
		if (WorkspaceViewerProperties.PREF_MARGIN_TOP.equals(s)
			|| WorkspaceViewerProperties.PREF_MARGIN_LEFT.equals(s)
			|| WorkspaceViewerProperties.PREF_MARGIN_RIGHT.equals(s)
			|| WorkspaceViewerProperties.PREF_MARGIN_BOTTOM.equals(s)
			|| WorkspaceViewerProperties.PREF_PAGE_SIZE.equals(s)
			|| WorkspaceViewerProperties.PREF_USE_LANDSCAPE.equals(s)
			|| WorkspaceViewerProperties.PREF_USE_PORTRAIT.equals(s)
			|| WorkspaceViewerProperties.PREF_PAGE_HEIGHT.equals(s)
			|| WorkspaceViewerProperties.PREF_PAGE_WIDTH.equals(s)
			|| WorkspaceViewerProperties.PREF_USE_DIAGRAM_SETTINGS.equals(s))
			return true;
		else
			return false;
	}

	
	/**
	 * 
	 * Accessor for scalable layers. Subclasses can access them in order 
	 * to update the layer when a relevant preference has been modified
	 * @return 
	 */
	protected ScalableFreeformLayeredPane getLayers() {
		return layers;
	}

	
	protected void setLayers(ScalableFreeformLayeredPane layers) {
		this.layers = layers;
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport#setPreferencesHint(org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint)
	 */
	public void setPreferencesHint(PreferencesHint preferenceHint) {
		this.preferencesHint = preferenceHint;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport#getPreferencesHint()
	 */
	public PreferencesHint getPreferencesHint() {
		return preferencesHint;
	}
	
	/**
	 * Clients must define the measurement unit in the <code>Diagram</code> notation
	 * object for their editor to affect this mapping mode object value.
	 * 
	 * @return <code>IMapMode</code> that is the coordinate mapping for the Editor from device to
	 * logical coordinates.
	 */
	final public IMapMode getMapMode() {
		if (mm == null)
			mm = new WrapperMapMode();
		return mm;
	}

	public DiagramRuler getHorizontalRuler() {
		return horizontalRuler;
}
	
	private void setHorizontalRuler(DiagramRuler horizontalRuler) {
		this.horizontalRuler = horizontalRuler;
	}

	
	public DiagramRuler getVerticalRuler() {
		return verticalRuler;
	}

	
	private void setVerticalRuler(DiagramRuler verticalRuler) {
		this.verticalRuler = verticalRuler;
	}

	protected GridLayer createGridLayer() {
		return new GridLayerEx();
	}

	protected GridLayer createGridLayer(int r, int g, int b) {
		return new GridLayerEx(new Color(null,r,g,b));
	}

}
