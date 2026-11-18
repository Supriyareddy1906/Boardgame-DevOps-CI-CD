
package com.javaproject.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ErrorMessageTest {

    @Test
    void testConstructorAndGetters() {
        ErrorMessage error = new ErrorMessage("Something went wrong");

        assertEquals("error", error.getSTATUS());
        assertEquals("Something went wrong", error.getMessage());
    }

    @Test
    void testSetter() {
        ErrorMessage error = new ErrorMessage("Old message");

        error.setMessage("Updated message");

        assertEquals("Updated message", error.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorMessage first = new ErrorMessage("Error occurred");
        ErrorMessage second = new ErrorMessage("Error occurred");
        ErrorMessage third = new ErrorMessage("Different error");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, third);
        assertNotEquals(first, null);
    }

    @Test
    void testToString() {
        ErrorMessage error = new ErrorMessage("Test error");

        assertTrue(error.toString().contains("Test error"));
    }

    @Test
    void testCanEqual() {
        ErrorMessage first = new ErrorMessage("Error");
        ErrorMessage second = new ErrorMessage("Error");

        assertTrue(first.canEqual(second));
    }
}
