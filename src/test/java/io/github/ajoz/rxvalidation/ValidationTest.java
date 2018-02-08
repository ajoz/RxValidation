package io.github.ajoz.rxvalidation;

import org.junit.Test;

public class ValidationTest {
    @Test
    public void map() {
        Validation.<String, String>success("Test")
                .map(String::length)
                .test();
    }
}
