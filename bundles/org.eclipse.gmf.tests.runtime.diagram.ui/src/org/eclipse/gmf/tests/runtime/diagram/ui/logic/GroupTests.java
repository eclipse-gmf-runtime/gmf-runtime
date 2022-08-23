/******************************************************************************
 * Copyright (c) 2007, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ArrangeAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.GroupAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SelectAllAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.UngroupAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.IExpandableFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.type.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestActionCallback;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for group and ungroup functionality.
 * 
 * @author crevells
 */
public class GroupTests
    extends AbstractTestBase {

    public static Test suite() {
        TestSuite s = new TestSuite(GroupTests.class);
        return s;
    }

    public GroupTests() {
        super("Group Tests");//$NON-NLS-1$
    }

    public class GroupTestFixture
        extends LogicTestFixture {

        protected void createShapesAndConnectors()
            throws Exception {
            // do nothing, each test will create the shapes it wants
        }
    }

    protected void setTestFixture() {
        testFixture = new GroupTestFixture();
    }

    protected LogicTestFixture getFixture() {
        return (LogicTestFixture) testFixture;
    }

    /** the shape in the north-east */
    protected View neView;

    /** the shape in the north-west */
    protected View nwView;

    /** the shape in the south-east */
    protected View seView;

    /** the shape in the south-west */
    protected View swView;

    /**
     * Create the four shapes to be used to test grouping.
     */
    protected void setupShapes() {

        IElementType CIRCUIT_TYPE = ElementTypeRegistry.getInstance().getType(
            "logic.circuit"); //$NON-NLS-1$

        ShapeEditPart nwCircuitEP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(10, 10), new Dimension(50, 50),
            getContainerEP());

        ShapeEditPart neNoteEP = getFixture().createShapeUsingTool(
            DiagramNotationType.NOTE, new Point(100, 10),
            new Dimension(50, 50), getContainerEP());

        ShapeEditPart swGeoshapeEP = getFixture().createShapeUsingTool(
            DiagramNotationType.NOTE, new Point(10, 100),
            new Dimension(50, 50), getContainerEP());

        ShapeEditPart seCircuitEP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(100, 100), new Dimension(50, 50),
            getContainerEP());

        flushEventQueue();

        // Cache the views so we can find the editparts again later.
        nwView = (View) nwCircuitEP.getModel();
        neView = (View) neNoteEP.getModel();
        swView = (View) swGeoshapeEP.getModel();
        seView = (View) seCircuitEP.getModel();

        // Create some connections just to make things more complicated.
        getFixture().createConnectorUsingTool(swGeoshapeEP, nwCircuitEP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(neNoteEP, seCircuitEP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(neNoteEP, swGeoshapeEP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(swGeoshapeEP, seCircuitEP,
            GeoshapeType.LINE);

        flushEventQueue();
    }

    protected IGraphicalEditPart getContainerEP() {
        return getDiagramEditPart();
    }

    protected ShapeEditPart getNWShape() {
        return (ShapeEditPart) findEditPart(nwView);
    }

    protected ShapeEditPart getNEShape() {
        return (ShapeEditPart) findEditPart(neView);
    }

    protected ShapeEditPart getSWShape() {
        return (ShapeEditPart) findEditPart(swView);
    }

    protected ShapeEditPart getSEShape() {
        return (ShapeEditPart) findEditPart(seView);
    }

    protected void setupShapesAndGroups() {
        setupShapes();

        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());

        GroupEditPart groupEP = groupShapes(shapes);

        shapes.clear();
        shapes.add(groupEP);
        shapes.add(getNEShape());

        groupShapes(shapes);

        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(2, getInnerGroupEP().getChildren().size());
    }

    protected GroupEditPart getInnerGroupEP() {
        return (GroupEditPart) getNWShape().getParent();
    }

    protected GroupEditPart getOuterGroupEP() {
        return (GroupEditPart) getNEShape().getParent();
    }

    protected GroupEditPart groupShapes(List<ShapeEditPart> editparts) {
        View childView = (View) editparts.get(0).getModel();

        GroupRequest request = new GroupRequest(ActionIds.ACTION_GROUP);
        request.setEditParts(editparts);
        Command cmd = editparts.get(0).getParent().getCommand(request);
        assertTrue(cmd.canExecute());
        getCommandStack().execute(cmd);
        flushEventQueue();

        EditPart groupEP = findEditPart(childView).getParent();
        assertTrue(groupEP instanceof GroupEditPart);
        return (GroupEditPart) groupEP;
    }

    protected Tool getSelectionToolFromPalette() {
        PaletteViewer paletteViewer = getDiagramEditPart().getViewer()
            .getEditDomain().getPaletteViewer();
        ToolEntry selectionTool = paletteViewer.getPaletteRoot()
            .getDefaultEntry();
        paletteViewer.setActiveTool(selectionTool);
        Tool tool = selectionTool.createTool();
        tool.setEditDomain((EditDomain) getDiagramWorkbenchPart()
            .getDiagramEditDomain());
        return tool;
    }

    /**
     * @return
     */
    protected IGraphicalEditPart findEditPart(View view) {
        return (IGraphicalEditPart) getDiagramEditPart().getViewer()
            .getEditPartRegistry().get(view);
    }

    protected MouseEvent createMouseEvent(int x, int y) {
        Event e = new Event();

        e.widget = getDiagramEditPart().getViewer().getControl();
        ;
        e.display = e.widget.getDisplay();
        e.button = 1; // left button
        e.x = x;
        e.y = y;

        return new MouseEvent(e);
    }

    protected MouseEvent createRightMouseEvent(int x, int y) {
        Event e = new Event();

        e.widget = getDiagramEditPart().getViewer().getControl();
        ;
        e.display = e.widget.getDisplay();
        e.button = 3; // right button
        e.x = x;
        e.y = y;

        return new MouseEvent(e);
    }

    protected Rectangle getAbsoluteBounds(IGraphicalEditPart editpart) {
        IFigure figure = editpart.getFigure();
        Rectangle bounds = (figure instanceof IExpandableFigure) ? ((IExpandableFigure) figure)
            .getExtendedBounds().getCopy()
            : figure.getBounds().getCopy();
        editpart.getFigure().translateToAbsolute(bounds);
        return bounds;
    }

    protected void assertNotEquals(Object object1, Object object2) {
        assertFalse(object1.equals(object2));
    }

    public void testGroupCommandAndUndoRedo()
        throws Exception {

        setupShapes();

        // Group the NE shape and the SE shape and test undo/redo of the action.
        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());

        GroupEditPart group1EP = groupShapes(shapes);

        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(group1EP, getSWShape().getParent());
        assertEquals(group1EP.getParent(), getContainerEP());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(getAbsoluteBounds(group1EP), getAbsoluteBounds(
            getNWShape()).union(getAbsoluteBounds(getSWShape())));

        assertTrue(getCommandStack().canUndo());
        getCommandStack().undo();
        flushEventQueue();

        assertEquals(getContainerEP(), getNWShape().getParent());
        assertEquals(getSWShape().getParent(), getContainerEP());
        assertEquals(4, getContainerEP().getChildren().size());

        assertTrue(getCommandStack().canRedo());
        getCommandStack().redo();
        flushEventQueue();

        group1EP = (GroupEditPart) getNWShape().getParent();
        assertEquals(group1EP, getSWShape().getParent());
        assertEquals(group1EP.getParent(), getContainerEP());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(getAbsoluteBounds(group1EP), getAbsoluteBounds(
            getNWShape()).union(getAbsoluteBounds(getSWShape())));

        // Group group1 and the NE shape and test undo/redo of the action.

        shapes.clear();
        shapes.add(group1EP);
        shapes.add(getNEShape());

        GroupEditPart group2EP = groupShapes(shapes);

        group1EP = (GroupEditPart) getNWShape().getParent();

        assertEquals(group1EP, getSWShape().getParent());
        assertEquals(group2EP, group1EP.getParent());
        assertEquals(group2EP, getNEShape().getParent());
        assertEquals(getContainerEP(), group2EP.getParent());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(2, group2EP.getChildren().size());
        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(getAbsoluteBounds(group2EP), getAbsoluteBounds(
            getNEShape()).union(getAbsoluteBounds(group1EP)));

        assertTrue(getCommandStack().canUndo());
        getCommandStack().undo();
        flushEventQueue();

        group1EP = (GroupEditPart) getNWShape().getParent();
        assertEquals(getContainerEP(), group1EP.getParent());
        assertEquals(getContainerEP(), getNEShape().getParent());
        assertEquals(3, getContainerEP().getChildren().size());

        assertTrue(getCommandStack().canRedo());
        getCommandStack().redo();
        flushEventQueue();

        group1EP = (GroupEditPart) getNWShape().getParent();
        group2EP = (GroupEditPart) getNEShape().getParent();
        assertEquals(group2EP, group1EP.getParent());
        assertEquals(getContainerEP(), group2EP.getParent());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(2, group2EP.getChildren().size());
        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(getAbsoluteBounds(group2EP), getAbsoluteBounds(
            getNEShape()).union(getAbsoluteBounds(group1EP)));
    }

    public void testUngroupCommandAndUndoRedo()
        throws Exception {

        setupShapesAndGroups();

        // Ungroup the top-level group.
        Request request = new Request(ActionIds.ACTION_UNGROUP);
        Command cmd = getOuterGroupEP().getCommand(request);
        assertTrue(cmd.canExecute());
        getCommandStack().execute(cmd);
        // flushEventQueue();

        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(getContainerEP(), getInnerGroupEP().getParent());
        assertEquals(getContainerEP(), getNEShape().getParent());

        // Undo the ungroup of the top-level group.
        assertTrue(getCommandStack().canUndo());
        getCommandStack().undo();

        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(getOuterGroupEP(), getInnerGroupEP().getParent());
        assertEquals(2, getOuterGroupEP().getChildren().size());

        // Redo the ungroup of the top-level group.
        assertTrue(getCommandStack().canRedo());
        getCommandStack().redo();
        // flushEventQueue();

        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(getContainerEP(), getInnerGroupEP().getParent());
        assertEquals(getContainerEP(), getNEShape().getParent());
    }

    public void testGroupIsSelectedAfterGroupAction()
        throws Exception {

        setupShapes();

        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());

        GroupAction action = new GroupAction(getWorkbenchPage());
        getWorkbenchPage().activate(getDiagramWorkbenchPart());
        action.init();

        EditPartViewer viewer = getDiagramWorkbenchPart()
            .getDiagramGraphicalViewer();
        viewer.deselectAll();
        viewer.setSelection(new StructuredSelection(shapes));

        action.refresh();
        assertTrue(action.isEnabled());
        action.run();
        action.dispose();

        flushEventQueue();

        assertEquals(1, ((StructuredSelection) viewer.getSelection()).size());
        assertTrue(((StructuredSelection) viewer.getSelection())
            .getFirstElement() instanceof GroupEditPart);
    }

	public void testGroupActionEnablement()
        throws Exception {
        setupShapes();

        GroupAction action = new GroupAction(getWorkbenchPage());
        getWorkbenchPage().activate(getDiagramWorkbenchPart());
        action.init();

        EditPartViewer viewer = getDiagramWorkbenchPart()
            .getDiagramGraphicalViewer();
        viewer.deselectAll();

        // should be disabled when there is only one shape and connections are included in the selection
        List<EditPart> selection = new LinkedList<EditPart>(getDiagramEditPart().getConnections());
        selection.add(getNWShape());
        viewer.setSelection(new StructuredSelection(selection));
        action.refresh();
        assertFalse(action.isEnabled());
        
        // should be enabled when connections are included in the selection
        selection = new LinkedList<EditPart>(getDiagramEditPart().getConnections());
        selection.addAll(getContainerEP().getChildren());
        viewer.setSelection(new StructuredSelection(selection));
        action.refresh();
        assertTrue(action.isEnabled());

        // should be disabled when only one shape is selected
        viewer.setSelection(new StructuredSelection(getNWShape()));
        action.refresh();
        assertFalse(action.isEnabled());

        // should be disabled when only connections are selected
        viewer.setSelection(new StructuredSelection(getDiagramEditPart()
            .getConnections()));
        action.refresh();
        assertFalse(action.isEnabled());

        // should be enabled on groups
        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());
        GroupEditPart groupEP = groupShapes(shapes);
        shapes.clear();
        shapes.add(groupEP);
        shapes.add(getNEShape());
        viewer.setSelection(new StructuredSelection(shapes));
        action.refresh();
        assertTrue(action.isEnabled());

        // should be disabled on uneditable diagrams
        getContainerEP().disableEditMode();
        action.refresh();
        assertFalse(action.isEnabled());
    }

    public void testUngroupActionEnablement()
        throws Exception {
        setupShapes();

        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());
        GroupEditPart group1EP = groupShapes(shapes);

        shapes.clear();
        shapes.add(getNEShape());
        shapes.add(getSEShape());
        GroupEditPart group2EP = groupShapes(shapes);

        UngroupAction action = new UngroupAction(getWorkbenchPage());
        getWorkbenchPage().activate(getDiagramWorkbenchPart());
        action.init();

        EditPartViewer viewer = getDiagramWorkbenchPart()
            .getDiagramGraphicalViewer();
        viewer.deselectAll();

        // should be enabled when one group is selected
        viewer.setSelection(new StructuredSelection(getInnerGroupEP()));
        action.refresh();
        assertTrue(action.isEnabled());

        // should be enabled when multiple groups are selected
        List<ShapeEditPart> groups = new LinkedList<ShapeEditPart>();
        groups.add(group1EP);
        groups.add(group2EP);
        viewer.setSelection(new StructuredSelection(groups));
        action.refresh();
        assertTrue(action.isEnabled());
    }

    public void testSelectGroupWithClickAndDrag()
        throws Exception {

        setupShapesAndGroups();

        EditPartViewer viewer = getDiagramEditPart().getViewer();
        PaletteViewer paletteViewer = viewer.getEditDomain().getPaletteViewer();

        ToolEntry selectionTool = paletteViewer.getPaletteRoot()
            .getDefaultEntry();
        paletteViewer.setActiveTool(selectionTool);
        Tool tool = selectionTool.createTool();
        tool.setEditDomain((EditDomain) getDiagramWorkbenchPart()
            .getDiagramEditDomain());

        // draw rubber band around all the shapes
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        tool.mouseDown(createMouseEvent(0, 0), viewer);
        tool.mouseDrag(createMouseEvent(200, 200), viewer);
        tool.mouseUp(createMouseEvent(200, 200), viewer);
        tool.deactivate();

        // size should be 6: 1 group, 1 shape, 4 connections
        assertEquals(6, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getOuterGroupEP()));
    }

    public void testSelectGroupWithRightClick()
        throws Exception {

        setupShapesAndGroups();

        Tool tool = getSelectionToolFromPalette();

        // click on a shape in the outer group and the outer group should be
        // selected
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        Point point = getAbsoluteBounds(getNEShape()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getOuterGroupEP()));

        // right-click on the same shape a second time and the group should
        // still be
        // selected
        tool.mouseDown(createRightMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createRightMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getOuterGroupEP()));
    }

    public void testSelectShapesAndGroupsWithClick()
        throws Exception {

        setupShapesAndGroups();

        Tool tool = getSelectionToolFromPalette();

        // click on a shape in the outer group and the outer group should be
        // selected
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        Point point = getAbsoluteBounds(getNEShape()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getOuterGroupEP()));

        // click on the same shape a second time and the shape should be
        // selected
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getNEShape()));

        // right-click on the same shape and it should remain selected
        tool.mouseDown(createRightMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createRightMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getNEShape()));

        // click on a shape not in the group and that shape itself should be
        // selected
        viewer.getSelectionManager().deselectAll();
        point = getAbsoluteBounds(getSEShape()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getSEShape()));

        // click on an area of blank space in a group and nothing should be
        // selected
        viewer.getSelectionManager().deselectAll();
        point = getAbsoluteBounds(getNWShape()).getBottom();
        Point point2 = getAbsoluteBounds(getSWShape()).getTop();
        point.translate(0, (point2.y - point.y) / 2);
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertTrue(
            "should be empty unless we happened to click on one of the lines", //$NON-NLS-1$
            viewer.getSelectedEditParts().isEmpty()
                || viewer.getSelectedEditParts().get(0) instanceof ConnectionEditPart);

        // click on a shape in the inner group and the outer group should be
        // selected
        viewer.getSelectionManager().deselectAll();
        point = getAbsoluteBounds(getNWShape()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getOuterGroupEP()));

        // click on the same shape a second time and the inner group should be
        // selected
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getInnerGroupEP()));

        // click on the same shape a third time and the shape itself should be
        // selected
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getNWShape()));

        tool.deactivate();
    }

    public void testSelectGroupWithClickOddScenario()
        throws Exception {

        setupShapes();

        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());

        GroupEditPart group1EP = groupShapes(shapes);

        shapes.clear();
        shapes.add(getNEShape());
        shapes.add(getSEShape());

        GroupEditPart group2EP = groupShapes(shapes);

        shapes.clear();
        shapes.add(group1EP);
        shapes.add(group2EP);

        GroupEditPart outerGroupEP = groupShapes(shapes);

        Tool tool = getSelectionToolFromPalette();

        // click on a shape and the outer group should be
        // selected
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        Point point = getAbsoluteBounds(getNEShape()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(outerGroupEP));

        tool.deactivate();
    }

    /**
     * This ensure that when a group is created you can select a connection that
     * exists between shapes in the group. The issue was that the connection
     * editpart was not active.
     * 
     * This fails because of GEF Bugzilla 174085.
     * 
     * @throws Exception
     */
    public void testSelectConnectionInGroupWithClick()
        throws Exception {

        setupShapes();

        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());
        GroupEditPart groupEP = groupShapes(shapes);

        ConnectionEditPart connectionEP = (ConnectionEditPart) getNWShape()
            .getTargetConnections().get(0);

        Tool tool = getSelectionToolFromPalette();
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();

        Point point = getAbsoluteBounds(connectionEP).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertEquals(connectionEP, viewer.getSelectedEditParts().get(0));
        assertTrue(connectionEP.isActive());
        assertTrue(connectionEP.getSource().isActive());
        assertEquals(groupEP, connectionEP.getSource().getParent());
        assertTrue(connectionEP.getTarget().isActive());
        assertEquals(groupEP, connectionEP.getTarget().getParent());

        tool.deactivate();
    }

    public void disabledM6testMoveGroup()
        throws Exception {

        setupShapesAndGroups();

        Tool tool = getSelectionToolFromPalette();

        Rectangle origGroupBounds = getAbsoluteBounds(getInnerGroupEP());
        Rectangle origNEShapeBounds = getAbsoluteBounds(getNWShape());
        Rectangle origSEShapeBounds = getAbsoluteBounds(getSWShape());

        Point offset = new Point(25, 25);

        // Click and drag the group in one gesture.
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        Point point = origNEShapeBounds.getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        point.translate(offset);
        tool.mouseDrag(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);
        tool.deactivate();
        flushEventQueue();

        // Cannot test the exact bounds because I can't figure out what the
        // exact bounds should be (maybe there are rounding issues?) so the
        // tests here will have to do.
        Dimension resultingOffset = origGroupBounds.getLocation()
            .getDifference(getAbsoluteBounds(getInnerGroupEP()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);
        assertEquals(resultingOffset, origNEShapeBounds.getLocation()
            .getDifference(getAbsoluteBounds(getNWShape()).getLocation()));
        assertEquals(resultingOffset, origSEShapeBounds.getLocation()
            .getDifference(getAbsoluteBounds(getSWShape()).getLocation()));

        // Now select the group first and then click over a shape and drag. This
        // should move the group.
        origGroupBounds = getAbsoluteBounds(getInnerGroupEP());
        origNEShapeBounds = getAbsoluteBounds(getNWShape());
        origSEShapeBounds = getAbsoluteBounds(getSWShape());
        point = origNEShapeBounds.getCenter();
        offset = new Point(-25, -25);

        viewer.getSelectionManager().deselectAll();
        viewer.select(getInnerGroupEP());
        tool.activate();

        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        point.translate(offset);
        tool.mouseDrag(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);
        tool.deactivate();

        flushEventQueue();

        // Cannot test the exact bounds because I can't figure out what the
        // exact bounds should be (maybe there are rounding issues?) so the
        // tests here will have to do.
        resultingOffset = origGroupBounds.getLocation().getDifference(
            getAbsoluteBounds(getInnerGroupEP()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);

        resultingOffset = origNEShapeBounds.getLocation().getDifference(
            getAbsoluteBounds(getNWShape()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);

        resultingOffset = origSEShapeBounds.getLocation().getDifference(
            getAbsoluteBounds(getSWShape()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);

        // Confirm sizes are still the same.
        assertEquals(origGroupBounds.getSize(), getAbsoluteBounds(
            getInnerGroupEP()).getSize());
        assertEquals(origNEShapeBounds.getSize(), getAbsoluteBounds(
            getNWShape()).getSize());
        assertEquals(origSEShapeBounds.getSize(), getAbsoluteBounds(
            getSWShape()).getSize());
    }

    public void testMoveShapeInGroup2()
        throws Exception {

        setupShapesAndGroups();

        Rectangle origNEShapeBounds = getAbsoluteBounds(getNWShape());
        Rectangle origSEShapeBounds = getAbsoluteBounds(getSWShape());

        // Move NEShape.
        ChangeBoundsRequest request = new ChangeBoundsRequest(
            RequestConstants.REQ_MOVE);
        request.setMoveDelta(new Point(-20, -20));
        request.setEditParts(getNWShape());
        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));
        assertEquals(getAbsoluteBounds(getNEShape()).union(
            getAbsoluteBounds(getInnerGroupEP())),
            getAbsoluteBounds(getOuterGroupEP()));

        // Move the NE shape back to where it was.
        request.setMoveDelta(new Point(20, 20));
        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));
        assertEquals(getAbsoluteBounds(getNEShape()).union(
            getAbsoluteBounds(getInnerGroupEP())),
            getAbsoluteBounds(getOuterGroupEP()));

        // Move the NE shape out beyond the bottom-right of the group.
        request.setMoveDelta(getAbsoluteBounds(getOuterGroupEP())
            .getBottomRight().getTranslated(20, 20));
        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origNEShapeBounds.getLocation(), getAbsoluteBounds(
            getNWShape()).getLocation());
        assertEquals(origSEShapeBounds.getLocation(), getAbsoluteBounds(
            getSWShape()).getLocation());
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));
        assertEquals(getAbsoluteBounds(getNEShape()).union(
            getAbsoluteBounds(getInnerGroupEP())),
            getAbsoluteBounds(getOuterGroupEP()));
    }

    public void testUngroupShapesMaintainsShapeLocations()
        throws Exception {

        setupShapesAndGroups();

        // Ungroup the inner group.

        // First cache the bounds in absolute coordinates of one of the figures.
        Rectangle origSEShapeBounds = getAbsoluteBounds(getSWShape());

        // Perform the ungroup.
        Request request = new Request(ActionIds.ACTION_UNGROUP);
        Command cmd = getInnerGroupEP().getCommand(request);
        assertTrue(cmd.canExecute());
        getCommandStack().execute(cmd);
        flushEventQueue();

        // Test that the bounds in absolute have not changed.
        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));

        // Ungroup the outer group.

        // First cache the bounds in absolute coordinates of one of the figures.
        Rectangle origNWShapeBounds = getAbsoluteBounds(getNEShape());

        // Perform the ungroup.
        request = new Request(ActionIds.ACTION_UNGROUP);
        cmd = getInnerGroupEP().getCommand(request);
        assertTrue(cmd.canExecute());
        getCommandStack().execute(cmd);
        flushEventQueue();

        // Test that the bounds in absolute have not changed.
        assertEquals(origNWShapeBounds, getAbsoluteBounds(getNEShape()));
    }

    public void testDeleteShapeInGroup()
        throws Exception {

        setupShapes();

        List<ShapeEditPart> shapes = new LinkedList<ShapeEditPart>();
        shapes.add(getNWShape());
        shapes.add(getSWShape());
        shapes.add(getSEShape());

        GroupEditPart groupEP = groupShapes(shapes);

        shapes.clear();
        shapes.add(groupEP);
        shapes.add(getNEShape());

        groupShapes(shapes);

        // Inner group has 3 shapes, outer group has inner group and 1 shape.
        assertEquals(1, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(3, getInnerGroupEP().getChildren().size());

        Request request = new GroupRequest(RequestConstants.REQ_DELETE);

        // Delete one shape from the inner group.
        getCommandStack().execute(getSWShape().getCommand(request));

        // Inner group should now have 2 shapes.
        assertEquals(2, getInnerGroupEP().getChildren().size());

        // Delete another shape from the inner group.
        getCommandStack().execute(getNWShape().getCommand(request));

        // Inner group should not exist anymore.
        assertEquals(getOuterGroupEP(), getSEShape().getParent());
        assertEquals(2, getOuterGroupEP().getChildren().size());

        // Now try undo.
        getCommandStack().undo();
        getCommandStack().undo();

        // Inner group has 3 shapes, outer group has inner group and 1 shape.
        assertEquals(1, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(3, getInnerGroupEP().getChildren().size());

    }

    /**
     * Resizing a group is not supported yet.
     * 
     * @throws Exception
     */
    public void testCannotResizeGroup()
        throws Exception {

        setupShapesAndGroups();

        ChangeBoundsRequest request = new ChangeBoundsRequest(
            RequestConstants.REQ_RESIZE);
        request.setResizeDirection(PositionConstants.SOUTH);
        request.setEditParts(getOuterGroupEP());
        request.setSizeDelta(new Dimension(0, 100));

        Command cmd = getOuterGroupEP().getCommand(request);
        assertTrue(cmd == null || !cmd.canExecute());
    }

    public void testResizeShapeInGroup()
        throws Exception {

        setupShapesAndGroups();

        Rectangle origNEShapeBounds = getAbsoluteBounds(getNWShape());
        Rectangle origSEShapeBounds = getAbsoluteBounds(getSWShape());

        // Resize the NE shape to the south-west.
        ChangeBoundsRequest request = new ChangeBoundsRequest(
            RequestConstants.REQ_RESIZE);
        request.setResizeDirection(PositionConstants.SOUTH_WEST);
        request.setMoveDelta(new Point(-10, 0));
        request.setEditParts(getNWShape());
        request.setSizeDelta(new Dimension(10, 10));

        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origNEShapeBounds.getSize(), getAbsoluteBounds(
            getNWShape()).getSize());
        assertNotEquals(origNEShapeBounds.getLocation(), getAbsoluteBounds(
            getNWShape()).getLocation());
        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Resize the NE shape to the north-east
        origNEShapeBounds = getAbsoluteBounds(getNWShape());
        origSEShapeBounds = getAbsoluteBounds(getSWShape());

        request.setResizeDirection(PositionConstants.NORTH_EAST);
        request.setMoveDelta(new Point(0, -10));
        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origNEShapeBounds.getLocation(), getAbsoluteBounds(
            getNWShape()).getLocation());
        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Resize SEShape to the south-east
        origNEShapeBounds = getAbsoluteBounds(getNWShape());
        origSEShapeBounds = getAbsoluteBounds(getSWShape());

        request.setResizeDirection(PositionConstants.SOUTH_EAST);
        request.setMoveDelta(new Point(0, 0));
        request.setEditParts(getSWShape());

        getSWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origSEShapeBounds.getSize(), getAbsoluteBounds(
            getSWShape()).getSize());
        assertEquals(origSEShapeBounds.getLocation(), getAbsoluteBounds(
            getSWShape()).getLocation());
        assertEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Resize SEShape to the north-west
        origNEShapeBounds = getAbsoluteBounds(getNWShape());
        origSEShapeBounds = getAbsoluteBounds(getSWShape());

        request.setResizeDirection(PositionConstants.NORTH_WEST);
        request.setMoveDelta(new Point(-10, -10));
        getSWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origSEShapeBounds.getSize(), getAbsoluteBounds(
            getSWShape()).getSize());
        assertNotEquals(origSEShapeBounds.getLocation(), getAbsoluteBounds(
            getSWShape()).getLocation());
        assertEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Decrease NEShape's size by 10 on each side.
        origNEShapeBounds = getAbsoluteBounds(getNWShape());
        origSEShapeBounds = getAbsoluteBounds(getSWShape());

        request.setResizeDirection(PositionConstants.NORTH_EAST);
        request.setMoveDelta(new Point(0, 10));
        request.setEditParts(getNWShape());
        request.setSizeDelta(new Dimension(-10, -10));
        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        request.setResizeDirection(PositionConstants.SOUTH_WEST);
        request.setMoveDelta(new Point(10, 0));
        getNWShape().getCommand(request).execute();
        flushEventQueue();

        assertEquals(origSEShapeBounds, getAbsoluteBounds(getSWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Decrease SEShape's size by 10 on each side.
        origNEShapeBounds = getAbsoluteBounds(getNWShape());
        origSEShapeBounds = getAbsoluteBounds(getNWShape());

        request.setResizeDirection(PositionConstants.SOUTH_EAST);
        request.setMoveDelta(new Point(0, 0));
        request.setEditParts(getSWShape());
        getSWShape().getCommand(request).execute();
        flushEventQueue();

        assertNotEquals(origSEShapeBounds, getSWShape().getFigure().getBounds());
        assertEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));

        request.setResizeDirection(PositionConstants.NORTH_WEST);
        request.setMoveDelta(new Point(10, 10));
        getSWShape().getCommand(request).execute();

        assertEquals(origNEShapeBounds, getAbsoluteBounds(getNWShape()));
        assertEquals(getAbsoluteBounds(getNWShape()).union(
            getAbsoluteBounds(getSWShape())),
            getAbsoluteBounds(getInnerGroupEP()));
    }

    public void testDeleteGroup()
        throws Exception {

        setupShapesAndGroups();

        Request request = new GroupRequest(RequestConstants.REQ_DELETE);
        getCommandStack().execute(getOuterGroupEP().getCommand(request));

        assertEquals(1, getContainerEP().getChildren().size());

        getCommandStack().undo();

        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(2, getInnerGroupEP().getChildren().size());

    }

    public void testSelectActions()
        throws Exception {
        setupShapesAndGroups();

        // test select all
        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));

        SelectAllAction selectAction = SelectAllAction
            .createSelectAllAction(getWorkbenchPage());

        testAction(selectAction, new ITestActionCallback() {

            public void onRunExecution() {

                List selectedParts = getContainerEP().getViewer()
                    .getSelectedEditParts();

                // 2 shapes and 4 connectors
                assertEquals(6, selectedParts.size());
                assertTrue(selectedParts.contains(getOuterGroupEP()));
                assertTrue(selectedParts.contains(getNEShape()
                    .getSourceConnections().get(0)));
                assertFalse(selectedParts.contains(getNWShape()));

            }
        });

        // test select all shapes
        getContainerEP().getViewer().deselectAll();
        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));

        selectAction = SelectAllAction
            .createSelectAllShapesAction(getWorkbenchPage());

        testAction(selectAction, new ITestActionCallback() {

            public void onRunExecution() {

                List selectedParts = getContainerEP().getViewer()
                    .getSelectedEditParts();

                // 2 shapes
                assertEquals(2, selectedParts.size());
                assertTrue(selectedParts.contains(getOuterGroupEP()));
                assertTrue(selectedParts.contains(getSEShape()));

            }
        });

        // test select all connections
        getContainerEP().getViewer().deselectAll();
        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));

        selectAction = SelectAllAction
            .createSelectAllConnectionsAction(getWorkbenchPage());

        testAction(selectAction, new ITestActionCallback() {

            public void onRunExecution() {

                List selectedParts = getContainerEP().getViewer()
                    .getSelectedEditParts();

                // 4 connectors
                assertEquals(4, selectedParts.size());
                assertTrue(selectedParts.contains(getNEShape()
                    .getSourceConnections().get(0)));
            }
        });
    }

    public void disabledM6testArrangeActions()
        throws Exception {
        setupShapesAndGroups();

        // test arrange all
        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));

        final Rectangle origOuterGroupBounds = getAbsoluteBounds(getOuterGroupEP());
        final Rectangle seShapeBounds = getAbsoluteBounds(getSEShape());

        ArrangeAction arrangeAction = ArrangeAction
            .createArrangeAllAction(getWorkbenchPage());
        testAction(arrangeAction, new ITestActionCallback() {

            public void onRunExecution() {
                assertEquals(origOuterGroupBounds.getSize(), getAbsoluteBounds(
                    getOuterGroupEP()).getSize());
                assertNotEquals(seShapeBounds.getLocation(), getAbsoluteBounds(
                    getSEShape()).getLocation());
            }
        });

        getCommandStack().undo();

        // test arrange selection with a group selected
        getContainerEP().getViewer().deselectAll();
        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getOuterGroupEP()));

        final Rectangle origInnerGroupBounds = getAbsoluteBounds(getInnerGroupEP());
        final Rectangle swShapeBounds = getAbsoluteBounds(getSWShape());

        arrangeAction = ArrangeAction
            .createArrangeSelectionAction(getWorkbenchPage());
        testAction(arrangeAction, new ITestActionCallback() {

            public void onRunExecution() {
                assertAlmostEquals(origInnerGroupBounds.getSize(), getAbsoluteBounds(
                    getInnerGroupEP()).getSize());
                assertNotEquals(swShapeBounds.getLocation(), getAbsoluteBounds(
                    getSWShape()).getLocation());
            }
        });

    }
    
    /**
     * It is possible that two himetric values may be off slightly just because
     * we are mixing doubles and integers in GMF/GEF and there could be slight
     * rounding differences. This method will treat a difference of less than 1
     * as being equal.
     * 
     * @param d1
     * @param d2
     * @return
     */
    private void assertAlmostEquals(Dimension d1, Dimension d2) {
        assertTrue((Math.abs(d1.height - d2.height) <= 1)
            && (Math.abs(d1.width - d2.width) <= 1));
    }

}
