package com.hart.overwatch.util;

import org.springframework.test.context.ActiveProfiles;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@ActiveProfiles("test")
public class MyUtilTest {

    @Test
    public void MyUtil_Capitalize_ReturnString() {
        Assertions.assertThat("Hello").isEqualTo(MyUtil.capitalize("hello"));
        Assertions.assertThat("World").isEqualTo(MyUtil.capitalize("WORLD"));
        Assertions.assertThat("Foo").isEqualTo(MyUtil.capitalize("fOO"));
    }

    @Test
    public void MyUtil_ValidatePassword_ReturnBoolean() {
        Assertions.assertThat(MyUtil.validatePassword("Test12345%")).isTrue();
        Assertions.assertThat(MyUtil.validatePassword("Test12345")).isFalse();
        Assertions.assertThat(MyUtil.validatePassword("test12345%")).isFalse();
        Assertions.assertThat(MyUtil.validatePassword("test12345%")).isFalse();
        Assertions.assertThat(MyUtil.validatePassword("TEST12345%")).isFalse();
    }

    @Test
    public void MyUtil_Deslugify_ReturnString() {
        Assertions.assertThat("hello world").isEqualTo(MyUtil.deslugify("hello-world"));
        Assertions.assertThat("test driven development")
                .isEqualTo(MyUtil.deslugify("test-driven-development"));

    }

}
