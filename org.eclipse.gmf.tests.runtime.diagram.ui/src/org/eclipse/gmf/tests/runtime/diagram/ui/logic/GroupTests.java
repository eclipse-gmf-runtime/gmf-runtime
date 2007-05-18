/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

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
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.GroupAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SelectAllAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.UngroupAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestActionCallback;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;

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

        public void openDiagram()
            throws Exception {
            super.openDiagram();

            // Expand the diagram since these tests simulate mouse clicks
            // and it is necessary that the diagram be completely visible in
            // the viewport.
            IEditorReference[] editors = getWorkbenchPage().findEditors(
                ((DiagramEditor) getDiagramWorkbenchPart()).getEditorInput(),
                LogicDiagramPlugin.EDITOR_ID, IWorkbenchPage.MATCH_ID);
            getWorkbenchPage().setPartState(editors[0],
                IWorkbenchPage.STATE_MAXIMIZED);
        }
    }

    protected void setTestFixture() {
        testFixture = new GroupTestFixture();
    }

    protected LogicTestFixture getFixture() {
        return (LogicTestFixture) testFixture;
    }

    protected View note1View;

    protected View note2View;

    protected View geoshape1View;

    protected View geoshape2View;

    /**
     * Adds two notes and two geoshapes and some connections between them. Look
     * at the details of the method to see what gets created.
     */
    protected void setupShapes() {
        ShapeEditPart note1EP = getFixture().createShapeUsingTool(
            DiagramNotationType.NOTE, new Point(10, 10), getContainerEP(),
            new Dimension(50, 50));

        ShapeEditPart note2EP = getFixture().createShapeUsingTool(
            DiagramNotationType.NOTE, new Point(100, 10), getContainerEP(),
            new Dimension(50, 50));

        ShapeEditPart geoshape1EP = getFixture().createShapeUsingTool(
            GeoshapeType.CYLINDER, new Point(10, 100), getContainerEP(),
            new Dimension(50, 50));

        ShapeEditPart geoshape2EP = getFixture().createShapeUsingTool(
            GeoshapeType.DIAMOND, new Point(100, 100), getContainerEP(),
            new Dimension(50, 50));

        flushEventQueue();

        // Cache the views so we can find the editparts again later.
        note1View = (View) note1EP.getModel();
        note2View = (View) note2EP.getModel();
        geoshape1View = (View) geoshape1EP.getModel();
        geoshape2View = (View) geoshape2EP.getModel();

        // Create some connections just to make things more complicated.
        getFixture().createConnectorUsingTool(note1EP, geoshape1EP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(note2EP, geoshape1EP,
            DiagramNotationType.NOTE_ATTACHMENT);
        getFixture().createConnectorUsingTool(note2EP, geoshape2EP,
            DiagramNotationType.NOTE_ATTACHMENT);
        getFixture().createConnectorUsingTool(geoshape1EP, geoshape2EP,
            GeoshapeType.LINE);

        flushEventQueue();
    }

    protected IGraphicalEditPart getContainerEP() {
        return getDiagramEditPart();
    }

    protected ShapeEditPart getNote1EP() {
        return (ShapeEditPart) findEditPart(note1View);
    }

    protected ShapeEditPart getNote2EP() {
        return (ShapeEditPart) findEditPart(note2View);
    }

    protected ShapeEditPart getGeoshape1EP() {
        return (ShapeEditPart) findEditPart(geoshape1View);
    }

    protected ShapeEditPart getGeoshape2EP() {
        return (ShapeEditPart) findEditPart(geoshape2View);
    }

    protected void setupShapesAndGroups() {
        setupShapes();

        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());

        GroupEditPart groupEP = groupShapes(shapes);

        shapes.clear();
        shapes.add(groupEP);
        shapes.add(getNote2EP());

        groupShapes(shapes);

        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(2, getInnerGroupEP().getChildren().size());
    }

    protected GroupEditPart getInnerGroupEP() {
        return (GroupEditPart) getNote1EP().getParent();
    }

    protected GroupEditPart getOuterGroupEP() {
        return (GroupEditPart) getNote2EP().getParent();
    }

    protected GroupEditPart groupShapes(List editparts) {
        View childView = (View) ((IGraphicalEditPart) editparts.get(0))
            .getModel();

        GroupRequest request = new GroupRequest(ActionIds.ACTION_GROUP);
        request.setEditParts(editparts);
        Command cmd = ((IGraphicalEditPart) editparts.get(0)).getParent()
            .getCommand(request);
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
        Rectangle bounds = editpart.getFigure().getBounds().getCopy();
        editpart.getFigure().translateToAbsolute(bounds);
        return bounds;
    }

    public void testGroupCommandAndUndoRedo()
        throws Exception {

        setupShapes();

        // Group note1 and geoshape1 and test undo/redo of the action.
        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());

        GroupEditPart group1EP = groupShapes(shapes);

        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(group1EP, getGeoshape1EP().getParent());
        assertEquals(group1EP.getParent(), getContainerEP());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(getAbsoluteBounds(group1EP), getAbsoluteBounds(
            getNote1EP()).union(getAbsoluteBounds(getGeoshape1EP())));

        assertTrue(getCommandStack().canUndo());
        getCommandStack().undo();

        assertEquals(getContainerEP(), getNote1EP().getParent());
        assertEquals(getGeoshape1EP().getParent(), getContainerEP());
        assertEquals(4, getContainerEP().getChildren().size());

        assertTrue(getCommandStack().canRedo());
        getCommandStack().redo();

        group1EP = (GroupEditPart) getNote1EP().getParent();
        assertEquals(group1EP, getGeoshape1EP().getParent());
        assertEquals(group1EP.getParent(), getContainerEP());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(getAbsoluteBounds(group1EP), getAbsoluteBounds(
            getNote1EP()).union(getAbsoluteBounds(getGeoshape1EP())));

        // Group group1 and note2 and test undo/redo of the action.

        shapes.clear();
        shapes.add(group1EP);
        shapes.add(getNote2EP());

        GroupEditPart group2EP = groupShapes(shapes);

        group1EP = (GroupEditPart) getNote1EP().getParent();

        assertEquals(group1EP, getGeoshape1EP().getParent());
        assertEquals(group2EP, group1EP.getParent());
        assertEquals(group2EP, getNote2EP().getParent());
        assertEquals(getContainerEP(), group2EP.getParent());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(2, group2EP.getChildren().size());
        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(getAbsoluteBounds(group2EP), getAbsoluteBounds(
            getNote2EP()).union(getAbsoluteBounds(group1EP)));

        assertTrue(getCommandStack().canUndo());
        getCommandStack().undo();

        group1EP = (GroupEditPart) getNote1EP().getParent();
        assertEquals(getContainerEP(), group1EP.getParent());
        assertEquals(getContainerEP(), getNote2EP().getParent());
        assertEquals(3, getContainerEP().getChildren().size());

        assertTrue(getCommandStack().canRedo());
        getCommandStack().redo();

        group1EP = (GroupEditPart) getNote1EP().getParent();
        group2EP = (GroupEditPart) getNote2EP().getParent();
        assertEquals(group2EP, group1EP.getParent());
        assertEquals(getContainerEP(), group2EP.getParent());
        assertEquals(2, group1EP.getChildren().size());
        assertEquals(2, group2EP.getChildren().size());
        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(getAbsoluteBounds(group2EP), getAbsoluteBounds(
            getNote2EP()).union(getAbsoluteBounds(group1EP)));
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
        assertEquals(getContainerEP(), getNote2EP().getParent());

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
        assertEquals(getContainerEP(), getNote2EP().getParent());
    }

    public void testGroupIsSelectedAfterGroupAction()
        throws Exception {

        setupShapes();

        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());

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

        // should be enabled when connections are included in the selection
        viewer.setSelection(new StructuredSelection(getDiagramEditPart()
            .getPrimaryEditParts()));
        action.refresh();
        assertTrue(action.isEnabled());

        // should be disabled when only one shape is selected
        viewer.setSelection(new StructuredSelection(getNote1EP()));
        action.refresh();
        assertFalse(action.isEnabled());

        // should be enabled when connections are included in the selection
        viewer.setSelection(new StructuredSelection(getDiagramEditPart()
            .getPrimaryEditParts()));
        action.refresh();
        assertTrue(action.isEnabled());

        // should be disabled when only connections are selected
        viewer.setSelection(new StructuredSelection(getDiagramEditPart()
            .getConnections()));
        action.refresh();
        assertFalse(action.isEnabled());

        // should be enabled on groups
        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());
        GroupEditPart groupEP = groupShapes(shapes);
        shapes.clear();
        shapes.add(groupEP);
        shapes.add(getNote2EP());
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

        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());
        GroupEditPart group1EP = groupShapes(shapes);

        shapes.clear();
        shapes.add(getNote2EP());
        shapes.add(getGeoshape2EP());
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
        List groups = new LinkedList();
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
        Point point = getAbsoluteBounds(getNote2EP()).getCenter();
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
        Point point = getAbsoluteBounds(getNote2EP()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getOuterGroupEP()));

        // click on the same shape a second time and the shape should be
        // selected
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getNote2EP()));

        // right-click on the same shape and it should remain selected
        tool.mouseDown(createRightMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createRightMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getNote2EP()));

        // click on a shape not in the group and that shape itself should be
        // selected
        viewer.getSelectionManager().deselectAll();
        point = getAbsoluteBounds(getGeoshape2EP()).getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        assertEquals(1, viewer.getSelectedEditParts().size());
        assertTrue(viewer.getSelectedEditParts().contains(getGeoshape2EP()));

        // click on an area of blank space in a group and nothing should be
        // selected
        viewer.getSelectionManager().deselectAll();
        tool.mouseDown(createMouseEvent(30, 75), viewer);
        tool.mouseUp(createMouseEvent(30, 75), viewer);

        assertEquals(0, viewer.getSelectedEditParts().size());

        // click on a shape in the inner group and the outer group should be
        // selected
        viewer.getSelectionManager().deselectAll();
        point = getAbsoluteBounds(getNote1EP()).getCenter();
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
        assertTrue(viewer.getSelectedEditParts().contains(getNote1EP()));

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

        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());
        GroupEditPart groupEP = groupShapes(shapes);

        ConnectionEditPart connectionEP = (ConnectionEditPart) getNote1EP()
            .getSourceConnections().get(0);

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

    public void testMoveGroup()
        throws Exception {

        setupShapesAndGroups();

        Tool tool = getSelectionToolFromPalette();

        System.out.println(getInnerGroupEP().getFigure().getBounds());
        System.out.println(getAbsoluteBounds(getInnerGroupEP()));
        Rectangle origGroupBounds = getAbsoluteBounds(getInnerGroupEP());
        Rectangle origNote1Bounds = getAbsoluteBounds(getNote1EP());
        Rectangle origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());

        Point offset = new Point(25, 25);

        // Click and drag the group in one gesture.
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        Point point = origNote1Bounds.getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        point.translate(offset);
        tool.mouseDrag(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);
        tool.deactivate();
        flushEventQueue();
        Thread.sleep(5000);

        // Cannot test the exact bounds because I can't figure out what the
        // exact bounds should be (maybe there are rounding issues?) so the
        // tests here will have to do.
        Dimension resultingOffset = origGroupBounds.getLocation()
            .getDifference(getAbsoluteBounds(getInnerGroupEP()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);
        assertEquals(resultingOffset, origNote1Bounds.getLocation()
            .getDifference(getAbsoluteBounds(getNote1EP()).getLocation()));
        assertEquals(resultingOffset, origGeoshape1Bounds.getLocation()
            .getDifference(getAbsoluteBounds(getGeoshape1EP()).getLocation()));

        // Now select the group first and then click over a shape and drag. This
        // should move the group.
        origGroupBounds = getAbsoluteBounds(getInnerGroupEP());
        origNote1Bounds = getAbsoluteBounds(getNote1EP());
        origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());
        point = origNote1Bounds.getCenter();
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

        resultingOffset = origNote1Bounds.getLocation().getDifference(
            getAbsoluteBounds(getNote1EP()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);

        resultingOffset = origGeoshape1Bounds.getLocation().getDifference(
            getAbsoluteBounds(getGeoshape1EP()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);

        // Confirm sizes are still the same.
        assertEquals(origGroupBounds.getSize(), getAbsoluteBounds(
            getInnerGroupEP()).getSize());
        assertEquals(origNote1Bounds.getSize(), getAbsoluteBounds(getNote1EP())
            .getSize());
        assertEquals(origGeoshape1Bounds.getSize(), getAbsoluteBounds(
            getGeoshape1EP()).getSize());
    }

    public void testMoveShapeInGroup()
        throws Exception {

        setupShapesAndGroups();

        Tool tool = getSelectionToolFromPalette();

        Rectangle origGroupBounds = getAbsoluteBounds(getInnerGroupEP());
        Rectangle origNote1Bounds = getAbsoluteBounds(getNote1EP());
        Rectangle origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());

        Point offset = new Point(25, 25);

        // Move geoshape1. Click three times -- first select the outer group,
        // then select the inner group, then select geoshape1.
        EditPartViewer viewer = getDiagramEditPart().getViewer();
        viewer.getSelectionManager().deselectAll();
        tool.activate();
        Point point = origGeoshape1Bounds.getCenter();
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);

        // Now click and drag geoshape1 to move it.
        tool.mouseDown(createMouseEvent(point.x, point.y), viewer);
        point.translate(offset);
        tool.mouseDrag(createMouseEvent(point.x, point.y), viewer);
        tool.mouseUp(createMouseEvent(point.x, point.y), viewer);
        tool.deactivate();
        flushEventQueue();

        // Confirm geoshape1 was not removed from the group.
        assertEquals(2, getInnerGroupEP().getChildren().size());

        // geoshape1 should have moved.
        Dimension resultingOffset = origGeoshape1Bounds.getLocation()
            .getDifference(getAbsoluteBounds(getGeoshape1EP()).getLocation());
        assertTrue(resultingOffset.height != 0 && resultingOffset.width != 0);
        assertEquals(origGeoshape1Bounds.getSize(), getAbsoluteBounds(
            getGeoshape1EP()).getSize());

        // note1 should remain the same.
        assertEquals(origNote1Bounds, getAbsoluteBounds(getNote1EP()));

        // The group location should not have changed but the size should have
        // grown.
        assertEquals(origGroupBounds.getLocation(), getAbsoluteBounds(
            getInnerGroupEP()).getLocation());
        assertNotSame(origGroupBounds.getSize(), getAbsoluteBounds(
            getInnerGroupEP()).getSize());

    }

    public void testUngroupShapesMaintainsShapeLocations()
        throws Exception {

        setupShapesAndGroups();

        // Ungroup the inner group.

        // First cache the bounds in absolute coordinates of one of the figures.
        Rectangle origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());

        // Perform the ungroup.
        Request request = new Request(ActionIds.ACTION_UNGROUP);
        Command cmd = getInnerGroupEP().getCommand(request);
        assertTrue(cmd.canExecute());
        getCommandStack().execute(cmd);
        flushEventQueue();

        // Test that the bounds in absolute have not changed.
        assertEquals(origGeoshape1Bounds, getAbsoluteBounds(getGeoshape1EP()));

        // Ungroup the outer group.

        // First cache the bounds in absolute coordinates of one of the figures.
        Rectangle origNote2Bounds = getAbsoluteBounds(getNote2EP());

        // Perform the ungroup.
        request = new Request(ActionIds.ACTION_UNGROUP);
        cmd = getInnerGroupEP().getCommand(request);
        assertTrue(cmd.canExecute());
        getCommandStack().execute(cmd);
        flushEventQueue();

        // Test that the bounds in absolute have not changed.
        assertEquals(origNote2Bounds, getAbsoluteBounds(getNote2EP()));
    }

    public void testDeleteShapeInGroup()
        throws Exception {

        setupShapes();

        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());
        shapes.add(getGeoshape2EP());

        GroupEditPart groupEP = groupShapes(shapes);

        shapes.clear();
        shapes.add(groupEP);
        shapes.add(getNote2EP());

        groupShapes(shapes);

        // Inner group has 3 shapes, outer group has inner group and 1 shape.
        assertEquals(1, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(3, getInnerGroupEP().getChildren().size());

        Request request = new GroupRequest(RequestConstants.REQ_DELETE);

        // Delete one shape from the inner group.
        getCommandStack().execute(getGeoshape1EP().getCommand(request));

        // Inner group should now have 2 shapes.
        assertEquals(2, getInnerGroupEP().getChildren().size());

        // Delete another shape from the inner group.
        getCommandStack().execute(getNote1EP().getCommand(request));

        // Inner group should not exist anymore.
        assertEquals(getOuterGroupEP(), getGeoshape2EP().getParent());
        assertEquals(2, getOuterGroupEP().getChildren().size());

        // Now try undo.
        getCommandStack().undo();
        getCommandStack().undo();

        // Inner group has 3 shapes, outer group has inner group and 1 shape.
        assertEquals(1, getContainerEP().getChildren().size());
        assertEquals(2, getOuterGroupEP().getChildren().size());
        assertEquals(3, getInnerGroupEP().getChildren().size());

    }

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

        Rectangle origNote1Bounds = getAbsoluteBounds(getNote1EP());
        Rectangle origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());

        // Increase note1's size by 10 on each side.
        ChangeBoundsRequest request = new ChangeBoundsRequest(
            RequestConstants.REQ_RESIZE);
        request.setResizeDirection(PositionConstants.NORTH_EAST);
        request.setEditParts(getNote1EP());
        request.setSizeDelta(new Dimension(10, 10));

        getNote1EP().getCommand(request).execute();

        assertNotSame(origNote1Bounds, getAbsoluteBounds(getNote1EP()));
        assertEquals(origGeoshape1Bounds, getAbsoluteBounds(getGeoshape1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        request.setResizeDirection(PositionConstants.SOUTH_WEST);
        getNote1EP().getCommand(request).execute();

        assertEquals(origGeoshape1Bounds, getAbsoluteBounds(getGeoshape1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Increase geoshape1's size by 10 on each side.
        origNote1Bounds = getAbsoluteBounds(getNote1EP());
        origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());

        request.setResizeDirection(PositionConstants.SOUTH_EAST);
        request.setEditParts(getGeoshape1EP());

        getGeoshape1EP().getCommand(request).execute();

        assertNotSame(origGeoshape1Bounds, getAbsoluteBounds(getGeoshape1EP()));
        assertEquals(origNote1Bounds, getAbsoluteBounds(getNote1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        request.setResizeDirection(PositionConstants.NORTH_WEST);
        getGeoshape1EP().getCommand(request).execute();

        assertEquals(origNote1Bounds, getAbsoluteBounds(getNote1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Decrease note1's size by 10 on each side.
        origNote1Bounds = getAbsoluteBounds(getNote1EP());
        origGeoshape1Bounds = getAbsoluteBounds(getGeoshape1EP());

        request.setResizeDirection(PositionConstants.NORTH_EAST);
        request.setEditParts(getNote1EP());
        request.setSizeDelta(new Dimension(-10, -10));

        getNote1EP().getCommand(request).execute();

        assertNotSame(origNote1Bounds, getAbsoluteBounds(getNote1EP()));
        assertEquals(origGeoshape1Bounds, getAbsoluteBounds(getGeoshape1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        request.setResizeDirection(PositionConstants.SOUTH_WEST);
        getNote1EP().getCommand(request).execute();

        assertEquals(origGeoshape1Bounds, getAbsoluteBounds(getGeoshape1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        // Decrease geoshape1's size by 10 on each side.
        origNote1Bounds = getAbsoluteBounds(getNote1EP());
        origGeoshape1Bounds = getAbsoluteBounds(getNote1EP());

        request.setResizeDirection(PositionConstants.SOUTH_EAST);
        request.setEditParts(getGeoshape1EP());

        getGeoshape1EP().getCommand(request).execute();

        assertNotSame(origGeoshape1Bounds, getGeoshape1EP().getFigure()
            .getBounds());
        assertEquals(origNote1Bounds, getAbsoluteBounds(getNote1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
            getAbsoluteBounds(getInnerGroupEP()));

        request.setResizeDirection(PositionConstants.NORTH_WEST);
        getGeoshape1EP().getCommand(request).execute();

        assertEquals(origNote1Bounds, getAbsoluteBounds(getNote1EP()));
        assertEquals(getAbsoluteBounds(getNote1EP()).union(
            getAbsoluteBounds(getGeoshape1EP())),
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
                assertTrue(selectedParts.contains(getNote2EP()
                    .getSourceConnections().get(0)));
                assertFalse(selectedParts.contains(getNote1EP()));

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
                assertTrue(selectedParts.contains(getGeoshape2EP()));

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
                assertTrue(selectedParts.contains(getNote2EP()
                    .getSourceConnections().get(0)));
            }
        });
    }

}
