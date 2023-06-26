/******************************************************************************
 * Copyright (c) 2002, 2008, 2023 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Ansgar Radermacher - contribution for bug xxxx 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.ImageFileDropTargetListener;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.PaletteToolTransferDragSourceListener;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.PaletteToolTransferDropTargetListener;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteService;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;
import org.eclipse.gmf.runtime.gef.ui.palette.customize.PaletteCustomizerEx;
import org.eclipse.gmf.runtime.gef.ui.palette.customize.PaletteViewerEx;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.ActivityManagerEvent;
import org.eclipse.ui.activities.IActivityManagerListener;

/**
 * A generic diagram editor with a palette.  This supports the palette moved from
 * the diagram to a palette view.  If the palette view is open than the
 * palette in the diagram will be removed.
 * @author choang, cmahoney
 */
public abstract class DiagramEditorWithFlyOutPalette
	extends DiagramEditor
{
	/**
	 * Listens for activity/capability events.
	 * 
	 * @author cmahoney
	 */
	class ActivityManagerListener
		implements IActivityManagerListener {

		public void activityManagerChanged(
				ActivityManagerEvent activityManagerEvent) {
			if (activityManagerEvent.haveEnabledActivityIdsChanged()) {
				if (getEditDomain() != null
					&& getEditDomain().getPaletteViewer() != null
					&& getEditDomain().getPaletteViewer().getPaletteRoot() != null) {

					createPaletteRoot(getEditDomain().getPaletteViewer()
						.getPaletteRoot());
				}
			}
		}
	}
	
	/**
	 * The activity listener.
	 */
	private IActivityManagerListener activityManagerListener;

	boolean fHasFlyoutPalette = true;
	public DiagramEditorWithFlyOutPalette() {
		// empty
	}
	
	public DiagramEditorWithFlyOutPalette(boolean hasFlyout) {
		fHasFlyoutPalette = hasFlyout;
	}

	/**
	 * uncollapsed pinned palette state constant
	 */
	protected static final int UNCOLLAPSED_PINNED = 4;
	/**
	 * collapsed pinned palette state constant
	 */
	protected static final int COLLAPSED = 2;

	// Provider that creates palette viewer

	private PaletteViewerProvider provider;
	// Splitter the supports the moving of the palette in and out of the diagram
	// from a palette view.
	private FlyoutPaletteComposite splitter;
	// Page that supports the slurping in and out of the palette from the diagram to
	// and from a palette view.  The palette view is like any other view it can be moved
	// around.  Similiar in docking functionality as the outline view.
	private CustomPalettePage page;

	protected void initializeGraphicalViewer() {
		if(fHasFlyoutPalette) {
			splitter.hookDropTargetListener(getGraphicalViewer());
			
			super.initializeGraphicalViewer();

			/* Add a drop target listener for ME drop events */
			getDiagramGraphicalViewer().addDropTargetListener(
				(TransferDropTargetListener) new ImageFileDropTargetListener(
					getDiagramGraphicalViewer()));

			// Add a transfer drag target listener that is supported on
			// palette template entries whose template is a creation tool.
			// This will enable drag and drop of the palette shape creation
			// tools.
			getDiagramGraphicalViewer()
				.addDropTargetListener(
					new PaletteToolTransferDropTargetListener(
						getGraphicalViewer()));

		} else {
			super.initializeGraphicalViewer();
		}
	}
	
	protected boolean toolSupportsAccessibility(Tool t) {
		return (t instanceof CreationTool) || 
				(t instanceof ConnectionCreationTool);
	}
	
	protected PaletteViewer constructPaletteViewer() {
		return new PaletteViewerEx();
	}

	/**
	 * Creates a PaletteViewerProvider.
	 * @return PaletteViewerProvider that provides for the palette viewer for the diagram
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		assert fHasFlyoutPalette == true;
		getEditDomain().setPaletteRoot(createPaletteRoot(null));
		return new PaletteViewerProvider(getEditDomain()){

			public PaletteViewer createPaletteViewer(Composite parent) {
				PaletteViewer pViewer = constructPaletteViewer();
				pViewer.createControl(parent);
				configurePaletteViewer(pViewer);
				hookPaletteViewer(pViewer);
				return pViewer;
			}
			
			/**
			 * Override to provide the additional behavior for the tools.
			 * Will intialize with a PaletteEditPartFactory that has a TrackDragger that
			 * understand how to handle the mouseDoubleClick event for shape creation tools.
			 * Also will initialize the palette with a defaultTool that is the SelectToolEx that undestands
			 * how to handle the enter key which will result in the creation of the shape also.
			 */
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);

				viewer.getKeyHandler().setParent(getPaletteKeyHandler());
				viewer.getControl().addMouseListener(getPaletteMouseListener());

				// Add a transfer drag target listener that is supported on
				// palette template entries whose template is a creation tool.
				// This will enable drag and drop of the palette shape creation
				// tools.
				viewer
					.addDragSourceListener(new PaletteToolTransferDragSourceListener(
						viewer));
                viewer.setCustomizer(createPaletteCustomizer());
			}


			/**
			 * @return Palette Key Handler for the palette
			 */
			private KeyHandler getPaletteKeyHandler() {

				if (paletteKeyHandler == null) {

					paletteKeyHandler = new KeyHandler() {

						/**
						 * Processes a <i>key released </i> event. This method
						 * is called by the Tool whenever a key is released, and
						 * the Tool is in the proper state. Override to support
						 * pressing the enter key to create a shape or connection
						 * (between two selected shapes)
						 *
						 * @param event
						 *            the KeyEvent
						 * @return <code>true</code> if KeyEvent was handled
						 *         in some way
						 */
						public boolean keyReleased(KeyEvent event) {

							if (event.keyCode == SWT.Selection) {

								Tool tool =
									getPaletteViewer()
										.getActiveTool()
										.createTool();

								if (toolSupportsAccessibility(tool)) {

									tool.keyUp(event, getDiagramGraphicalViewer());

									// 	deactivate current selection
									getPaletteViewer().setActiveTool(null);

									return true;
								}

							}
							return super.keyReleased(event);
						}

					};

				}
				return paletteKeyHandler;
			}

			/**
			 * @return Palette Mouse listener for the palette
			 */
			private MouseListener getPaletteMouseListener() {

				if (paletteMouseListener == null) {

					paletteMouseListener = new MouseListener() {

						/**
						 * Flag to indicate that the current active tool should
						 * be cleared after a mouse double-click event.
						 */
						private boolean clearActiveTool = false;

						/**
						 * Override to support double-clicking a palette tool
						 * entry to create a shape or connection (between two
						 * selected shapes).
						 *
						 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
						 */
						public void mouseDoubleClick(MouseEvent e) {
							Tool tool = getPaletteViewer().getActiveTool()
								.createTool();

							if (toolSupportsAccessibility(tool)) {

								tool.setViewer(getDiagramGraphicalViewer());
								tool.setEditDomain(getDiagramGraphicalViewer()
									.getEditDomain());
								tool.mouseDoubleClick(e,
									getDiagramGraphicalViewer());

								// Current active tool should be deactivated,
								// but if it is down here it will get
								// reactivated deep in GEF palette code after
								// receiving mouse up events.
								clearActiveTool = true;
							}
						}

						public void mouseDown(MouseEvent e) {
							// do nothing
						}

						public void mouseUp(MouseEvent e) {
							// 	Deactivate current active tool here if a
							// double-click was handled.
							if (clearActiveTool) {
								getPaletteViewer().setActiveTool(null);
								clearActiveTool = false;
							}

						}
					};

				}
				return paletteMouseListener;
			}

		};
	}

	/** Key Handler for the palette */
	KeyHandler paletteKeyHandler = null;

	/** Mouse listener for the palette */
	MouseListener paletteMouseListener = null;

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (getGraphicalControl()!=null)
			getGraphicalControl().setFocus();
	}


	/**
	 * Creates a splitter composite that will contain
	 * 2 parts one is for the diagram and the other is for the palette.
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {

		if(fHasFlyoutPalette) {
			FlyoutPaletteComposite.FlyoutPreferences flyoutPrefs = new FlyoutPreferencesImpl(
                getPreferenceStore());

			splitter = new FlyoutPaletteComposite(parent, SWT.NONE, getSite().getPage(), getPaletteViewerProvider(), flyoutPrefs);
			super.createPartControl(splitter);
			splitter.setGraphicalControl(getGraphicalControl());
			if (page != null) {
				splitter.setExternalViewer(getPaletteViewer());
				page = null;
			}

			// CWD: FlyoutPaletteComposite no longer implements the Java Beans
			//      property change API, nor does it even have a 'default state'
			//      property in the IES M9 drop
			//TODO(M9):  Review whether this listener is needed
//			splitter.addPropertyChangeListener(new PropertyChangeListener() {
//			public void propertyChange(PropertyChangeEvent evt) {
//				if (evt.getPropertyName().equals(FlyoutPaletteCompositeEx.PROPERTY_DEFAULT_STATE))
//					handlePaletteDefaultStateChanged(((Integer)evt.getNewValue()).intValue());
//				else
//					handlePaletteResized(((Integer)evt.getNewValue()).intValue());
//				}
//			});
		} else {
			super.createPartControl(parent);
		}

	}
	
	/**
     * Returns a preference store where GMF global workspace preferences can be
     * saved. For example, the palette customizations are saved in this
     * preference store. These settings are workspace-wide and not specific to
     * any particular diagram.  Override to customize.
     * 
     * @return the preference store
     * @since 2.1
     */
    protected IPreferenceStore getPreferenceStore() {
        return DiagramUIPlugin.getInstance().getPreferenceStore();
    }
	
	/**
	 * Adapts to
	 * <LI> PalettePage.class
	 * <LI> PaletteViewer.class
	 * other wise delegates to super.getAdapter()
	 */
	public Object getAdapter(Class type) {

		if(fHasFlyoutPalette) {
			if (type == PalettePage.class) {
				if (splitter == null) {
					page = new CustomPalettePage(getPaletteViewerProvider());
					return page;
				}
				return new CustomPalettePage(getPaletteViewerProvider());
			}
			// Please do not remove PaletteViewer adapter
			// QE automation depends on it
			if (type == PaletteViewer.class){
	
				return getPaletteViewer();
			}
		}
		return super.getAdapter(type);
	}

	/**
	 * Creates the palette root for the palette viewer or updates the existing
	 * palette root passed in if entries should be added/removed based on the
	 * current state.
	 * 
	 * @param existingPaletteRoot
	 *            the existing palette root if the palette has already been
	 *            created, or null if the palette root has not yet been created
	 * @return the new palette root or the updated palette root
	 */
	protected PaletteRoot createPaletteRoot(PaletteRoot existingPaletteRoot) {
	    PaletteRoot paletteRoot;
		if (existingPaletteRoot == null) {
			paletteRoot = PaletteService.getInstance().createPalette(this,
				getDefaultPaletteContent());
		} else {
			PaletteService.getInstance().updatePalette(existingPaletteRoot,
				this, getDefaultPaletteContent());
			paletteRoot = existingPaletteRoot;
		}
        applyCustomizationsToPalette(paletteRoot);
        return paletteRoot;
	}

	/**
	 * Gets the palette content to be sent to the palette service when creating
	 * the palette.
	 * 
	 * @return the palette content
	 */
	protected abstract Object getDefaultPaletteContent();

	/**
	 * Returns the palette view provider that is reponsible for creating and
	 * palette view.
	 * 
	 * @return PaletteViewerProvider
	 */
	protected final PaletteViewerProvider getPaletteViewerProvider() {
		if (provider == null)
			provider = createPaletteViewerProvider();
		return provider;
	}

	/**
	 * Returns the initial palette size in pixels. Subclasses may override this method to
	 * return a persisted value.
	 * @see #handlePaletteResized(int)
	 * @return the initial size of the palette in pixels.
	 */
	protected int getInitialPaletteSize() {
		// '125' is the value of the FlyoutPaletteComposite.DEFAULT_PALETTE_SIZE constant
		//   which was formerly public but is now private
		return 125;
	}

	/**
	 * gets the initial palette state, it could be 
	 * UNCOLLAPSED_PINNED or COLLAPSED
	 * @return int
	 */
	protected int getInitialPaletteState() {
		// '4' is the value of the FlyoutPaletteComposite.STATE_UNCOLLAPSED constant
		//   which is private.
		return UNCOLLAPSED_PINNED;
	}
	
    /**
     * Returns the initial docking location of the palette either
     * {@link PositionConstants#EAST} or {@link PositionConstants#WEST}. The
     * 
     * @return the initial dock location either {@link PositionConstants#EAST}
     *         or {@link PositionConstants#WEST}
     */
    protected int getInitialDockLocation() {
        return PositionConstants.EAST;
    }

	protected void setEditDomain(DefaultEditDomain ed) {
		super.setEditDomain(ed);

	}

	/**
	 * Called to configure the viewer before it receives its contents.
	 */
	protected void configurePaletteViewer() {
		assert fHasFlyoutPalette == true;
		PaletteViewer viewer = getPaletteViewer();

		if (viewer == null) return;

		ContextMenuProvider paletteContextProvider = new PaletteContextMenuProvider(viewer);
		getPaletteViewer().setContextMenu(paletteContextProvider);
	}

	/**
	 * Helper method to returns the PaletteViewer from the
	 * page.
	 * @return the palette viewer
	 */
	private PaletteViewer getPaletteViewer() {

		return getEditDomain().getPaletteViewer();

	}

	/**
	 * CustomPalettePage that helps with the switching of the palette
	 * from the diagram to the palette view.
	 * @author choang
	 */
	protected class CustomPalettePage extends PaletteViewerPage {
		/**
		 * constructor
		 * @param provider
		 */
		public CustomPalettePage(PaletteViewerProvider provider) {
			super(provider);
		}
		public void createControl(Composite parent) {
			super.createControl(parent);
			if (splitter != null)
				splitter.setExternalViewer(viewer);
		}
		public void dispose() {
			if (splitter != null)
				splitter.setExternalViewer(null);
			super.dispose();
		}
		/**
		 * gets the palette viewer
		 * @return <code>PaletteViewer</code>
		 */
		public PaletteViewer getPaletteViewer() {
			return viewer;
		}
	}

	/**
	 * Called whenever the user resizes the palette.  Sub-classes can store the new palette
	 * size.  May want to use this to store preferences as a perferences.
	 * @param newSize the new size in pixels
	 */
	protected void handlePaletteResized(int newSize){
		//
	}
	/**
	 * Called whenever the user updates the default palette state.  Sub-classes can store
	 * the new palette state.  May want to use this to store preferences as a perferences.
	 * @param newState the new state
	 */
	protected void handlePaletteDefaultStateChanged(int newState){
		//
	}

	/**
     * An implementation of the fly-out palette preferences that stores the
     * flyout palette settings in the given Preferences.
     */
	private final class FlyoutPreferencesImpl
			implements FlyoutPaletteComposite.FlyoutPreferences {

        private static final String PALETTE_DOCK_LOCATION = "org.eclipse.gmf.pdock"; //$NON-NLS-1$
        private static final String PALETTE_SIZE = "org.eclipse.gmf.psize"; //$NON-NLS-1$
        private static final String PALETTE_STATE = "org.eclipse.gmf.pstate"; //$NON-NLS-1$

        private IPreferenceStore preferences;

        private FlyoutPreferencesImpl(IPreferenceStore preferenceStore) {
            preferences = preferenceStore;
        }
        
        public int getDockLocation() {
            if (preferences.contains(PALETTE_DOCK_LOCATION)) {
                return preferences.getInt(PALETTE_DOCK_LOCATION);
            } else {
                return getInitialDockLocation();
            }
        }

        public int getPaletteState() {
            if (preferences.contains(PALETTE_STATE)) {
                return preferences.getInt(PALETTE_STATE);
            } else {
                return getInitialPaletteState();
            }
        }

        public int getPaletteWidth() {
            if (preferences.contains(PALETTE_SIZE)) {
                return preferences.getInt(PALETTE_SIZE);
            } else {
                return getInitialPaletteSize();
            }
        }
        
        public void setDockLocation(int location) {
            preferences.setValue(PALETTE_DOCK_LOCATION, location);
        }
        
        public void setPaletteState(int state) {
            preferences.setValue(PALETTE_STATE, state);
        }
        
        public void setPaletteWidth(int width) {
            preferences.setValue(PALETTE_SIZE, width);
        }   
	}

	protected void startListening() {
		super.startListening();
		activityManagerListener = createActivityManagerListener();
		if (activityManagerListener != null) {
			PlatformUI.getWorkbench().getActivitySupport().getActivityManager()
				.addActivityManagerListener(activityManagerListener);
		}
	}

	protected void stopListening() {
		if (activityManagerListener != null) {
			PlatformUI.getWorkbench().getActivitySupport().getActivityManager()
				.removeActivityManagerListener(activityManagerListener);
			activityManagerListener = null;
		}
		super.stopListening();
	}
	
    protected IActivityManagerListener createActivityManagerListener() {
        return new ActivityManagerListener();
    }

    /**
     * Applies any current customizations to the palette root. If the palette
     * viewer is not set or it does not have its palette root set, this method
     * will do nothing.
     */
    protected void applyCustomizationsToPalette(PaletteRoot paletteRoot) {
        PaletteCustomizer customizer = (getPaletteViewer() != null) ? getPaletteViewer()
            .getCustomizer()
            : createPaletteCustomizer();
        if (customizer instanceof PaletteCustomizerEx) {
            ((PaletteCustomizerEx) customizer)
                .applyCustomizationsToPalette(paletteRoot);
        }
    }

    /**
     * Creation factory method for the palette customizer.
     * @return a new palette customizer
     */
    protected PaletteCustomizer createPaletteCustomizer() {
        return new PaletteCustomizerEx(getPreferenceStore());
    }

    /**
     * Dispose splitter
     */
    @Override
	public void dispose() {
		if (splitter != null) {
			splitter.setExternalViewer(null);
			splitter.dispose();
			splitter = null;
		}
		if (page != null) {
			page.dispose();
			page = null;
		}
		super.dispose();
	}
}
