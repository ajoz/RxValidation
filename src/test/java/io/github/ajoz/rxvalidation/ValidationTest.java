package io.github.ajoz.rxvalidation;

import io.reactivex.functions.Function;
import org.junit.Test;

public class ValidationTest {
    @Test
    public void map() {
        final String expected = "expected";

        Validation.<String, String>success("Test")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(final String validated) {
                        return expected;
                    }
                })
                .test();
    }
}
