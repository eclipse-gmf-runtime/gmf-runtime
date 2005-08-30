/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.text.MessageFormat;
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
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.AbstractAddActionBarTool;
import org.eclipse.gmf.runtime.diagram.ui.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.tools.AddActionBarTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.AddUMLActionBarTool;
import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * 
 * ActionBars are esentially a cartoon balloon with buttons that are activated
 * during mouse hover over a shape. The current thinking is to start this by
 * including the Add UML commands for the appropirate shape. The idea for this
 * came from the bpel editor and we are drawing the balloon the same as the
 * bpel editor. Addtional usages for actionbar's (besides add uml)
 * Shapes/Connectors Format Commands Shapes/Connectors Appearance Commands
 * Shapes/Connectors User defined would be nice Diagram Add UML (essentially
 * the current toolbox)
 * 
 * @author affrantz@us.ibm.com
 */

public class ActionBarEditPolicy extends GraphicalEditPolicy {
	
	/* ************************** nested classes ************************/
	/**
	 *
	 * Class to hold pertinent information about the tool placed on the actionbar
	 * 
	 * @author affrantz@us.ibm.com 
	 */
	private class ActionBarDescriptor {
		/** The action button tooltip */
		private String _tooltip = new String();

		/** The image for the button */
		private Image _icon = null;

		/** The typeinfo used to create the Request for the command */
		private IElementType _elementType;

		/** The DracgTracker / Tool associatd with the actionbar button */
		private DragTracker _dragTracker = null;

		/**
		 * constructor
		 * @param s
		 * @param i
		 * @param elementType
		 * @param theTracker
		 */
		public ActionBarDescriptor(
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

	} // end ActionBarDescriptor
	
	/**
	 * Default tool placed on the actionbar
	 * 
	 * @author affrantz@us.ibm.com 
	 */
	private class ActionBarLabelHandle extends Label implements Handle {
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
		public ActionBarLabelHandle(DragTracker tracker, Image theImage) {
			super(theImage);
			myDragTracker = tracker;
			this.setOpaque(true);
			this.setBackgroundColor(ColorConstants.buttonLightest);
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
		 *      set ActionBarEditPolicy.myActionMoveFigure bit so the ActionBar
		 *      is not dismissed after creating an item in the editpart
		 *  
		 */
		public void handleMousePressed(MouseEvent event) {

			if (1 == event.button) 
			{
				// this is the flag in ActionBarEditPolicy that
				// prevents the actionbar from dismissing after a new item
				// is added to a shape, which causes the editpart to be
				// resized.
				setFlag(ACTIONBAR_MOVE_FIGURE, true);
				// future: when other tools besides AddActionBarTool are
				// used
				// we will need a way in which to call

			}

			super.handleMousePressed(event);
		}

		
		/**
		 * checks if tool tip is enabled or not 
		 * @return true or false
		 */
		protected boolean isToolEnabled()
		{
			if((myDragTracker != null) && (myDragTracker instanceof AbstractAddActionBarTool))
			{
				AbstractAddActionBarTool abarTool = (AbstractAddActionBarTool) myDragTracker;
				return abarTool.isCommandEnabled();
			}
			return true;
		}
		
		
		
		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			if(!isToolEnabled())
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

	private static Image DESC_ACTION_ACTIONBAR_PLUS_IMAGE = Images.DESC_ACTION_ACTIONBAR_PLUS.createImage();
	private static Image DESC_ACTION_ACTIONBAR_IMAGE = Images.DESC_ACTION_ACTIONBAR.createImage();
	/**
	 *
	 * This is the figure that represents the ballon portion of the actionbar
	 * 
	 * @author affrantz@us.ibm.com
	 */
	private class RoundedRectangleWithTail extends RoundedRectangle {
		
		private Image myActionTailImage = null;
		private boolean bIsInit=false;
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
			theBounds.height -= theTail.getBounds().height ;
			theBounds.height -= shiftWidth;// shift slight above cursor
			theBounds.x 	+= shiftWidth; // shift slight to right of cursor
			theBounds.width -= (shiftWidth+1); // otherwise rhs is clipped
			

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
				if(getIsDisplayAtMouseHoverLocation() && !getHostIsConnector())
				{
					if(myActionTailImage == null)
					{
						myActionTailImage = DESC_ACTION_ACTIONBAR_PLUS_IMAGE;
						bIsInit=true;
					}
				}
				else
				{
					if(myActionTailImage == null)
					{
						myActionTailImage = DESC_ACTION_ACTIONBAR_IMAGE;
						bIsInit=true;
					}
				}
				
			}
			return myActionTailImage;
		
		}

	}
	
	/**
	 * Listens to mouse events on the owner shape and handles so that the
	 * action bar balloon can be added or removed.
	 * 
	 * @author affrantz@us.ibm.com
	 */
	private class ActionBarMouseMotionListener extends MouseMotionListener.Stub {

		public void mouseEntered(MouseEvent me) {
			// Avoid handles from being removed if the mouse has moved back
			// inside the shape when the handles were already being displayed.
			setMouseInsideShape(true);
		}

		public void mouseExited(MouseEvent me) {
			setMouseInsideShape(false);
			hideActionBalloonDelayed(HIDE_DELAY);
		}
		/**
		 * We the tools to dismiss after the delay period if 
		 * the mouse is inside the shape, but we do not want the tools to 
		 * dismiss if the mouse is inside the one of the tools.
		 * 
		 */
		private boolean shouldHoverActivate(Object theObj)
		{
			if(theObj instanceof RoundedRectangleWithTail)
				return true;
			if(theObj instanceof ActionBarLabelHandle)
				return true;
			
			
			return false;
		}
		/**
		 * Adds the connector handles after a delay
		 */
		public void mouseHover(MouseEvent me) {
			if(getActivateOnHover())
			{
				// if the cursor is inside the actionbar
				// or the keyboar triggred activation 
				// then we do not want to deactivate
				if(!shouldHoverActivate(me.getSource()))
					setActivateOnHover(false);
				
				myMouseHoverLocation.setLocation(me.getLocation());
				if(getIsDisplayAtMouseHoverLocation())
					showActionBalloonDelayed(SHOW_DELAY_LOCATION_SPECIFIC); // no delay
				else
					showActionBalloon(); // no delay
			}
				
			
		}
		/**
		 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.MouseEvent)
		 */
		public void mouseMoved(MouseEvent me) {
			
			if(getIsDisplayAtMouseHoverLocation() && shouldDisplayActionBar())
			{
				Object srcObj = me.getSource(); 
				if((srcObj!=null)&& srcObj.equals(getHostFigure()))
				{	
					hideActionBalloon();
				}
			}
			setActivateOnHover(true);
			setMouseMoveLocation(me.getLocation());
			setMyMouseHoverLocation(me.getLocation());
			
			if(!getIsDisplayAtMouseHoverLocation() && shouldDisplayActionBar()) {
				// if the cursor is inside the actionbar
				// or the keyboar triggred activation 
				// then we do not want to deactivate
				if(!shouldHoverActivate(me.getSource()))
					setActivateOnHover(false);
				
				showActionBalloonDelayed(DEFAULT_SHOW_DELAY);
			}

			super.mouseMoved(me);
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

		private Point myActionBarLastPosition = new Point(0, 0);

		boolean hasPositionChanged(Rectangle theBounds) {
			if (theBounds.x != myActionBarLastPosition.x)
				return true;
			if (theBounds.y != myActionBarLastPosition.y)
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
			if (getFlag(ACTIONBAR_MOVE_FIGURE)
				&& hasPositionChanged(source.getBounds())) {
				hideActionBalloon(); // without delay
			} else {
				setFlag(ACTIONBAR_MOVE_FIGURE, false); // toggle flag back
				Rectangle theBounds = source.getBounds();
				myActionBarLastPosition.setLocation(theBounds.x, theBounds.y);

			}

		}
	}

	/**
	 * Listens to the focus events on the owner editpart so that the handles
	 * can be added when the space bar is pressed. I tried to use
	 * IFigure.addFocusListener(), but the figure isn't getting any focus
	 * change events when the space bar is pressed.
	 * 
	 * @author affrantz@us.ibm.com 
	 */
	private class EditPartFocusListener extends EditPartListener.Stub {
		/**
		 * @see org.eclipse.gef.EditPartListener#selectedStateChanged(org.eclipse.gef.EditPart)
		 */
		public void selectedStateChanged(EditPart part) {
	
			if (part.hasFocus() && isMouseInsideShape()) {
				showActionBalloon();
			} else {
				hideActionBalloon();
			}
		}
	}
	/**
	 *
	 * Listens for mouse key presses so the actionbar can be dismissed if the context 
	 * menu is displayed
	 * 
	 * @author affrantz@us.ibm.com 
	 */
	private class ActionBarMouseListener extends MouseListener.Stub {
		
		/**
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
		*/
		public void mousePressed(MouseEvent me) {
			if(3 == me.button) // context menu, hide the actionbar
			{
				hideActionBalloon();
			}
			super.mousePressed(me);
			setActionbarOnDiagramActivated(true);
		}
		public void mouseReleased(MouseEvent me)
		{
			super.mouseReleased(me);
			
		}
}
	/* ************************* End nested classes *********************/

	/** Delay in ms to wait after leaving shape editpart. */
	static private int HIDE_DELAY = 1000;
	
	/**
	 * Delay in ms to wait before showing the actionbar upon entering the shape.
	 */
	private static final int DEFAULT_SHOW_DELAY = 200;

	/**
	 * Delay in ms to wait for displaying the actionBar on a diagrms or machine
	 * diagram. There is no delay when displaying for a typcail shape.
	 */
	static private int SHOW_DELAY_LOCATION_SPECIFIC = 1000;
	
	/** Only keep the action bar visible for this length of time.
	 * 
	*/
	static private int STAY_VISIBLE_DELAY = 2000;

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

	/** ActionBar bits */
	static private int ACTIONBAR_ACTIVATEONHOVER				= 0x01; /* Display the action when hovering*/
	static private int ACTIONBAR_MOVE_FIGURE			 		= 0x02; /* Ignore the first figureMoved event when creating elements inside a shape via an actionBar*/ 
	static private int ACTIONBAR_DISPLAYATMOUSEHOVERLOCATION	= 0x04; /* Display the actIOnbar at the mouse location used by diagrams and machine edit parts*/
	static private int ACTIONBAR_MOUSEINSIDE_SHAPE 				= 0x08; /* is the Mouse inside the bounds of the shape */
	static private int ACTIONBAR_ONDIAGRAMACTIVATED				= 0x10; /* For actionBars on diagram and machine edit parts, where we ACTIONBAR_DISPLAYATMOUSEHOVERLOCATION, don't display actionBar until user clicks on surface*/
	static private int ACTIONBAR_HOST_IS_CONNECTOR				= 0x20; /* For actionBars on connector edit parts*/
	
	/** Bit field for the actrionbar associated bits */
	private int myActionBarFlags 								= ACTIONBAR_ACTIVATEONHOVER;
	
	private double myBallonOffsetPercent = BALLOON_X_OFFSET_RHS;

	/** the figure used to surround the action buttons */
	private RoundedRectangleWithTail myBalloon = null;

	private Point myMouseHoverLocation = new Point();
	
	/** The action bar descriptors for the action bar buttons */
	private List myActionBarDescriptors = new ArrayList();

	/** Images created that must be deleted when actionbar is removed */
	protected List imagesToBeDisposed = new ArrayList();
	
	/** mouse motion listener for the owner shape and handles */
	private ActionBarMouseMotionListener myMouseListener = new ActionBarMouseMotionListener();
	
	/** mouse keys listener for the owner shape*/
	private ActionBarMouseListener myMouseKeyListener = new ActionBarMouseListener();

	/** listener for owner shape movement */
	private OwnerMovedListener myOwnerMovedListener = new OwnerMovedListener();

	/** listener for focus change on editpart */
	private EditPartFocusListener myFocusListener = new EditPartFocusListener();

	private Point myMouseMoveLocation = new Point();
	
	/** flag for whether mouse cursor within shape */
	
	private void setFlag(int bit, boolean b)
	{
		if(b)
			myActionBarFlags |= bit;
		else if(getFlag(bit))
			myActionBarFlags ^= bit;

	}

	private boolean getFlag(int bit)
	{
		return ((myActionBarFlags & bit) > 0);
	}


	
	private void setActionbarOnDiagramActivated(boolean bVal)
	{
		setFlag(ACTIONBAR_ONDIAGRAMACTIVATED, bVal);
	}
	private boolean getActionbarOnDiagramActivated()
	{
		return getFlag(ACTIONBAR_ONDIAGRAMACTIVATED);
	}
	// Member functions
	private void setActivateOnHover(boolean bVal)
	{
		setFlag(ACTIONBAR_ACTIVATEONHOVER, bVal);
	}
	private boolean getActivateOnHover()
	{
		return getFlag(ACTIONBAR_ACTIVATEONHOVER);
	}
	
	/**
	 * set the host is connector flag 
	 * @param bVal the new value
	 */
	protected void setHostIsConnector(boolean bVal)
	{
		setFlag(ACTIONBAR_HOST_IS_CONNECTOR, bVal);
	}
	
	/**
	 * get the host is connector flag 
	 * @return true or false
	 */
	protected boolean getHostIsConnector()
	{
		return getFlag(ACTIONBAR_HOST_IS_CONNECTOR);
	}
	
	/**
	 * Populates the action bar with actionbar descriptors added by suclassing
	 * this editpolicy (i.e. <code>fillActionBarDescriptors</code> and by
	 * querying the modeling assistant service for all types supported on the
	 * actionbar of this host. For those types added by the modeling assistant
	 * service the icons are retrieved using the Icon Service.
	 */
	protected void populateActionBars() {
		fillActionDescriptors();
		List types = ModelingAssistantService.getInstance()
			.getTypesForActionBar(getHost());
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			Object type = iter.next();
			if (type instanceof IElementType) {
				addActionBarDescriptor2((IElementType) type, IconService
					.getInstance().getIcon((IElementType) type));
			}
		}
	}

	/**
	 * This is the entry point that subclasses can override to fill the
	 * actionbar descrioptors if they have customized tools that cannot be done
	 * using the type along with the modeling assistant service.
	 */
	protected void fillActionDescriptors() {
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
	/** Never display actionbars when there is an active tool on the pallette
	 * 
	 */ 
	private boolean shouldDisplayActionBar()
	{
		//RATLC00532515: ActionBars shouldn't be shown if the host edit part isn't editable.
		if (getHost() instanceof GraphicalEditPart) {
			if (!((GraphicalEditPart)getHost()).isEditModeEnabled()) {
				return false;
			}
		}
		if(this.getIsDisplayAtMouseHoverLocation())
		{
			if(getHostIsConnector())
				return isSelectionToolActive();
			 if(getActionbarOnDiagramActivated())
			 	return isSelectionToolActive();
			 return false;
		}
		else
			return isSelectionToolActive();

	}
	
	/**
	 * allows plugins to add their own actionbar tools and tips
	 * @param elementType
	 * @param theImage
	 * @param theTracker
	 * @param theTip
	 */
	protected void addActionBarDescriptor(
			IElementType elementType,
			Image theImage,
			DragTracker theTracker,
			String theTip) {

		ActionBarDescriptor desc =
			new ActionBarDescriptor(theTip, theImage, elementType, theTracker);
		myActionBarDescriptors.add(desc);

	}

	/**
	 * adds action bar descriptor
	 * @param elementType
	 * @param theImage
	 * @param theTracker
	 */
	protected void addActionBarDescriptor(
		IElementType elementType,
		Image theImage,
		DragTracker theTracker) {

		String theInputStr = PresentationResourceManager.getI18NString("ActionBar.AddNew"); //$NON-NLS-1$
		
		
		String theTip = MessageFormat.format(theInputStr, new Object[] {elementType.getDisplayName()});
	
		addActionBarDescriptor(elementType, theImage, theTracker, theTip);
	}

	
	/**
	 * default method for plugins which passes along the AddActionBarTool
	 * as the tool to be used.
	 * @param elementType
	 * @param theImage
	 */
	protected void addActionBarDescriptor2(IElementType elementType,
			Image theImage) {

		this.addActionBarDescriptor(elementType, theImage,
			new AddActionBarTool(getHost(), elementType));

	}
	
	/**
	 * @param elementType
	 * @param theImage
	 * @param theTip
	 */
	protected void addActionBarDescriptor2(
			IElementType elementType,
			Image theImage,
			String theTip) {

		AddActionBarTool theTracker =
			new AddActionBarTool(getHost(), elementType);
		ActionBarDescriptor desc =
			new ActionBarDescriptor(theTip, theImage, elementType, theTracker);
		myActionBarDescriptors.add(desc);

	}
	
	/**
	 * method used primarily to add UnspecifiedTypeCreationTool
	 * @param elementType
	 * @param theImage
	 * @param theRequest the create request to be used
	 */
	protected void addActionBarDescriptor2(
			IElementType elementType,
			Image theImage,
			CreateRequest theRequest)
	{

		AddActionBarTool theTracker =
			new AddActionBarTool(getHost(), theRequest);
		
		this.addActionBarDescriptor(elementType, theImage, theTracker);
	
	}
	
	/**
	 * default method for plugins which passes along the AddUMLActionBarTool as
	 * the tool to be used.
	 * @param elementType
	 * @param theImage
	 * @deprecated Use <code>addActionBarDescriptor2</code> instead. All your
	 *             actionbar scenarios should be tested when migrating to
	 *             <code>addActionBarDescriptor2</code>. The difference
	 *             between the two is that <code>AddUMLActionBarTool</code>
	 *             has been removed and <code>AddActionBarTool</code> is now
	 *             used always. <code>AddUMLActionBarTool</code> used a
	 *             request to create an element only, whereas
	 *             <code>AddActionBarTool</code> uses a request to create an
	 *             element and view and if that does not return a command, then
	 *             it tries a request to create an element only. Alternatively,
	 *             you could migrate to use the Modeling Assistant Service to
	 *             handle the actionbar types instead of overriding this
	 *             editpolicy. Contact Cherie for assistance.
	 */
	protected void addActionBarDescriptor(
		IElementType elementType,
		Image theImage) {	
		
		DragTracker theTracker;
		if (elementType instanceof INotationType){
			theTracker = new AddActionBarTool(getHost(), elementType);
		}
		else{
			theTracker = new AddUMLActionBarTool(getHost(), elementType);
		}
		this.addActionBarDescriptor(elementType, theImage, theTracker);
		
	}
	
	/**
	 * @param elementType
	 * @param theImage
	 * @param theTip
	 * @deprecated Use <code>addActionBarDescriptor2</code> instead. All your
	 *             actionbar scenarios should be tested when migrating to
	 *             <code>addActionBarDescriptor2</code>. The difference
	 *             between the two is that <code>AddUMLActionBarTool</code>
	 *             has been removed and <code>AddActionBarTool</code> is now
	 *             used always. <code>AddUMLActionBarTool</code> used a
	 *             request to create an element only, whereas
	 *             <code>AddActionBarTool</code> uses a request to create an
	 *             element and view and if that does not return a command, then
	 *             it tries a request to create an element only. Contact Cherie
	 *             for assistance.
	 */
	protected void addActionBarDescriptor(
			IElementType elementType,
			Image theImage,
			String theTip) {

		AddUMLActionBarTool theTracker =
			new AddUMLActionBarTool(getHost(), elementType);
		ActionBarDescriptor desc =
			new ActionBarDescriptor(theTip, theImage, elementType, theTracker);
		myActionBarDescriptors.add(desc);

	}
	/**
	 * method used primarily to add UnspecifiedTypeCreationTool
	 * @param elementType
	 * @param theImage
	 * @param theTool 
	 * @deprecated Use <code>addActionBarDescriptor2</code> instead. All your
	 *             actionbar scenarios should be tested when migrating to
	 *             <code>addActionBarDescriptor2</code>. The difference
	 *             between the two is that <code>AddUMLActionBarTool</code>
	 *             has been removed and <code>AddActionBarTool</code> is now
	 *             used always. <code>AddUMLActionBarTool</code> used a
	 *             request to create an element only, whereas
	 *             <code>AddActionBarTool</code> uses a request to create an
	 *             element and view and if that does not return a command, then
	 *             it tries a request to create an element only. Contact Cherie
	 *             for assistance.
	 */
	protected void addActionBarDescriptor(
			IElementType elementType,
			Image theImage,
			CreateRequest theRequest)
	{

		AddUMLActionBarTool theTracker =
			new AddUMLActionBarTool(getHost(), theRequest);
		
		this.addActionBarDescriptor(elementType, theImage, theTracker);
	
	}

	/**
	 * gets the action bar descriptors
	 * @return list
	 */
	protected List getActionBarDescriptors() {
		return myActionBarDescriptors;
	}
	/**
	 * initialize the action bars from the list of action descriptors.
	 */
	private void initActionBars() {

		List theList = getActionBarDescriptors();
		if (theList.isEmpty()) {
			return;
		}
		createBalloon();

	    int iTotal = ACTION_WIDTH_HGT * theList.size() + ACTION_MARGIN_RIGHT;

		getBalloon().setSize(
			iTotal,
			ACTION_WIDTH_HGT + 2 * ACTION_BUTTON_START_Y);

		int xLoc = ACTION_BUTTON_START_X;
		int yLoc = ACTION_BUTTON_START_Y;

		for (Iterator iter = theList.iterator(); iter.hasNext();) {
			ActionBarDescriptor theDesc = (ActionBarDescriptor) iter.next();

			// Button b = new Button(theDesc.myButtonIcon);
			ActionBarLabelHandle b =
				new ActionBarLabelHandle(
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

			b.addMouseMotionListener(this.myMouseListener);
			b.addMouseListener(this.myMouseKeyListener);

		}
	}



	/** the <code>Runnable</code> used for the remove handles delay timer */
	private Runnable hideBalloonRunnable = new Runnable() {
		public void run() {
			if (!isMouseInsideShape() || !getActivateOnHover()) {
				hideActionBalloon();
			}
		}
	};
	
	/** the <code>Runnable</code> class used for the show balloon delay timer */
	private class ShowBalloonRunnable
		implements Runnable {

		/** the mouse location when the timer was started */
		private Point originalMouseLocation;

		/**
		 * @param originalMouseLocation
		 */
		protected ShowBalloonRunnable(Point originalMouseLocation) {
			this.originalMouseLocation = originalMouseLocation;
		}

		/**
		 * The connector handles are added when this task is run, assuming the
		 * mouse is still at the same spot where it was when the timer was
		 * started (i.e. only add the connector handles when the user stops
		 * moving the mouse).
		 */
		public void run() {
			if (isMouseInsideShape()
				&& originalMouseLocation.equals(getMouseMoveLocation())) {
				showActionBalloon();
			}
		}
	}
	
	private void setMouseInsideShape(boolean bVal) {
		this.setFlag(ACTIONBAR_MOUSEINSIDE_SHAPE, bVal);
	}
	private boolean isMouseInsideShape() {
		return getFlag(ACTIONBAR_MOUSEINSIDE_SHAPE);
	}
	
	/**
	 * Is the Action bar preference turned on?
	 * 
	 * @return true iff the show action bar preference is turned on
	 */
	private boolean isPreferenceOn() {
		IPreferenceStore preferenceStore = (IPreferenceStore)
			((IGraphicalEditPart) getHost())
				.getDiagramPreferencesHint().getPreferenceStore();
		return preferenceStore.getBoolean(
			IPreferenceConstants.PREF_SHOW_ACTION_BARS);
	}
	private RoundedRectangleWithTail getBalloon() {
		return myBalloon;
	}
	private void createBalloon() {
		myBalloon = new RoundedRectangleWithTail();
	}
	private void showActionBalloon() {
		if(!shouldDisplayActionBar()  || !isPreferenceOn())
			return;
		
		// already have a one 
		if (getBalloon() != null && getBalloon().getParent() != null) 
		{
			return ;
		}
		
		if (this.myActionBarDescriptors.isEmpty()) 
		{
			
			populateActionBars();
			initActionBars();

			if (myActionBarDescriptors.isEmpty()) {
				return; // nothing to show
			}
		}
		getBalloon().addMouseMotionListener(myMouseListener);
		getBalloon().addMouseListener(myMouseKeyListener);
		

		// the feedback layer figures do not recieve mouse events so do not use
		// it for actionbars
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		layer.add(getBalloon());
				
		Point thePoint = getBalloonPosition();

		getBalloon().setLocation(thePoint);
		
		// dismiss the actionBar after STAY_VISIBLE_DELAY ms.
		if(!getActivateOnHover())
		{
			hideActionBalloonDelayed(STAY_VISIBLE_DELAY);
		}
	}	
	
	/**
	 * getter for the IsDisplayAtMouseHoverLocation flag
	 * @return true or false
	 */
	protected boolean getIsDisplayAtMouseHoverLocation()
	{
		return getFlag(ACTIONBAR_DISPLAYATMOUSEHOVERLOCATION);
	}
	/**
	 * setter for the IsDisplayAtMouseHoverLocation
	 * @param bVal 
	 */
	protected void setIsDisplayAtMouseHoverLocation(boolean bVal)
	{
		setFlag(ACTIONBAR_DISPLAYATMOUSEHOVERLOCATION, bVal);
	}
	
	/** 
	 * For editparts that consume the entire viewport, statechart, structure, communication,
	 * we want to display the actionbar at the mouse location.  
	 * @return Point
	 */ 
	private Point getBalloonPosition()
	{
		Point thePoint = new Point();
		boolean atMouse = getIsDisplayAtMouseHoverLocation();
		if(atMouse)
		{	
			thePoint.setLocation(myMouseHoverLocation);
			getHostFigure().translateToAbsolute(thePoint);
			getBalloon().translateToRelative(thePoint);
			
			//	shift the ballon so it is above the cursor.
			thePoint.y -= ACTION_WIDTH_HGT;  
			
			if (willBalloonBeClipped(thePoint)) {
				Rectangle rcBounds = getHostFigure().getBounds().getCopy();
				getHostFigure().translateToAbsolute(rcBounds);
				getBalloon().translateToRelative(rcBounds);
				Dimension dim = getBalloon().getSize();
				int offsetX = dim.width + ACTION_WIDTH_HGT; 
				thePoint.x = rcBounds.right() - offsetX;
			}
						
		}
		else
		{
			Dimension theoffset = new Dimension();
			Rectangle rcBounds = getHostFigure().getBounds().getCopy();
	
			getHostFigure().translateToAbsolute(rcBounds);
			getBalloon().translateToRelative(rcBounds);

			theoffset.height = - (BALLOON_Y_OFFSET + ACTION_WIDTH_HGT);
			theoffset.width = (int) (rcBounds.width * myBallonOffsetPercent);

			thePoint.x = rcBounds.x + theoffset.width;
			thePoint.y = rcBounds.y + theoffset.height;
			if (isRightDisplay() && willBalloonBeClipped(thePoint)) {
				this.setLeftHandDisplay();
				theoffset.width = (int) (rcBounds.width * myBallonOffsetPercent);
				thePoint.x = rcBounds.x + theoffset.width;

			}			
		}
		return thePoint;
	}
	private boolean willBalloonBeClipped(Point pnt) {
		Control ctrl1 = getHost().getViewer().getControl();
		if (ctrl1 instanceof FigureCanvas) {
			FigureCanvas figureCanvas = (FigureCanvas) ctrl1;
			Viewport vp = figureCanvas.getViewport();
			Rectangle vpRect = vp.getClientArea();
			Dimension dim = getBalloon().getSize();
			if ((pnt.x + dim.width) >= (vpRect.x + vpRect.width)) {
				return true;
			}
		}
		return false;
	}
	private void teardownActionBar() {
		getBalloon().removeMouseMotionListener(myMouseListener);
		getBalloon().removeMouseListener(myMouseKeyListener);
		// the feedback layer figures do not recieve mouse events
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		if (myBalloon.getParent() != null) {
			layer.remove(myBalloon);
		}
		myBalloon = null;

		this.myActionBarDescriptors.clear();
		setRightHandDisplay(); // set back to default
		
		for (Iterator iter = imagesToBeDisposed.iterator(); iter.hasNext();) {
			((Image) iter.next()).dispose();
		}
		imagesToBeDisposed.clear();

	}
	private void hideActionBalloon() {
		if (getBalloon() != null) {

			teardownActionBar();
		}

	}
	
	/**
	 * Sets a timer to show the actionbar
	 *  
	 */
	private void showActionBalloonDelayed(int theDelay) {
		if (getBalloon() == null) {
			Display.getCurrent().timerExec(theDelay,
				new ShowBalloonRunnable(getMouseMoveLocation()));
		}
	}	
	
	/**
	 * Sets a timer to remove the actionbar if currently shown.
	 */	
	private void hideActionBalloonDelayed(int theDelay) {
		if (getBalloon() != null) {
			Display.getCurrent().timerExec(theDelay, hideBalloonRunnable);
		}
	}

	/**
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();

		getHostFigure().addMouseMotionListener(this.myMouseListener);
		getHostFigure().addMouseListener(this.myMouseKeyListener);
		getHostFigure().addFigureListener(this.myOwnerMovedListener);

		((IGraphicalEditPart) getHost()).addEditPartListener(
			this.myFocusListener);
		
		if (getHost() instanceof ISurfaceEditPart) {
			setIsDisplayAtMouseHoverLocation(true);
		}
	}

	/**
	 * 
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		getHostFigure().removeMouseMotionListener(this.myMouseListener);
		getHostFigure().removeMouseListener(this.myMouseKeyListener);
		getHostFigure().removeFigureListener(this.myOwnerMovedListener);
		
		((IGraphicalEditPart) getHost()).removeEditPartListener(
			this.myFocusListener);
		hideActionBalloon();

		super.deactivate();

	}
	/**
	 * This is the default which places the actionbar to favor the right side
	 * of the shape
	 *  
	 */
	protected void setRightHandDisplay() {
		this.myBallonOffsetPercent = BALLOON_X_OFFSET_RHS;
	}
	/**
	 * Place the actionbar to favor the left had side of the shape
	 *  
	 */
	protected void setLeftHandDisplay() {
		this.myBallonOffsetPercent = BALLOON_X_OFFSET_LHS;
	}
	/**
	 * check thee right display status 
	 * @return true or false
	 */
	protected boolean isRightDisplay() {
		return (BALLOON_X_OFFSET_RHS == myBallonOffsetPercent);
	}
	
	/**
	 * sets the mouse move location
	 * @param thePoint
	 */
	protected void setMouseMoveLocation(Point thePoint)
	{
		this.myMouseMoveLocation = thePoint;
	}
	
	/**
	 * gets the mouse move location
	 * @return point
	 */
	protected Point getMouseMoveLocation()
	{
		return this.myMouseMoveLocation;
	}
		
	/**
	 * @return Returns the myMouseHoverLocation.
	 */
	protected Point getMyMouseHoverLocation() {
		return myMouseHoverLocation;
	}
	/**
	 * @param theMouseHoverLocation The myMouseHoverLocation to set.
	 */
	protected void setMyMouseHoverLocation(Point theMouseHoverLocation) {
		this.myMouseHoverLocation.setLocation( theMouseHoverLocation.x, theMouseHoverLocation.y);
	}

}
