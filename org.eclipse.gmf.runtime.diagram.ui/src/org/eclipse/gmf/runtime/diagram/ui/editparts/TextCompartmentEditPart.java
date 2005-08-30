/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelDirectEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ISemanticParser;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.gef.ui.internal.requests.DirectEditRequestWrapper;
import org.eclipse.gmf.runtime.gef.ui.parts.TextCellEditorEx;
import org.eclipse.gmf.runtime.gef.ui.parts.WrapTextCellEditor;
import com.ibm.xtools.notation.FontStyle;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.View;

/*
 * @canBeSeenBy %partners
 */
/**
 * yhe conroller for hte text compartment
 * @author mmostafa
 *
 */
public class TextCompartmentEditPart extends CompartmentEditPart {
	
	/**
	 * the text cell editor locator
	 * @author mmostafa
	 *
	 */
	public class TextCellEditorLocator implements CellEditorLocator {

		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);
			
			if (getLabel().isTextWrapped() && getLabel().getText().length() > 0)
				rect.setSize(new Dimension(text.computeSize(rect.width, SWT.DEFAULT)));
			else {
				int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr*2, 0));
			}

			if (!rect.equals(new Rectangle(text.getBounds())))
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}

	}
	/** the direct edit manager for text editing */
	private DirectEditManager manager;
	/** the text parser */
	protected IParser parser;
	/** the text parser options */
	private ParserOptions parserOptions;
	/** the element to listen to as suggested by the parser*/
	private List parserElements = null;
	/** the number of icons in the text label */
	private int numIcons = 0;

	/** Label that is displayed as the tooltip. */
	private Label toolTipLabel = new Label();

	/**
	 * coinstructor
	 * @param view the view controlled by this edit part
	 */
	public TextCompartmentEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
			EditPolicy.DIRECT_EDIT_ROLE,
			new LabelDirectEditPolicy());
		// Text Compartment do not handle creation request for views
		removeEditPolicy(EditPolicyRoles.CREATION_ROLE);
		
	}

	/**
	 * @return WrapLabelFigure
	 */
	protected IFigure createFigure() {
		return createWrapLabel();
	}

	/**
	 * @return WrapLabel, the created wrap label
	 */
	protected WrapLabel createWrapLabel() {
		WrapLabel label = new WrapLabel(""); //$NON-NLS-1$
		label.setLabelAlignment(PositionConstants.TOP);
		label.setTextAlignment(PositionConstants.TOP);
		return label;
	}

	
	/**
	 * Use getLabel() instead if you which to get the WrapLabel
	 * for the the text compartent
	 * @see org.eclipse.gef.GraphicalEditPart#getFigure()
	 */
	public IFigure getFigure() {
		return super.getFigure();
	}
	
	/**
	 * This should be used instead of getFigure() to get the figure
	 * @return Return the WrapLabel for the TextCompartment 
	 */
	public WrapLabel getLabel() {
		return (WrapLabel) getFigure();
	}

	/**
	 * gets the label Icon for this edit part
	 * @param index the index to use
	 * @return Image
	 */
	protected Image getLabelIcon(int index) {
		return null;
	}

	/**
	 * gets the label text
	 * @return the lebel text
	 */
	protected String getLabelText() {
		EObject element = resolveSemanticElement();
		return (element == null) ? null
			: (getParser() == null) ? null
				: getParser().getPrintString(new EObjectAdapter(element),
					getParserOptions().intValue());
	}

	/**
	 * gets the edit text
	 * @return the edit text
	 */
	public String getEditText() {
		EObject element = resolveSemanticElement();
		return (element == null) ? "" //$NON-NLS-1$
		: getParser().getEditString(
			new EObjectAdapter(element),
			getParserOptions().intValue());
	}
	
	/**
	 * get the completion processor
	 * @return IContentAssistProcessor 
	 */
	public IContentAssistProcessor getCompletionProcessor() {
		EObject element = resolveSemanticElement();
		if (element != null) {
			return getParser().getCompletionProcessor(new EObjectAdapter(element));
		}
		return null;
	}

	private boolean canParse() {
		return getEditText() != null;
	}

	/**
	 * Returns a validator for the user's edit text
	 * @return a validator
	 */
	public ICellEditorValidator getEditTextValidator() {
		return new ICellEditorValidator() {
			public String isValid(final Object value) {
				if (value instanceof String) {
					final EObject element = resolveSemanticElement();

					final IParserEditStatus isValid[] = { null };
					final IParser theParser = getParser();
					MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {
							public Object run() {
								isValid[0] =
								    theParser.isValidEditString(
										new EObjectAdapter(element),
										(String) value);
								return null;
							}
						});

					return isValid[0].getCode() == ParserEditStatus.EDITABLE ? null : isValid[0].getMessage();
				}

				// shouldn't get here
				return null;
			}
		};
	}

	/**
	 * Gets the parser options. The result is passed as a parameter to the 
	 * parser's getPrintString() and isAffectingEvent() methods
	 * @return ParserOptions the parser options
	 */
	public final ParserOptions getParserOptions() {
		if (parserOptions == null)
			parserOptions = buildParserOptions();
		return parserOptions;
	}

	/**
	 * Builds the parser options. 
	 * @return ParserOptions the parser options
	 */
	protected ParserOptions buildParserOptions() {
		return ParserOptions.NONE;
	}

	/**
	 * Builds the parser options.
	 */
	protected final void refreshParserOptions() {
		parserOptions = buildParserOptions();
	}

	/**
	 * Determines if the given event affects the paser options
	 * 
	 * @param evt The event in question
	 * @return whether the given event affects the parser options
	 */
	protected boolean isAffectingParserOptions(PropertyChangeEvent evt) {
		return false;
	}

	/**
	 * Method getLabelToolTip.
	 * @return IFigure
	 */
	protected IFigure getLabelToolTip() {
		String text = getToolTipText();
		if (text != null && text.length() > 0) {
			toolTipLabel.setText(text);
			return toolTipLabel;
		}
		return null;
	}

	/**
	 * This method can be overridden in the subclass to return
	 * text for the tooltip.  
	 * @return String the tooltip
	 */
	protected String getToolTipText() {
		return null;
	}

	/**
	 * check if this edit part is editable or not
	 * @return true or false
	 */
	protected boolean isEditable() {
		EObject element = resolveSemanticElement();
		if (element != null && canParse()) {
			return true;
		}
		return false;
	}

	/**
	 * performas direct edit
	 */
	protected void performDirectEdit() {
		getManager().show();
	}

	/**
	 * Performs direct edit and will initiate another mouse click 
	 * event so that the cursor will appear under the mouse
	 * 	 
	 *  @param eventLocation
	 */
	protected void performDirectEdit(Point eventLocation) {
		if (getManager().getClass() == TextDirectEditManager.class) {			
			((TextDirectEditManager) getManager()).show(eventLocation.getSWTPoint());
		}
	}
	
	/**
	 * 
	 * Performs direct edit setting the initial text to be the initialCharacter
	 * 
	 * @param initialCharacter
	 */
	private void performDirectEdit(char initialCharacter) {
		// Run the TextDirectEditManager show with the initial character
		// This will not send an extra mouse click
		if (getManager() instanceof TextDirectEditManager) {									
			((TextDirectEditManager) getManager()).show(initialCharacter);
		} else {
			performDirectEdit();
		}
	}
	
	/**
	 * 
	 * Performs direct edit request based on request type
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#performDirectEditRequest(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected void performDirectEditRequest(Request request) {

		final Request theRequest = request;

		MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {
				public Object run() {
					if (isActive() && isEditable()) {

						// IF the direct edit request has an initial character...
						if (theRequest instanceof DirectEditRequestWrapper) {							
							char initialCharacter = ((DirectEditRequestWrapper) theRequest)
							.getInitialCharacter();							
							performDirectEdit(initialCharacter);
						} else if ((theRequest instanceof DirectEditRequest) && (getEditText().equals(getLabelText()))) {
							DirectEditRequest editRequest = (DirectEditRequest) theRequest;
							performDirectEdit(editRequest.getLocation());
						} else { // Some other Request
							performDirectEdit();
						}
					}
					
					return null;
				}
			});
	}

	protected void handleNotificationEvent(NotificationEvent event) {
		if (getParser() != null
			&& getParser().isAffectingEvent(event.getNotification(),
				getParserOptions().intValue())) {
			refreshLabel();
			return;
		}
		if (getParser() instanceof ISemanticParser) {
			ISemanticParser modelParser = (ISemanticParser) getParser();
			if (modelParser.areSemanticElementsAffected(null,
					event.getNotification())) {
				removeSemanticListeners();
				if (resolveSemanticElement() != null)
					addSemanticListeners();
				refreshLabel();
				return;
			}
		}
		super.handleNotificationEvent(event);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Properties.ID_FONTCOLOR)){
			Integer c = (Integer) evt.getNewValue();
			setFontColor(PresentationResourceManager.getInstance().getColor(c));
		}
		else if (evt.getPropertyName().equals(Properties.ID_FONTUNDERLINE))
			refreshUnderline();
		else if (evt.getPropertyName().equals(Properties.ID_FONTSTRIKETHROUGH))
			refreshStrikeThrough();
		else if (isAffectingParserOptions(evt)) {
			refreshParserOptions();
			refreshLabel();
		} else
			super.handlePropertyChangeEvent(evt);
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshParserOptions();
		refreshLabel();
		refreshUnderline();
		refreshStrikeThrough();
		refreshFontColor();
	}

	protected void setFontColor(Color color) {
		getLabel().setForegroundColor(color);
	}

	protected void addNotationalListeners() {
		super.addNotationalListeners();
		addListenerFilter("PrimaryView", this, ViewUtil.getPropertyChangeNotifier(getPrimaryView())); //$NON-NLS-1$
	}

	protected void addSemanticListeners() {
		if (getParser() instanceof ISemanticParser) {
			EObject semanticElement = resolveSemanticElement();
			parserElements =
				((ISemanticParser) getParser()).getSemanticElementsBeingParsed(semanticElement);

			for (int i = 0; i < parserElements.size(); i++)
				addListenerFilter("SemanticModel" + i, this, PresentationListener.getNotifier((EObject)parserElements.get(i))); //$NON-NLS-1$

		} else 
			super.addSemanticListeners();
	}

	protected void removeNotationalListeners() {
		super.removeNotationalListeners();
		removeListenerFilter("PrimaryView"); //$NON-NLS-1$
	}

	protected void removeSemanticListeners() {
		if (parserElements != null) {
			for (int i = 0; i < parserElements.size(); i++)
				removeListenerFilter("SemanticModel" + i); //$NON-NLS-1$
		} else
			super.removeSemanticListeners();
	}

	/**
	 * getter for the Num Icons
	 * @return num icons
	 */
	public int getNumIcons() {
		return numIcons;
	}

	/**
	 * setter for the num icons
	 * @param numIcons
	 */
	public void setNumIcons(int numIcons) {
		this.numIcons = numIcons;
	}

	protected List getModelChildren() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Method getParser.
	 * @return IParser
	 */
	public IParser getParser() {
		if (parser == null) {
			String parserHint = ((View)getModel()).getType();
			EObject element = resolveSemanticElement();
			if (element != null) {
				ParserHintAdapter hintAdapter =
					new ParserHintAdapter(element, parserHint);
				parser = ParserService.getInstance().getParser(hintAdapter);
			}
		}
		return parser;
	}

	/**
	 * Will update the tool tip text for the figure and also the icons for the label.  In additional
	 * it will apply any font constraints to the label based on the type of Text Compartment we
	 * are dealing with.
	 * Any body overriding this method should either can this super.refreshLabel() or
	 * call applyFontContraintsToLabel() to ensure the the proper font constraints are apply to
	 * the label.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart#refreshLabel()
	 */
	protected void refreshLabel() {
		// refreshes the label text
		getLabel().setText(getLabelText());

		// refreshes the label icon(s)
		for (int i = 0; i < numIcons; i++)
			getLabel().setIcon(getLabelIcon(i), i);

		// refreshes the label tool tip
		getLabel().setToolTip(getLabelToolTip());
	}

	/**
	 * Refreshes the font underline property
	 */
	protected void refreshUnderline() {
		FontStyle style = (FontStyle) getPrimaryView().getStyle(NotationPackage.eINSTANCE.getFontStyle());
		if (style != null)
			getLabel().setTextUnderline(style.isUnderline());
	}

	/**
	 * Refreshes the font underline property
	 */
	protected void refreshStrikeThrough() {
		FontStyle style = (FontStyle) getPrimaryView().getStyle(NotationPackage.eINSTANCE.getFontStyle());
		if (style != null)
			getLabel().setTextStrikeThrough(style.isStrikeThrough());
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()
	 */
	protected AccessibleEditPart getAccessibleEditPart() {
		if (accessibleEP == null)
			accessibleEP = new AccessibleGraphicalEditPart() {
			public void getName(AccessibleEvent e) {
				IFigure fig = getFigure();
				if (fig instanceof WrapLabel) {
					e.result = ((WrapLabel)fig).getText();
				}
			}
		};
		return accessibleEP;
	}

	/**
	 * There is no children to text compartments 
	 * 
	 * @param semanticHint
	 * @return IGraphicalEditPart
	 */
	public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
		return null;
	}
	/**
	 * @return Returns the manager.
	 *
	 */
	protected DirectEditManager getManager() {
		if (manager == null)
			setManager(
				new TextDirectEditManager(
					this,
					getLabel().isTextWrapped()
						? WrapTextCellEditor.class
						: TextCellEditorEx.class,
					new TextCellEditorLocator()));
		return manager;
	}
	/**
	 * @param manager The manager to set.
	 * 
	 */
	protected void setManager(DirectEditManager manager) {
		this.manager = manager;
	}
	
	/**
	 * gets the primary child view for this edit part, this is usually used
	 * by direct edit requests, to see where the edit will happen
	 * @return <code>View</code>
	 */
	public View getPrimaryChildView(){
		if (getModel()!=null){
			View view = (View)getModel();
			return ViewUtil.getChildBySemanticHint(view,CommonParserHint.DESCRIPTION);
		}
		return null;
	}
}
