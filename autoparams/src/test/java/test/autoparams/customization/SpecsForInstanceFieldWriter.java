package test.autoparams.customization;

import autoparams.AutoSource;
import autoparams.customization.CompositeCustomizer;
import autoparams.customization.Customization;
import autoparams.customization.InstanceFieldWriter;
import autoparams.generator.ObjectGenerationContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SpecsForInstanceFieldWriter {

    public static class DomainCustomizer extends CompositeCustomizer {
        public DomainCustomizer() {
            super(
                new InstanceFieldWriter(Versioned.class),
                new InstanceFieldWriter(Entity.class),
                new InstanceFieldWriter(User.class),
                new InstanceFieldWriter(Worker.class).excluding("activeWorks", "closedWorks"),
                new InstanceFieldWriter(Operator.class).including("teamName", "phoneNumber"),
                new InstanceFieldWriter(Inventory.class));
        }
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_sets_fields(Versioned versioned) {
        assertNotNull(versioned.getVersion());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_sets_fields_of_generic_class(Entity<Long> entity) {
        assertNotNull(entity.getId());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_sets_fields_of_generic_type(Inventory<Entity<Long>> inventory) {
        assertNotNull(inventory.getItems());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_sets_inherited_fields(Worker worker) {
        assertNotNull(worker.getId());
        assertNotNull(worker.getUsername());
        assertNotNull(worker.getTeamName());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_does_not_set_static_fields(User user) {
        assertEquals("hello world", User.getDefaultGreeting());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_does_not_set_excluded_fields(Worker worker) {
        assertEquals(0, worker.getActiveWorks());
        assertEquals(0, worker.getClosedWorks());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(DomainCustomizer.class)
    void sut_sets_only_included_fields(Operator operator) {
        assertNotNull(operator.getTeamName());
        assertNotNull(operator.getPhoneNumber());
        assertEquals(0, operator.getActiveWorks());
        assertEquals(0, operator.getClosedWorks());
    }

    @ParameterizedTest
    @AutoSource
    void including_accumulates_conditions(ObjectGenerationContext context) {
        // Arrange
        InstanceFieldWriter sut = new InstanceFieldWriter(Operator.class)
            .excluding("activeWorks")
            .including("teamName", "activeWorks");

        context.customizeGenerator(sut);

        // Act
        Operator actual = context.generate(Operator.class);

        // Assert
        assertNotNull(actual.getTeamName());
        assertEquals(0, actual.getActiveWorks());
    }

    @ParameterizedTest
    @AutoSource
    void excluding_accumulates_conditions(ObjectGenerationContext context) {
        // Arrange
        InstanceFieldWriter sut = new InstanceFieldWriter(Operator.class)
            .including("teamName", "activeWorks")
            .excluding("activeWorks");

        context.customizeGenerator(sut);

        // Act
        Operator actual = context.generate(Operator.class);

        // Assert
        assertNull(actual.getPhoneNumber());
        assertEquals(0, actual.getActiveWorks());
    }

}
