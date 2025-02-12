package test.autoparams.primitive;

import autoparams.Repeat;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpecsForInt {

    @AutoParameterizedTest
    void sut_creates_arbitrary_int_values(
        int value1,
        int value2,
        int value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Integer_values(
        Integer value1,
        Integer value2,
        Integer value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_int_value(int arg) {
        assertThat(arg).isPositive();
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_max_constraint_for_int(@Max(100) int value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @AutoParameterizedTest
    void sut_includes_max_value_for_int(
        @Min(Integer.MAX_VALUE) @Max(Integer.MAX_VALUE) int arg
    ) {
        assertThat(arg).isEqualTo(Integer.MAX_VALUE);
    }

    @AutoParameterizedTest
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) int arg
    ) {
        assertThat(arg).isNotPositive();
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_large(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMaxConstraint", int.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelyLargeMaxConstraint(@Max(Integer.MAX_VALUE + 1L) int arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_small(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMaxConstraint", int.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelySmallMaxConstraint(@Max(Integer.MIN_VALUE - 1L) int arg) {
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_int(@Min(100) int value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_large(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMinConstraint", int.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelyLargeMinConstraint(@Min(Integer.MAX_VALUE + 1L) int arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_small(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMinConstraint", int.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelySmallMinConstraint(@Min(Integer.MIN_VALUE - 1L) int arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", int.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void maxConstraintLessThanMinConstraint(@Min(100) @Max(99) int arg) {
    }
}
