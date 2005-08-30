/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

/**
 * An element that contains a hint, a serializable String ID, a String label,
 * and an ImageDescriptor label.  SeletableElements keep track of its parent
 * which may or may not be null and a list of children which may be empty.
 * This allows SelectableElement objects to be optionally represented in tree
 * viewers.  SelectableElements also keep track of their SelectedType, which
 * describes if the element is selected, unselected, or set to leave.
 * Therefore, SelectableElement objects typically correspond to elements in the
 * UI.
 * 
 * <P>This class contains public convenience methods.  For eaxmple, it
 * includes methods to find SelectableElement objects or their hints based on
 * their String ID and a method to make copies of SelectableElement objects. 
 * 
 * <P>The SelectableElement class is used in at least 3 places, Show Related
 * Elements Show Hide Relationships, and Browse Diagrams.
 * 
 * @author Wayne Diu, wdiu
 */
public class SelectableElement {

	/**
	 * Unique identifier for this selectable element.
	 */
	private String id;

	/**
	 * String name of the element
	 */
	private String name;

	/**
	 * Icon for the element
	 */

	private ImageDescriptor icon;

	/**
	 * This element's children
	 */
	private Vector children;

	/**
	 * True if element was checked by the user, false if it wasn't
	 */
	private SelectedType selectedType;

	/**
	 * This element's parent
	 */
	private SelectableElement parent;

	/**
	 * Hint for checking what the type of this element is
	 */
	private Object hint;

	/**
	 * Adds a child to this element
	 * 
	 * @param element
	 *            the child to add
	 */
	public void addChild(SelectableElement element) {
		children.add(element);
		element.setParent(this);
	}

	/**
	 * Remove all children of this SelectableElement
	 */
	public void removeAllChildren() {
		Iterator i = children.iterator();
		while (i.hasNext()) {
			((SelectableElement) i.next()).setParent(null);
		}
		children = new Vector();
	}

	/**
	 * Constructor to make a new SelectableElement
	 * 
	 * @param aName
	 *            the String name of the element
	 * @param anIcon
	 *            the icon Image for the element
	 * @param aHint
	 *            the element type
	 * @deprecated Use the other constructor.
	 */
	public SelectableElement(String aName, ImageDescriptor anIcon, Object aHint) {
		// For now, we will use the name as a unique identifier.
		this(aName, aName, anIcon, aHint);
	}

	/**
	 * Constructor to make a new SelectableElement.
	 * 
	 * @param anID
	 *            A non-language specific unique identifier for this selectable
	 *            element.
	 * @param aName
	 *            A user-presentable name for this selectable element.
	 * @param anIcon
	 *            The icon image for the element.
	 * @param aHint
	 *            A hint associated with the selection of this element.
	 */
	public SelectableElement(String anID, String aName, ImageDescriptor anIcon,
			Object aHint) {
		children = new Vector();
		this.id = anID;
		this.name = aName;
		this.icon = anIcon;
		this.hint = aHint;
		selectedType = SelectedType.UNSELECTED;
	}

	/**
	 * Returns the number of children
	 * 
	 * @return int with the number of children this element has
	 */
	public int getNumberOfChildren() {
		return children.size();
	}

	/**
	 * Returns a child
	 * 
	 * @param i
	 *            with the index of the child of this element
	 * @return SelectableElement which is child i of this element
	 */
	public SelectableElement getChild(int i) {
		assert (i >= 0 && i < children.size());
		return (SelectableElement) children.get(i);
	}

	/**
	 * Returns the icon of this element to display to the user.
	 * 
	 * @return Image with icon of this element to display to the user
	 */
	public ImageDescriptor getIconImageDescriptor() {
		return icon;
	}

	/**
	 * Returns the name of this element to display to the user.
	 * 
	 * @return String with name of this element to display to the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the icon of this element to display to the user.
	 * 
	 * @param anIcon
	 *            The icon to set
	 */
	public void setIconImageDescriptor(ImageDescriptor anIcon) {
		this.icon = anIcon;
	}

	/**
	 * Sets the name of this element to display to the user.
	 * 
	 * @param aName
	 *            The name to set
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Returns the parent of this element.
	 * 
	 * @return SelectableElement
	 */
	public SelectableElement getParent() {
		return parent;
	}

	/**
	 * Sets the parent of this element.
	 * 
	 * @param aParent
	 *            The parent to set
	 */
	public void setParent(SelectableElement aParent) {
		this.parent = aParent;
	}

	/**
	 * Returns the children of this element as an array
	 * 
	 * @return SelectableElement[] array of this element's children.
	 */
	public SelectableElement[] getChildren() {
		SelectableElement elements[] = new SelectableElement[getNumberOfChildren()];
		for (int i = 0; i < getNumberOfChildren(); i++) {
			elements[i] = (SelectableElement) children.elementAt(i);
		}
		return elements;
	}

	/**
	 * Returns if the element was selected.
	 * 
	 * @return selectedType from the SelectedType EnumeratedType
	 */
	public SelectedType getSelectedType() {
		return selectedType;
	}

	/**
	 * Sets whether or not the element is selected. For example, if the element
	 * is checked in the interface.
	 * 
	 * @param aSelectedType
	 *            from the SelectedType EnumeratedType
	 */
	public void setSelectedType(SelectedType aSelectedType) {
		this.selectedType = aSelectedType;
	}

	/**
	 * Returns the hint, which is an Object. This could be subclassed if type
	 * safety is required.
	 * 
	 * @return the element type
	 */
	public Object getHint() {
		return hint;
	}

	/**
	 * Sets the SelectedType for a SelectableElement and its children
	 * 
	 * @param parent
	 *            sets the SelectedType for this SelectableElement
	 * @param selectedType
	 *            the SelectedType to set for the SelectableElement and its
	 *            children.
	 */
	public static void setSelectedTypeForSelecteableElementAndChildren(
			SelectableElement parent, SelectedType selectedType) {
		for (int i = 0; i < parent.getNumberOfChildren(); i++) {
			setSelectedTypeForSelecteableElementAndChildren(parent.getChild(i),
				selectedType);
		}
		parent.setSelectedType(selectedType);
	}

	/**
	 * Sets the SelectedType for a SelectableElement and its children that match
	 * the IDs in the List of IDs.
	 * 
	 * @param parent
	 *            sets the SelectedType for this SelectableElement
	 * @param selectedType
	 *            the SelectedType to set for the SelectableElement and its
	 *            children.
	 * @param list
	 *            List of IDs, not hints
	 */
	public static void setSelectedTypeForMatchingSelecteableElementAndChildren(
			SelectableElement parent, SelectedType selectedType, List list) {
		for (int i = 0; i < parent.getNumberOfChildren(); i++) {
			setSelectedTypeForMatchingSelecteableElementAndChildren(parent
				.getChild(i), selectedType, list);
		}

		if (list.contains(parent.getId())) {
			setSelectedTypeForSelecteableElementAndChildren(parent,
				selectedType);
		}

	}

	/**
	 * Calculates the longest string length of this element's children for the
	 * text that will be displayed in the control. This method only works for
	 * root SelectableElements.
	 * 
	 * @param selectableElement
	 *            the SelectableElement to calculate the longest string length.
	 *            Also looks at its children.
	 * @param control
	 *            the control with the font to use when calculating the font
	 *            size
	 * @return int with the string length in pixels
	 */
	public static int calculateLongestStringLength(
			SelectableElement selectableElement, Control control) {
		int INITIAL_LONGEST_STRING_LENGTH = 0;
		int INITIAL_ITERATION_LEVEL = -1; //there is a fake element

		assert (selectableElement.getParent() == null);

		GC gc = new GC(control);
		int longestStringLength = calculateLongestStringLength(
			selectableElement, gc, INITIAL_LONGEST_STRING_LENGTH,
			INITIAL_ITERATION_LEVEL);
		gc.dispose();
		return longestStringLength;
	}

	/**
	 * Calculates the longest string length of this element's children for the
	 * text that will be displayed.
	 * 
	 * @param selectableElement
	 *            the SelectableElement to calculate the longest string length.
	 *            Also looks at its children.
	 * @param gc
	 *            the GC to use when calculating the font size
	 * @param longestStringLength
	 *            the longest string length for the selectableElement and its
	 *            children so far.
	 * @param iterationLevel
	 *            how many levels we have gone to keep track of the indents of
	 *            the icons
	 * @return int with the string length in pixels
	 */
	private static int calculateLongestStringLength(
			SelectableElement selectableElement, GC gc,
			int longestStringLength, int iterationLevel) {
		int ICON_WIDTH = 32;
		//we don'internationalize these icons, checkbox is 16 and image is 16
		Point size = gc.textExtent(selectableElement.getName());
		if (size.x + (iterationLevel * ICON_WIDTH) > longestStringLength)
			longestStringLength = size.x + (iterationLevel * ICON_WIDTH);
		for (int i = 0; i < selectableElement.getNumberOfChildren(); i++) {
			longestStringLength = calculateLongestStringLength(
				selectableElement.getChild(i), gc, longestStringLength,
				iterationLevel + 1);
		}
		return longestStringLength;
	}

	/**
	 * Returns the number of children including itself. Includes children that
	 * are children of children, etc.
	 * 
	 * @param selectableElement
	 *            the SelectableElement that we will find the number of children
	 *            for.
	 * @return int the number of children including children that are children
	 *         of children, etc, and itself.
	 */
	public static int calculateNumberOfChildren(
			SelectableElement selectableElement) {
		int numberOfChildren = 0;
		for (int i = 0; i < selectableElement.getNumberOfChildren(); i++) {
			numberOfChildren += calculateNumberOfChildren(selectableElement
				.getChild(i));
		}
		return numberOfChildren + 1;
	}

	/**
	 * Recursively checks SelectableElement and its children to determine which
	 * elements have the specified selectedType. If a SelectableElement meets
	 * the type criteria, its hint (RelationshipType) is added to the
	 * matchingElements list.
	 * 
	 * @param matchingElements
	 * @param typeToMatch
	 */
	private void getMatchingElementTypes(List matchingElements,
			SelectedType typeToMatch) {

		for (int i = 0; i < getNumberOfChildren(); i++) {
			getChild(i).getMatchingElementTypes(matchingElements, typeToMatch);
		}
		if (getSelectedType() == typeToMatch && getHint() != null) {
			if (getHint() instanceof Collection)
				matchingElements.addAll((Collection) getHint());
			else
				matchingElements.add(getHint());
		}
	}

	/**
	 * Returns a list of SELECTED RelationshipTypes for a SelectableElement.
	 * 
	 * Checks this SelectableElement and the SelectableElement's children. For
	 * each SelectableElement where the selectedType is SELECTED, add the
	 * RelationshipType to a list.
	 * 
	 * @return List
	 */
	public List getSelectedElementTypes() {
		List selectedElements = new Vector();
		getMatchingElementTypes(selectedElements, SelectedType.SELECTED);
		return selectedElements;
	}

	/**
	 * Returns a list of UNSELECTED RelationshipTypes for a SelectableElement.
	 * 
	 * Checks this SelectableElement and the SelectableElement's children. For
	 * each SelectableElement where the selectedType is UNSELECTED, add the
	 * RelationshipType to a list.
	 * 
	 * @return List
	 */
	public List getUnSelectedElementTypes() {
		List unselectedElements = new Vector();
		getMatchingElementTypes(unselectedElements, SelectedType.UNSELECTED);
		return unselectedElements;
	}

	/**
	 * Returns a list of LEAVE RelationshipTypes for a SelectableElement.
	 * 
	 * Checks this SelectableElement and the SelectableElement's children. For
	 * each SelectableElement where the selectedType is LEAVE, add the
	 * RelationshipType to a list.
	 * 
	 * @return List
	 */
	public List getLeaveElementTypes() {
		List leaveElements = new Vector();
		getMatchingElementTypes(leaveElements, SelectedType.LEAVE);
		return leaveElements;
	}

	/**
	 * Returns a string representation of this selectable element. This is
	 * useful if a selectable element must be persisted between invocations of
	 * eclipse.
	 * 
	 * @return String id of this selectableElement
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retrieves all of the hints from the list of selections. The selections
	 * are strings that have been produced by the getId() method. Note: this
	 * should be invoked from the selectable element root.
	 * 
	 * @param stringRepresentations
	 *            Strings produced by the {@link SelectableElement#getId()}
	 *            method.
	 * @param hints
	 *            (out) A set used to store all of the hints.
	 */
	public void getHints(List stringRepresentations, Set hints) {
		// If we have hit a string representation that is fully selected
		//  then we simply retrieve all of the hints for this subtree. For
		//  this selectable element to have been in the "SELECTED" state, it
		//  and all of its children were selected.
		if (stringRepresentations.contains(getId())) {
			getAllHints(hints);
		} else {
			for (Iterator i = children.iterator(); i.hasNext();) {
				((SelectableElement) i.next()).getHints(stringRepresentations,
					hints);
			}
		}
	}

	/**
	 * Retrieve all hints for the subtree rooted at this selectable element.
	 * 
	 * @param hints
	 *            (out) A set used to store all of the hints.
	 */
	public void getAllHints(Set hints) {
		if (hint instanceof List)
			hints.addAll((List) hint);
		else if (hint != null)
			hints.add(hint);
		for (Iterator i = children.iterator(); i.hasNext();) {
			((SelectableElement) i.next()).getAllHints(hints);
		}
	}

	/**
	 * Recursively add this SelectableElement's and this SelectableElement's
	 * children's hints to a List which is not null.
	 * 
	 * It will not add duplicates into the List, and if the hint is null, it
	 * will not be added to the List.
	 * 
	 * @param list
	 *            not null, add hints to this List
	 * @param selectableElement
	 *            recursively add hints from this SelectableElement and its
	 *            children
	 */
	public static void addHintsToList(List list,
			SelectableElement selectableElement) {
		assert null != list;
		assert null != selectableElement;
		for (int i = 0; i < selectableElement.getNumberOfChildren(); i++) {
			addHintsToList(list, selectableElement.getChild(i));
		}

		Object hint = selectableElement.getHint();
		if (hint != null) {
			//I disagree that hint should be sometimes a List, and sometimes an
			//element, but I will support it
			if (hint instanceof List) {
				Iterator it = ((List) hint).iterator();
				while (it.hasNext()) {
					Object nestedHint = it.next();
					if (!list.contains(nestedHint)) {
						list.add(nestedHint);
					}
				}
			} else if (!list.contains(hint))
				list.add(hint);
		}
	}

	/**
	 * Returns if all children have the same selected type
	 * 
	 * @return true if all children are checked, false otherwise
	 * @param parent
	 *            we'll be checking the children of this parent
	 * @param selectType
	 *            the SelectedType that all children of the parent are checked
	 *            for
	 */
	public static boolean doAllChildrenHaveSelectedType(
			SelectableElement parent, SelectedType selectType) {
		for (int i = 0; i < parent.getNumberOfChildren(); i++) {
			if (parent.getChild(i).getSelectedType() != selectType)
				return false;
		}
		return true;
	}

	/**
	 * Return all children that have the SelectedType.
	 * 
	 * @param parent
	 *            parent selectable element.
	 * @param selectType
	 *            the selected type to match
	 * @param list
	 *            of SelectableElements
	 */
	public static void getAllChildrenOfType(SelectableElement parent,
			SelectedType selectType, List list) {
		assert null != list;

		for (int i = 0; i < parent.getNumberOfChildren(); i++) {
			if (parent.getChild(i).getSelectedType() == selectType) {
				list.add(parent.getChild(i));
			}

			getAllChildrenOfType(parent.getChild(i), selectType, list);
		}
	}

	/**
	 * Return element IDs, including children, that are SelectedType.SELECTED.
	 * 
	 * @return List of element IDs, including children, that are
	 *         SelectedType.SELECTED.
	 */
	public List getSelectedElementIds() {
		List list = new ArrayList();
		getMatchingElementIds(list, SelectedType.SELECTED);
		return list;
	}

	/**
	 * Return matching element IDs that match the typeToMatch.
	 * 
	 * @param matchingIds
	 *            List of String ids we are filling
	 * @param typeToMatch
	 *            going to match this type
	 */
	private void getMatchingElementIds(List matchingIds,
			SelectedType typeToMatch) {

		for (int i = 0; i < getNumberOfChildren(); i++) {
			getChild(i).getMatchingElementIds(matchingIds, typeToMatch);
		}
		if (getSelectedType() == typeToMatch) {
			matchingIds.add(getId());
		}
	}

	/**
	 * Same idea as the clone method. Share the same images, hints, etc. of the
	 * original. Just have different SelectableElements.
	 * 
	 * @return a copy of this selectableElement
	 */
	public SelectableElement makeCopy() {
		SelectableElement selectableElement = immediateCopy(this);
		copyChildren(this, selectableElement);
		return selectableElement;
	}

	/**
	 * Used by the copy method. Not suprisingly, this returns a copy of the src
	 * 
	 * @param src
	 *            children will be copied from here
	 * @return SelectableElement which is a copy of the src
	 */
	private SelectableElement immediateCopy(SelectableElement src) {
		SelectableElement selectableElement = new SelectableElement(
			src.getId(), src.getName(), src.getIconImageDescriptor(), src
				.getHint());
		selectableElement.setSelectedType(src.getSelectedType());
		return selectableElement;
	}

	/**
	 * Used by the copy method. Not surprisingly, this copies the children of
	 * src into dest.
	 * 
	 * @param src
	 *            children will be copied from here
	 * @param dest
	 *            children are copied into here
	 */
	private void copyChildren(SelectableElement src, SelectableElement dest) {
		for (int i = 0; i < src.getNumberOfChildren(); i++) {
			dest.addChild(immediateCopy(src.getChild(i)));
			copyChildren(src.getChild(i), dest.getChild(i));
		}
	}

	/**
	 * Collect all the hints of the children into a list.
	 * 
	 * @param list
	 *            that I am collecting the hints into.
	 */
	private void collectChildrenHints(List list) {
		Object aHint = getHint();
		if (aHint instanceof List) {
			Iterator it = ((List) aHint).iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (!list.contains(obj))
					list.add(obj);
			}

		} else if (aHint != null) {
			if (!list.contains(aHint))
				list.add(aHint);
		}

		for (int i = 0; i < getChildren().length; i++) {
			getChild(i).collectChildrenHints(list);
		}
	}

	/**
	 * Collect the types that match a list of String ids. Unlike getHints, which
	 * doesn't collect children hints.
	 * 
	 * @param list
	 *            List to add into
	 * @param ids
	 *            List of String ids we are trying to match
	 */
	public void getHintsThatMatchTheseIds(List list, List ids) {
		for (int i = 0; i < getNumberOfChildren(); i++) {
			if (ids.contains(getChild(i).getId())) {
				getChild(i).collectChildrenHints(list);
			} else {
				getChild(i).getHintsThatMatchTheseIds(list, ids);
			}
		}
	}

	/**
	 * Return the first element that matches the given id from the List of
	 * SelectableElement objects
	 * 
	 * @param selectableElements
	 *            List of SelectableElement objects to match
	 * @param id
	 *            String id to match
	 * @return the first element that matches the given id from the List of
	 *         SelectableElement objects or null if nothing matched
	 */
	public static SelectableElement findById(List selectableElements, String id) {
		assert null != selectableElements;
		Iterator it = selectableElements.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			assert (obj instanceof SelectableElement);

			SelectableElement selectableElement = (SelectableElement) obj;
			if (id.equals(selectableElement.getId())) {
				return selectableElement;
			}
		}

		return null;
	}

	/**
	 * Return the first element that matches the given id from this
	 * SelectableElement and its children
	 * 
	 * @param theId
	 *            String id to match
	 * @return the first element that matches the given id from this
	 *         SelectableElement and its children or null if nothing matched
	 */
	public SelectableElement findById(String theId) {

		assert null != theId;
		if (theId.equals(this.getId())) {
			return this;
		}

		for (int i = 0; i < getNumberOfChildren(); i++) {
			SelectableElement element = getChild(i).findById(theId);
			if (element != null)
				return element;
		}

		return null;
	}

}