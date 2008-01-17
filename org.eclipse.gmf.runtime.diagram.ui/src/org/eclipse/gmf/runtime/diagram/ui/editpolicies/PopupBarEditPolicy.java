/******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.AbstractPopupBarTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.PopupBarTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Popup bars are esentially a cartoon balloon with buttons that are activated
 * during mouse hover over a shape.
 * 
 * @author affrantz@us.ibm.com, cmahoney
 */
public class PopupBarEditPolicy extends DiagramAssistantEditPolicy {

	/* ************************** nested classes *********************** */
	/**
	 * 
	 * Class to hold pertinent information about the tool placed on the popup bar
	 * 
	 * @author affrantz@us.ibm.com
	 */
	private class PopupBarDescriptor {

		/** The action button tooltip */
		private String _tooltip = new String();

		/** The image for the button */
		private Image _icon = null;

		/** The typeinfo used to create the Request for the command */
		private IElementType _elementType;

		/** The DracgTracker / Tool associatd with the popup bar button */
		private DragTracker _dragTracker = null;

		/**
		 * constructor
		 * @param s
		 * @param i
		 * @param elementType
		 * @param theTracker
		 */
		public PopupBarDescriptor(
				String s,
				Image i,
				IElementType elementType,
				DragTracker theTracker) {
			_tooltip = s;
			_icon = i;
			_dragTracker = theTracker;
			_elementType = elementType;

		}

		/**
		 * gets the element type associated with this Descriptor
		 * @return element type
		 */
		public final IElementType getElementtype() {
			return _elementType;
		}

		/**
		 * gets the icon associated with this Descriptor
		 * @return Image
		 */
		public final Image getIcon() {
			return _icon;
		}

		/**
		 * gets the drag tracker associated with this Descriptor
		 * @return drag tracker
		 */
		public final DragTracker getDragTracker() {
			return _dragTracker;
		}

		/**
		 * gets the tool tip associated with this Descriptor
		 * @return string
		 */
		public final String getToolTip() {
			return _tooltip;
		}

	} // end PopupBarDescriptor

	/**
	 * Default tool placed on the popup bar
	 * 
	 * @author affrantz@us.ibm.com
	 */
	private class PopupBarLabelHandle extends Label implements Handle {
		/**
		 * flag to drawFocus rect around the handle when the mouse rolls over
		 * it
		 */
		private boolean myMouseOver = false;

		private Image myDisabledImage = null;

		/** The dragTracker CreationTool associated with the handle * */
		private DragTracker myDragTracker = null;

		private Image getDisabledImage()
		{
			if (myDisabledImage != null)
				return myDisabledImage;

			Image theImage = this.getIcon();
			if (theImage == null)
				return null;

			myDisabledImage = new Image(Display.getCurrent(), theImage, SWT.IMAGE_DISABLE);
			imagesToBeDisposed.add(myDisabledImage);
			return myDisabledImage;
		}

		/**
		 * cnostructor
		 * @param tracker
		 * @param theImage
		 */
		public PopupBarLabelHandle(DragTracker tracker, Image theImage) {
			super(theImage);
			myDragTracker = tracker;
			this.setOpaque(true);
			this.setBackgroundColor(ColorConstants.buttonLightest);
            calculateEnabled();
		}

		/**
		 * @see org.eclipse.gef.Handle#getAccessibleLocation()
		 */
		public Point getAccessibleLocation() {
			return null;
		}

		/**
		 * @see org.eclipse.gef.Handle#getDragTracker()
		 */
		public DragTracker getDragTracker() {
			return myDragTracker;
		}

		/**
		 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
		 *      paint a focus rectangle for the label if the mouse is inside
		 *      the label
		 */
		protected void paintBorder(Graphics graphics) {
			super.paintBorder(graphics);

			if (myMouseOver) {

				Rectangle area = getClientArea();
				graphics.setForegroundColor(ColorConstants.black);
				graphics.setBackgroundColor(ColorConstants.white);

				graphics.drawFocus(
					area.x,
					area.y,
					area.width - 1,
					area.height - 1);

			}

		}

		/**
		 * @see org.eclipse.draw2d.IFigure#handleMouseEntered(org.eclipse.draw2d.MouseEvent)
		 *      flip myMouseOver bit and repaint
		 */
		public void handleMouseEntered(MouseEvent event) {
            
            calculateEnabled();

			super.handleMouseEntered(event);
			myMouseOver = true;
			repaint();
		}

		/**
		 * @see org.eclipse.draw2d.IFigure#handleMouseExited(org.eclipse.draw2d.MouseEvent)
		 *      flip myMouseOver bit and repaint
		 */
		public void handleMouseExited(MouseEvent event) {

			super.handleMouseExited(event);
			myMouseOver = false;
			repaint();
		}

		/**
		 * @see org.eclipse.draw2d.IFigure#handleMousePressed(org.eclipse.draw2d.MouseEvent)
		 *      set PopupBarEditPolicy.myActionMoveFigure bit so the popup bar
		 *      is not dismissed after creating an item in the editpart
		 * 
		 */
		public void handleMousePressed(MouseEvent event) {

			if (1 == event.button) 
			{
				// this is the flag in PopupBarEditPolicy that
				// prevents the popup bar from dismissing after a new item
				// is added to a shape, which causes the editpart to be
				// resized.
				setFlag(POPUPBAR_MOVE_FIGURE, true);
				// future: when other tools besides PopupBarTool are
				// used
				// we will need a way in which to call

			}

			super.handleMousePressed(event);
		}

        private void calculateEnabled() {
            if((myDragTracker != null) && (myDragTracker instanceof AbstractPopupBarTool))
            {
                AbstractPopupBarTool abarTool = (AbstractPopupBarTool) myDragTracker;
                setEnabled(abarTool.isCommandEnabled());
            } else {
                setEnabled(true);
            }
        }

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			if(!isEnabled())
			{
				Image theImage = getDisabledImage();
				if (theImage != null)
				{
					graphics.translate(bounds.x, bounds.y);
					graphics.drawImage(theImage, getIconLocation());
					graphics.translate(-bounds.x, -bounds.y);
					return;
				}

			}
			super.paintFigure(graphics);

		}
	}

	private static Image IMAGE_POPUPBAR_PLUS = DiagramUIPluginImages
		.get(DiagramUIPluginImages.IMG_POPUPBAR_PLUS);

	private static Image IMAGE_POPUPBAR = DiagramUIPluginImages
		.get(DiagramUIPluginImages.IMG_POPUPBAR);
	
	/**
	 * 
	 * This is the figure that represents the ballon portion of the popup bar
	 * 
	 * @author affrantz@us.ibm.com
	 */
	private class RoundedRectangleWithTail extends RoundedRectangle {

		private Image myTailImage = null;

		private boolean bIsInit = false;

		private int myCornerDimension = 6;

		/**
		 * constructor
		 */
		public RoundedRectangleWithTail() {
			// we do not make the myActionTailFigue opaque because it
			// doesn't look good when magnification is set.
			this.setFill(true);
			this.setBackgroundColor(ColorConstants.buttonLightest);
			this.setForegroundColor(ColorConstants.lightGray);
			this.setVisible(true);
			this.setEnabled(true);
			this.setOpaque(true);

		}

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		public void paintFigure(Graphics graphics) {
			int shiftWidth = 3;
			Image theTail = getTail();
			Rectangle theBounds = this.getBounds().getCopy();
			theBounds.height -= theTail.getBounds().height;
			theBounds.height -= shiftWidth;// shift slight above cursor
			theBounds.x += shiftWidth; // shift slight to right of cursor
			theBounds.width -= (shiftWidth + 1); // otherwise rhs is clipped

			// fill the round rectangle first since it is opaque
			graphics.fillRoundRectangle(
				theBounds,
				myCornerDimension,
				myCornerDimension);
			graphics.drawRoundRectangle(
				theBounds,
				myCornerDimension,
				myCornerDimension);

			graphics.drawImage(
				theTail,
				theBounds.x + 6,
				theBounds.y + theBounds.height - 1);

		}
		private Image getTail()
		{
			if(!bIsInit)
			{
				if(getIsDisplayAtMouseHoverLocation() && !isHostConnection())
				{
					if(myTailImage == null)
					{
						myTailImage = IMAGE_POPUPBAR_PLUS;
						bIsInit = true;
					}
				}
				else
				{
					if(myTailImage == null)
					{
						myTailImage = IMAGE_POPUPBAR;
						bIsInit = true;
					}
				}

			}
			return myTailImage;

		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#isDiagramAssistant(java.lang.Object)
	 */
	protected boolean isDiagramAssistant(Object object) {
		return object instanceof RoundedRectangleWithTail
			|| object instanceof PopupBarLabelHandle;
	}

	/**
	 * Adds the popup bar after a delay
	 */
	public void mouseHover(MouseEvent me) {
			// if the cursor is inside the popup bar
			// or the keyboar triggred activation
			// then we do not want to deactivate
			if (!isDiagramAssistant(me.getSource()))
				setAvoidHidingDiagramAssistant(false);

			setMouseLocation(me.getLocation());
			if (getIsDisplayAtMouseHoverLocation())
				showDiagramAssistantAfterDelay(getAppearanceDelayLocationSpecific()); // no
																						// delay
			else if (shouldShowDiagramAssistant()) {
				showDiagramAssistant(getMouseLocation()); // no delay
			}
	}

	/**
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me) {

		if(getIsDisplayAtMouseHoverLocation())
			{
			Object srcObj = me.getSource();
			if ((srcObj != null) && srcObj.equals(getHostFigure())) {
				hideDiagramAssistant();
			}
		}
		setAvoidHidingDiagramAssistant(true);
		setMouseLocation(me.getLocation());

		if (!getIsDisplayAtMouseHoverLocation()) {
			// if the cursor is inside the popup bar
			// or the keyboar triggred activation
			// then we do not want to deactivate
			if (!isDiagramAssistant(me.getSource()))
				setAvoidHidingDiagramAssistant(false);

			showDiagramAssistantAfterDelay(getAppearanceDelay());
		}
	}

	/**
	 * Listens to the owner figure being moved so the handles can be removed
	 * when this occurs.
	 * 
	 * @author affrantz@us.ibm.com
	 * 
	 */
	private class OwnerMovedListener implements FigureListener {

		private Point myPopupBarLastPosition = new Point(0, 0);

		boolean hasPositionChanged(Rectangle theBounds) {
			if (theBounds.x != myPopupBarLastPosition.x)
				return true;
			if (theBounds.y != myPopupBarLastPosition.y)
				return true;
			return false;
		}

		/**
		 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
		 */
		public void figureMoved(IFigure source) {
			// for some reason we get more than one
			// figure moved call after compartment items are added
			// myActionMoveFigure handles the first one which we expect
			// hasPositionChanged handles the others caused by the selection of
			// the compartment
			// item.
			if (getFlag(POPUPBAR_MOVE_FIGURE)
				&& hasPositionChanged(source.getBounds())) {
				hideDiagramAssistant(); // without delay
			} else {
				setFlag(POPUPBAR_MOVE_FIGURE, false); // toggle flag back
				Rectangle theBounds = source.getBounds();
				myPopupBarLastPosition.setLocation(theBounds.x, theBounds.y);

			}

		}
	}

	/**
	 * Listens for mouse key presses so the popup bar can be dismissed if the context 
	 * menu is displayed
	 * 
	 * @author affrantz@us.ibm.com
	 */
	private class PopupBarMouseListener extends MouseListener.Stub {

		/**
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
		 */
		public void mousePressed(MouseEvent me) {
			if (3 == me.button) // context menu, hide the popup bar
			{
				hideDiagramAssistant();
			}
			super.mousePressed(me);
			setPopupBarOnDiagramActivated(true);
		}
		public void mouseReleased(MouseEvent me)
		{
			super.mouseReleased(me);

		}
	}

	/* ************************* End nested classes ******************** */

	/** Y postion offset from shape where the balloon top begin. */
	static private int BALLOON_Y_OFFSET = 10;

	/** Y postion offset from shape where the balloon top begin. */
	static private double BALLOON_X_OFFSET_RHS = 0.65;

	static private double BALLOON_X_OFFSET_LHS = 0.25;

	/** Y postion offset from shape where the balloon top begin. */
	static private int ACTION_WIDTH_HGT = 30;

	static private int ACTION_BUTTON_START_X = 5;

	static private int ACTION_BUTTON_START_Y = 5;

	static private int ACTION_MARGIN_RIGHT = 10;

	/** popup bar bits */
	static private int POPUPBAR_ACTIVATEONHOVER				= 0x01; /* Display the action when hovering*/
	static private int POPUPBAR_MOVE_FIGURE			 		= 0x02; /* Ignore the first figureMoved event when creating elements inside a shape via a popup bar*/ 
	static private int POPUPBAR_DISPLAYATMOUSEHOVERLOCATION	= 0x04; /* Display the popup bar at the mouse location used by diagrams and machine edit parts*/
	static private int POPUPBAR_ONDIAGRAMACTIVATED				= 0x10; /* For popup bars on diagram and machine edit parts, where we POPUPBAR_DISPLAYATMOUSEHOVERLOCATION, don't display popup bar until user clicks on surface*/
	static private int POPUPBAR_HOST_IS_CONNECTION				= 0x20; /* For popup bars on connection edit parts*/

	/** Bit field for the actrionbar associated bits */
	private int myPopupBarFlags = POPUPBAR_ACTIVATEONHOVER;

	private double myBallonOffsetPercent = BALLOON_X_OFFSET_RHS;

	/** the figure used to surround the action buttons */
	private IFigure myBalloon = null;

	/** The popup bar descriptors for the popup bar buttons */
	private List myPopupBarDescriptors = new ArrayList();

	/** Images created that must be deleted when popup bar is removed */
	protected List imagesToBeDisposed = new ArrayList();

	/** mouse keys listener for the owner shape */
	private PopupBarMouseListener myMouseKeyListener = new PopupBarMouseListener();

	/** listener for owner shape movement */
	private OwnerMovedListener myOwnerMovedListener = new OwnerMovedListener();

	/** flag for whether mouse cursor within shape */

	private void setFlag(int bit, boolean b)
	{
		if (b)
			myPopupBarFlags |= bit;
		else if (getFlag(bit))
			myPopupBarFlags ^= bit;

	}

	private boolean getFlag(int bit)
	{
		return ((myPopupBarFlags & bit) > 0);
	}


	
	private void setPopupBarOnDiagramActivated(boolean bVal)
	{
		setFlag(POPUPBAR_ONDIAGRAMACTIVATED, bVal);
	}
	private boolean getPopupBarOnDiagramActivated()
	{
		return getFlag(POPUPBAR_ONDIAGRAMACTIVATED);
	}

	/**
	 * set the host is connection flag
	 * @param bVal the new value
	 */
	protected void setHostConnection(boolean bVal)
	{
		setFlag(POPUPBAR_HOST_IS_CONNECTION, bVal);
	}

	/**
	 * get the host is connection flag
	 * @return true or false
	 */
	protected boolean isHostConnection()
	{
		return getFlag(POPUPBAR_HOST_IS_CONNECTION);
	}

	/**
	 * Populates the popup bar with popup bar descriptors added by suclassing
	 * this editpolicy (i.e. <code>fillPopupBarDescriptors</code> and by
	 * querying the modeling assistant service for all types supported on the
	 * popup bar of this host. For those types added by the modeling assistant
	 * service the icons are retrieved using the Icon Service.
	 */
	protected void populatePopupBars() {
		fillPopupBarDescriptors();
		List types = ModelingAssistantService.getInstance()
			.getTypesForPopupBar(getHost());
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			Object type = iter.next();
			if (type instanceof IElementType) {
				addPopupBarDescriptor((IElementType) type, IconService
					.getInstance().getIcon((IElementType) type));
			}
		}
	}

	/**
	 * This is the entry point that subclasses can override to fill the
	 * popup bar descrioptors if they have customized tools that cannot be done
	 * using the type along with the modeling assistant service.
	 */
	protected void fillPopupBarDescriptors() {
		// subclasses can override.
	}

	private boolean isSelectionToolActive()
	{
		// getViewer calls getParent so check for null
		if(getHost().getParent() != null && getHost().isActive() )
		{
			Tool theTool = getHost().getViewer().getEditDomain().getActiveTool();
			if((theTool != null) && theTool instanceof SelectionTool)
			{
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#shouldShowDiagramAssistant()
	 */
	protected boolean shouldShowDiagramAssistant()
	{
		if (!super.shouldShowDiagramAssistant()) {
			return false;
		}

		if(this.getIsDisplayAtMouseHoverLocation())
		{
			if (isHostConnection())
				return isSelectionToolActive();
			if (getPopupBarOnDiagramActivated())
				return isSelectionToolActive();
			return false;
		}
		else
			return isSelectionToolActive();

	}

	/**
	 * allows plugins to add their own popup bar tools and tips
	 * @param elementType
	 * @param theImage
	 * @param theTracker
	 * @param theTip
	 */
	protected void addPopupBarDescriptor(
			IElementType elementType,
			Image theImage,
			DragTracker theTracker,
			String theTip) {

		PopupBarDescriptor desc =
			new PopupBarDescriptor(theTip, theImage, elementType, theTracker);
		myPopupBarDescriptors.add(desc);

	}

	/**
	 * adds popup bar descriptor
	 * @param elementType
	 * @param theImage
	 * @param theTracker
	 */
	protected void addPopupBarDescriptor(
		IElementType elementType,
		Image theImage,
		DragTracker theTracker) {

		String theInputStr = DiagramUIMessages.PopupBar_AddNew;


		String theTip = NLS.bind(theInputStr, elementType.getDisplayName());
	
		addPopupBarDescriptor(elementType, theImage, theTracker, theTip);
	}

	/**
	 * default method for plugins which passes along the PopupBarTool
	 * as the tool to be used.
	 * @param elementType
	 * @param theImage
	 */
	protected void addPopupBarDescriptor(IElementType elementType,
			Image theImage) {

		this.addPopupBarDescriptor(elementType, theImage,
			new PopupBarTool(getHost(), elementType));

	}

	/**
	 * @param elementType
	 * @param theImage
	 * @param theTip
	 */
	protected void addPopupBarDescriptor(
			IElementType elementType,
			Image theImage,
			String theTip) {

		PopupBarTool theTracker =
			new PopupBarTool(getHost(), elementType);
		PopupBarDescriptor desc =
			new PopupBarDescriptor(theTip, theImage, elementType, theTracker);
		myPopupBarDescriptors.add(desc);

	}

	/**
	 * method used primarily to add UnspecifiedTypeCreationTool
	 * @param elementType
	 * @param theImage
	 * @param theRequest the create request to be used
	 */
	protected void addPopupBarDescriptor(
			IElementType elementType,
			Image theImage,
			CreateRequest theRequest)
	{

		PopupBarTool theTracker =
			new PopupBarTool(getHost(), theRequest);

		this.addPopupBarDescriptor(elementType, theImage, theTracker);

	}

	/**
	 * gets the popup bar descriptors
	 * @return list
	 */
	protected List getPopupBarDescriptors() {
		return myPopupBarDescriptors;
	}

	/**
	 * initialize the popup bars from the list of action descriptors.
	 */
	private void initPopupBars() {

		List theList = getPopupBarDescriptors();
		if (theList.isEmpty()) {
			return;
		}
		myBalloon = createPopupBarFigure();

		int iTotal = ACTION_WIDTH_HGT * theList.size() + ACTION_MARGIN_RIGHT;

		getBalloon().setSize(
			iTotal,
			ACTION_WIDTH_HGT + 2 * ACTION_BUTTON_START_Y);

		int xLoc = ACTION_BUTTON_START_X;
		int yLoc = ACTION_BUTTON_START_Y;

		for (Iterator iter = theList.iterator(); iter.hasNext();) {
			PopupBarDescriptor theDesc = (PopupBarDescriptor) iter.next();

			// Button b = new Button(theDesc.myButtonIcon);
			PopupBarLabelHandle b =
				new PopupBarLabelHandle(
					theDesc.getDragTracker(),
					theDesc.getIcon());

			Rectangle r1 = new Rectangle();
			r1.setLocation(xLoc, yLoc);
			xLoc += ACTION_WIDTH_HGT;
			r1.setSize(
				ACTION_WIDTH_HGT,
				ACTION_WIDTH_HGT - ACTION_MARGIN_RIGHT);

			Label l = new Label();
			l.setText(theDesc.getToolTip());

			b.setToolTip(l);
			b.setPreferredSize(ACTION_WIDTH_HGT, ACTION_WIDTH_HGT);
			b.setBounds(r1);

			getBalloon().add(b);

			b.addMouseMotionListener(this);
			b.addMouseListener(this.myMouseKeyListener);

		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#getPreferenceName()
	 */
	String getPreferenceName() {
		return IPreferenceConstants.PREF_SHOW_POPUP_BARS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#isDiagramAssistantShowing()
	 */
	protected boolean isDiagramAssistantShowing() {
		return getBalloon() != null;
	}

	private IFigure getBalloon() {
		return myBalloon;
	}

	protected IFigure createPopupBarFigure() {
		return new RoundedRectangleWithTail();
	}

	protected void showDiagramAssistant(Point referencePoint) {

		// already have a one
		if (getBalloon() != null && getBalloon().getParent() != null) 
		{
			return;
		}

		if (this.myPopupBarDescriptors.isEmpty()) 
		{

			populatePopupBars();
			initPopupBars();

			if (myPopupBarDescriptors.isEmpty()) {
				return; // nothing to show
			}
		}
		getBalloon().addMouseMotionListener(this);
		getBalloon().addMouseListener(myMouseKeyListener);

		// the feedback layer figures do not recieve mouse events so do not use
		// it for popup bars
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		layer.add(getBalloon());
		
		if (referencePoint == null) {
			referencePoint = getHostFigure().getBounds().getCenter();
		}

		Point thePoint = getBalloonPosition(referencePoint);

		getBalloon().setLocation(thePoint);

		// dismiss the popup bar after a delay
		if(!shouldAvoidHidingDiagramAssistant())
		{
			hideDiagramAssistantAfterDelay(getDisappearanceDelay());
		}
	}

	/**
	 * getter for the IsDisplayAtMouseHoverLocation flag
	 * @return true or false
	 */
	protected boolean getIsDisplayAtMouseHoverLocation()
	{
		return getFlag(POPUPBAR_DISPLAYATMOUSEHOVERLOCATION);
	}

	/**
	 * setter for the IsDisplayAtMouseHoverLocation
	 * @param bVal
	 */
	protected void setIsDisplayAtMouseHoverLocation(boolean bVal)
	{
		setFlag(POPUPBAR_DISPLAYATMOUSEHOVERLOCATION, bVal);
	}

	/**
	 * For editparts that consume the entire viewport, statechart, structure,
	 * communication, we want to display the popup bar at the mouse location.
	 * @param referencePoint
	 *            The reference point which may be used to determine where the
	 *            diagram assistant should be located. This is most likely the
	 *            current mouse location. 
	 *            @return Point
	 */
	private Point getBalloonPosition(Point referencePoint)
	{
		Point thePoint = new Point();
		boolean atMouse = getIsDisplayAtMouseHoverLocation();
		if (atMouse) {
			thePoint.setLocation(referencePoint);
			getHostFigure().translateToAbsolute(thePoint);
			getBalloon().translateToRelative(thePoint);

			// shift the ballon so it is above the cursor.
			thePoint.y -= ACTION_WIDTH_HGT;
			adjustToFitInViewport(thePoint);
		}
		else
		{
			Dimension theoffset = new Dimension();
			Rectangle rcBounds = getHostFigure().getBounds().getCopy();

			getHostFigure().translateToAbsolute(rcBounds);
			getBalloon().translateToRelative(rcBounds);

			theoffset.height = -(BALLOON_Y_OFFSET + ACTION_WIDTH_HGT);
			theoffset.width = (int) (rcBounds.width * myBallonOffsetPercent);

			thePoint.x = rcBounds.x + theoffset.width;
			thePoint.y = rcBounds.y + theoffset.height;
			adjustToFitInViewport(thePoint);
		}
		return thePoint;
	}

	/**
     * Uses the balloon location passed in and its size to determine if the
     * balloon will appear outside the viewport. If so, the balloon location
     * will be modified accordingly.
     * 
     * @param balloonLocation
     *            the suggested balloon location passed in and potentially
     *            modified when this method completes
     */
    private void adjustToFitInViewport(Point balloonLocation) {
        Control control = getHost().getViewer().getControl();
        if (control instanceof FigureCanvas) {
            Rectangle viewportRect = ((FigureCanvas) control).getViewport()
                .getClientArea();
            Rectangle balloonRect = new Rectangle(balloonLocation, getBalloon()
                .getSize());

            int yDiff = viewportRect.y - balloonRect.y;
            if (yDiff > 0) {
                // balloon is above the viewport, shift down
                balloonLocation.translate(0, yDiff);
            }
            int xDiff = balloonRect.right() - viewportRect.right();
            if (xDiff > 0) {
                // balloon is to the right of the viewport, shift left
                balloonLocation.translate(-xDiff, 0);
            }
        }
    }

	private void teardownPopupBar() {
		getBalloon().removeMouseMotionListener(this);
		getBalloon().removeMouseListener(myMouseKeyListener);
		// the feedback layer figures do not recieve mouse events
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		if (myBalloon.getParent() != null) {
			layer.remove(myBalloon);
		}
		myBalloon = null;

		this.myPopupBarDescriptors.clear();

		for (Iterator iter = imagesToBeDisposed.iterator(); iter.hasNext();) {
			((Image) iter.next()).dispose();
		}
		imagesToBeDisposed.clear();

	}

	protected void hideDiagramAssistant() {
		if (getBalloon() != null) {

			teardownPopupBar();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#showDiagramAssistantAfterDelay(int)
	 */
	protected void showDiagramAssistantAfterDelay(int theDelay) {
		// only show the popup bar if it isn't already showing
		if (!isDiagramAssistantShowing()) {
			super.showDiagramAssistantAfterDelay(theDelay);
		}
	}

	public void activate() {
		super.activate();

		getHostFigure().addMouseListener(this.myMouseKeyListener);
		getHostFigure().addFigureListener(this.myOwnerMovedListener);

		if (getHost() instanceof ISurfaceEditPart) {
			setIsDisplayAtMouseHoverLocation(true);
		}
	}

	public void deactivate() {
		getHostFigure().removeMouseListener(this.myMouseKeyListener);
		getHostFigure().removeFigureListener(this.myOwnerMovedListener);

		super.deactivate();

	}

	/**
	 * This is the default which places the popup bar to favor the right side
	 * of the shape
	 * 
     * @deprecated this is not being used anymore
	 */
	protected void setRightHandDisplay() {
		this.myBallonOffsetPercent = BALLOON_X_OFFSET_RHS;
	}

	/**
	 * Place the popup bar to favor the left had side of the shape
	 * @deprecated this is not being used anymore
	 */
	protected void setLeftHandDisplay() {
		this.myBallonOffsetPercent = BALLOON_X_OFFSET_LHS;
	}

	/**
	 * check thee right display status
	 * @return true or false
     * @deprecated this is not being used anymore
	 */
	protected boolean isRightDisplay() {
		return (BALLOON_X_OFFSET_RHS == myBallonOffsetPercent);
	}

	/**
	 * Gets the amount of time to wait before showing the popup bar if the
	 * popup bar is to be shown at the mouse location
	 * {@link #getIsDisplayAtMouseHoverLocation()}.
	 * 
	 * @return the time to wait in milliseconds
	 */
	protected int getAppearanceDelayLocationSpecific() {
		return getAppearanceDelay();
	}

    protected String getDiagramAssistantID() {
        return PopupBarEditPolicy.class.getName();
    }
}
