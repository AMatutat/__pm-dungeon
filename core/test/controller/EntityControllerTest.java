package controller;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import basiselements.DungeonElement;
import graphic.Painter;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({})
public class EntityControllerTest {
    private DungeonElement entity1, entity2;
    private EntityController controller;
    private Painter painter;

    @Before
    public void setUp() {
        entity1 = Mockito.mock(DungeonElement.class);
        entity2 = Mockito.mock(DungeonElement.class);
        painter = Mockito.mock(Painter.class);
        controller = new EntityController(painter);
    }

    @Test
    public void test_update_withEmptyController() {
        EntityController ecSpy = Mockito.spy(new EntityController(painter));
        PowerMockito.doNothing().when(ecSpy).forEach(any());
        assumeTrue(ecSpy.isEmpty());
        verify(ecSpy).isEmpty();

        ecSpy.update();
        assertFalse(ecSpy.contains(entity1));
        assertFalse(ecSpy.contains(entity2));
        assertTrue(ecSpy.isEmpty());
    }

    @Test
    public void test_update_withRemove() {
        // should be removed
        when(entity1.removable()).thenReturn(true);
        assumeTrue(controller.add(entity1));

        controller.update();
        verify(entity1).removable();
        Mockito.verifyNoMoreInteractions(entity1);
        assertFalse(controller.contains(entity1));
        assertFalse(controller.contains(entity2));
        assertTrue(controller.isEmpty());
    }

    @Test
    public void test_update_withRemoveAndTwoElements() {
        // should be removed
        when(entity1.removable()).thenReturn(true);
        when(entity2.removable()).thenReturn(true);
        assumeTrue(controller.add(entity1));
        assumeTrue(controller.add(entity2));

        controller.update();
        verify(entity1).removable();
        verify(entity2).removable();
        Mockito.verifyNoMoreInteractions(entity1, entity2);
        assertFalse(controller.contains(entity1));
        assertFalse(controller.contains(entity2));
        assertTrue(controller.isEmpty());
    }

    @Test
    public void test_update_withoutRemove() {
        // should not be removed
        when(entity1.removable()).thenReturn(false);
        assumeTrue(controller.add(entity1));

        controller.update();
        verify(entity1).removable();
        verify(entity1).update();
        verify(entity1).draw(painter);
        Mockito.verifyNoMoreInteractions(entity1);
        assertTrue(controller.contains(entity1));
        assertFalse(controller.contains(entity2));
        assertFalse(controller.isEmpty());
    }

    @Test
    public void test_update_withoutRemoveAndTwoElements() {
        // should not be removed
        when(entity1.removable()).thenReturn(false);
        when(entity2.removable()).thenReturn(false);
        assumeTrue(controller.add(entity1));
        assumeTrue(controller.add(entity2));

        controller.update();
        verify(entity1).removable();
        verify(entity1).update();
        verify(entity1).draw(painter);
        verify(entity2).removable();
        verify(entity2).update();
        verify(entity2).draw(painter);
        Mockito.verifyNoMoreInteractions(entity1, entity2);
        assertTrue(controller.contains(entity1));
        assertTrue(controller.contains(entity2));
        assertFalse(controller.isEmpty());
    }

    @Test
    public void test_update_withDifferentLayer() {
        // should not be removed
        when(entity1.removable()).thenReturn(false);
        when(entity2.removable()).thenReturn(false);
        assumeTrue(controller.add(entity1, ControllerLayer.TOP));
        assumeTrue(controller.add(entity2, ControllerLayer.BOTTOM));
        assumeTrue(controller.remove(entity1));
        assumeTrue(controller.add(entity1, ControllerLayer.TOP));

        controller.update();
        verify(entity1).removable();
        verify(entity1).update();
        verify(entity1).draw(painter);
        verify(entity2).removable();
        verify(entity2).update();
        verify(entity2).draw(painter);
        Mockito.verifyNoMoreInteractions(entity1, entity2);
        assertTrue(controller.contains(entity1));
        assertTrue(controller.contains(entity2));
        assertFalse(controller.isEmpty());
    }

    @Test
    public void test_update_withDifferentLayerAndDuplicates() {
        // should not be removed
        when(entity1.removable()).thenReturn(false);
        when(entity2.removable()).thenReturn(false);
        assumeTrue(controller.add(entity1, ControllerLayer.TOP));
        assumeTrue(controller.add(entity2, ControllerLayer.BOTTOM));
        assumeFalse(controller.add(entity1, ControllerLayer.BOTTOM));
        assumeFalse(controller.add(entity2, ControllerLayer.TOP));
        assumeTrue(controller.remove(entity1));
        assumeTrue(controller.add(entity1, ControllerLayer.TOP));

        controller.update();

        when(entity2.removable()).thenReturn(true);
        controller.update();

        verify(entity1, times(2)).removable();
        verify(entity1, times(2)).update();
        verify(entity1, times(2)).draw(painter);
        verify(entity2, times(2)).removable();
        verify(entity2, times(1)).update();
        verify(entity2, times(1)).draw(painter);
        Mockito.verifyNoMoreInteractions(entity1, entity2);

        assertTrue(controller.contains(entity1));
        assertFalse(controller.contains(entity2));
        assertFalse(controller.isEmpty());
    }

    @Test
    public void test_listBehavior() {
        DungeonElement e1 = Mockito.mock(DungeonElement.class);
        DungeonElement e2 = Mockito.mock(DungeonElement.class);
        DungeonElement e3 = Mockito.mock(DungeonElement.class);
        DungeonElement e4 = Mockito.mock(DungeonElement.class);
        List<DungeonElement> l1 = List.of(e1, e2, e3, e4);
        List<DungeonElement> l2 = List.of(e1, e4);
        List<DungeonElement> l3 = List.of(e4);

        assertTrue(controller.addAll(l1)); // 1,2,3,4
        assertTrue(controller.remove(e1)); // 2,3,4
        assertFalse(controller.isEmpty());
        controller.clearLayer(ControllerLayer.DEFAULT);
        assertTrue(controller.isEmpty());

        assertTrue(controller.addAll(l1)); // 1,2,3,4
        assertFalse(controller.addAll(l2)); // 1,2,3,4
        for (Iterator<DungeonElement> it = controller.iterator(); it.hasNext(); ) {
            DungeonElement e = it.next();
            if (e != e4) {
                it.remove();
            }
        }
        assertFalse(controller.isEmpty());
        assertEquals(1, controller.size());
        controller.clear();
        assertTrue(controller.isEmpty());
    }
}
