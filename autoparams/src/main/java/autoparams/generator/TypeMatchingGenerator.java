package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class TypeMatchingGenerator implements ObjectGenerator {

    private final Function<Type, Boolean> predicate;
    private final BiFunction<ObjectQuery, ObjectGenerationContext, Object> factory;

    public TypeMatchingGenerator(
        Function<Type, Boolean> predicate,
        BiFunction<ObjectQuery, ObjectGenerationContext, Object> factory
    ) {
        this.predicate = predicate;
        this.factory = factory;
    }

    public TypeMatchingGenerator(
        Supplier<Object> factory,
        Class<?>... candidates
    ) {
        this(buildPredicateWithTypes(candidates), (query, context) -> factory.get());
    }

    public TypeMatchingGenerator(
        Function<ObjectGenerationContext, Object> factory,
        Class<?>... candidates
    ) {
        this(buildPredicateWithTypes(candidates), (query, context) -> factory.apply(context));
    }

    public TypeMatchingGenerator(
        BiFunction<ObjectQuery, ObjectGenerationContext, Object> factory,
        Class<?>... candidates
    ) {
        this(buildPredicateWithTypes(candidates), factory);
    }

    private static Function<Type, Boolean> buildPredicateWithTypes(Class<?>... candidates) {
        return type -> Arrays.stream(candidates).anyMatch(candidate -> match(type, candidate));
    }

    private static boolean match(Type type, Class<?> candidate) {
        return type.equals(candidate)
            || (type instanceof ParameterizedType && match((ParameterizedType) type, candidate));
    }

    private static boolean match(ParameterizedType type, Class<?> candidate) {
        return type.getRawType().equals(candidate);
    }

    public static <T> TypeMatchingGenerator create(
        Class<T> type,
        Supplier<T> factory
    ) {
        return new TypeMatchingGenerator(factory::get, type);
    }

    public static <T> TypeMatchingGenerator create(
        Class<T> type,
        Function<ObjectGenerationContext, T> factory
    ) {
        return new TypeMatchingGenerator(factory::apply, type);
    }

    public static <T> TypeMatchingGenerator create(
        Class<T> type,
        BiFunction<ObjectQuery, ObjectGenerationContext, T> factory
    ) {
        return new TypeMatchingGenerator(factory::apply, type);
    }

    @Override
    public final ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();

        return predicate.apply(type)
            ? new ObjectContainer(factory.apply(query, context))
            : ObjectContainer.EMPTY;
    }
}
