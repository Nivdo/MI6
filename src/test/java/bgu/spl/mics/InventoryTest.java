package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;

    /**
     * <p>
     *
     */
    @BeforeEach
    public void setUp(){//initialize whatever we want to test
        inventory= Inventory.getInstance();
    }


    /**
     * <p>
     *  test instance is not null
     */
    @Test
    public void testGetInstance(){
        assertNotNull(inventory);
    }

    /**
     * <p>
     *  test load really add the item to the inventory
     */
    @Test
    public void testLoadingAndGettingItems(){
        String [] newGadgets = new String [3];
        newGadgets[0]="superGun";
        newGadgets[1]="boom";
        newGadgets[2]="vanishCar";
        inventory.load(newGadgets);
        boolean val=true;
        for(int i =0; i<3 & val; i++){
            val=inventory.getItem(newGadgets[i]);
        }
        assertTrue(val);
    }

    /**
     * <p>
     *  test getItem really removes the item from the inventory
     */
    @Test
    public void testRemoving(){
        String [] newGadgets = new String [3];
        newGadgets[0]="deathRay";
        newGadgets[1]="Aston Martin";
        newGadgets[2]="Martini";
        inventory.load(newGadgets);
        for(int i =0 ; i<3 ; i++){
            inventory.getItem(newGadgets[i]);
        }
        assertFalse(inventory.getItem("deathRay"));
        assertFalse(inventory.getItem("Aston Martin"));
        assertFalse(inventory.getItem("Martini"));
    }


    /**
     * <p>
     *  test inventory is a singletone
     */
    @Test
    public void testSingelton(){
        assertSame(inventory,Inventory.getInstance());
    }
}