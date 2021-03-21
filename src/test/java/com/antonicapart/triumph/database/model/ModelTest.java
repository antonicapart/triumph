package com.antonicapart.triumph.database.model;

import org.junit.Assert;
import org.junit.Test;

public class ModelTest {

    @Test
    public void testIfAModelHasAValidField() {
        final String column = "columnExample";

        final ModelExample model = new ModelExample();

        Assert.assertTrue(model.has(column));
    }

    @Test
    public void testIfAModelHasAnInvalidField() {
        final String column = "wrongColumnExample";

        final ModelExample model = new ModelExample();

        Assert.assertFalse(model.has(column));
    }

    @Test
    public void testSetAValidValueInAnExistingField() {
        final String column = "columnExample";
        final Long value = 1L;

        final ModelExample model = new ModelExample();
        final boolean test = model.set(column, value);

        Assert.assertTrue(test);
        Assert.assertEquals(model.getColumnExample(), value);
    }

    @Test
    public void testSetAWrongValueInAnExistingField() {
        final String column = "columnExample";
        final String value = "Wrong type value";

        final ModelExample model = new ModelExample();
        final boolean test = model.set(column, value);

        Assert.assertFalse(test);
    }

    @Test
    public void testSetAValueInANonExistentField() {
        final String column = "wrongColumnExample";
        final Long value = 1L;

        final ModelExample model = new ModelExample();
        final boolean test = model.set(column, value);

        Assert.assertFalse(test);
    }

    @Test
    public void testGetAValidValueInAnExistingField() {
        final String column = "columnExample";
        final Long value = 1L;

        final ModelExample model = new ModelExample();
        model.setColumnExample(value);

        Assert.assertEquals(model.get(column), value);
    }

    @Test
    public void testGetAValueInANonExistentField() {
        final String column = "wrongColumnExample";
        final Long value = 1L;

        final ModelExample model = new ModelExample();
        model.setColumnExample(value);

        Assert.assertNull(model.get(column));
    }

    @Test
    public void testGetAPrimitifValue() {
        final String column = "primitifColumnExample";
        final int value = 1;

        final ModelExample model = new ModelExample();
        model.set(column, value);

        Assert.assertEquals(model.get(column), value);
    }

}
