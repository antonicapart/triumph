package com.antonicapart.triumph.support;

import org.junit.Assert;
import org.junit.Test;

public class StrTest {

    @Test
    public void testConvertCamelCaseToSnakeCase() {
        Assert.assertEquals(Str.toSnake("triumphJAVAFramework"), "triumph_j_a_v_a_framework");
    }

    @Test
    public void testConvertStudlyCaseToSnakeCase() {
        Assert.assertEquals(Str.toSnake("TriumphJAVAFramework"), "triumph_j_a_v_a_framework");
    }

    @Test
    public void testConvertKebabCaseToSnakeCase() {
        Assert.assertEquals(Str.toSnake("triumph-j-a-v-a-framework"), "triumph_j_a_v_a_framework");
    }

    @Test
    public void testConvertSnakeCaseToCamelCase() {
        Assert.assertEquals(Str.toCamel("triumph_j_a_v_a_framework"), "triumphJAVAFramework");
    }

    @Test
    public void testConvertSnakeCaseToStudlyCase() {
        Assert.assertEquals(Str.toStudly("triumph_j_a_v_a_framework"), "TriumphJAVAFramework");
    }

    @Test
    public void testConvertSnakeCaseToKebabCase() {
        Assert.assertEquals(Str.toKebab("triumph_j_a_v_a_framework"), "triumph-j-a-v-a-framework");
    }

    @Test
    public void testToAddQuotationMarksToAString() {
        Assert.assertEquals(Str.addQuotationMarks("It is a test string"), "'It is a test string'");
    }

    @Test
    public void testToCapitalizeTheFirstCaseOfTheString() {
        Assert.assertEquals(Str.upperCaseFirst("it is a test string"), "It is a test string");
    }

    @Test
    public void testToPutInLowerTheFirstCaseOfTheString() {
        Assert.assertEquals(Str.lowerCaseFirst("It is a test string"), "it is a test string");
    }

}
