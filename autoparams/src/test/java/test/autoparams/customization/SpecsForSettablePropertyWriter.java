package test.autoparams.customization;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.customization.SettablePropertyWriter;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpecsForSettablePropertyWriter {

    @ParameterizedTest
    @AutoSource
    @Customization(SettablePropertyWriter.class)
    public void sut_sets_property(HasSetter value) {
        assertNotNull(value.getProp());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(SettablePropertyWriter.class)
    public void sut_sets_generics_property(HasGenericSetters<UUID, HasSetter> value) {
        assertNotNull(value);
        assertThat(value.getValue2()).isInstanceOf(UUID.class);
        assertThat(value.getValue3()).isInstanceOf(HasSetter.class);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(SettablePropertyWriter.class)
    public void sut_sets_nested_generics_property(
        HasGenericSetters<String, HasGenericSetters<UUID, HasSetter>> value) {
        assertNotNull(value);
        assertThat(value.getValue2()).isInstanceOf(String.class);
        assertThat(value.getValue3()).isInstanceOf(HasGenericSetters.class);
        assertThat(value.getValue3().getValue3()).isInstanceOf(HasSetter.class);
    }
}
