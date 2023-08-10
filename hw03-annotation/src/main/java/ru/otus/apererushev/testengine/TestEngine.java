package ru.otus.apererushev.testengine;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestEngine {

    private TestEngine() { }

    private static final String TEST_ENGINE = ConsoleColors.CYAN + "[TEST ENGINE]" + ConsoleColors.RESET;

    public static void test(Class<?> clazz) {
        System.out.printf("%s --== START ==--\n", TEST_ENGINE);
        try {
            runTestMethods(clazz);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new TestReflectionException(e);
        }
        System.out.printf("%s --== END ==--\n", TEST_ENGINE);
    }

    private static List<Method> getAnnotatedMethods(@NotNull final Class<?> clazz, @NotNull final Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> Objects.nonNull(m.getAnnotation(annotationClass)))
                .toList();
    }

    private static @NotNull Object newInstanceOf(@NotNull final Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    private static void runAnnotatedMethodsOf(@NotNull final Object object, @NotNull final Class<? extends Annotation> annotation)
            throws InvocationTargetException, IllegalAccessException {
        var beforeMethods = getAnnotatedMethods(object.getClass(), annotation);
        for (var before : beforeMethods) {
            before.invoke(object);
        }
    }

    private static void runTestMethods(@NotNull final Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var testMethods = getAnnotatedMethods(clazz, Test.class);

        int ok = 0;
        int ignored = 0;
        for (var method : testMethods) {
            if (method.isAnnotationPresent(Ignore.class)) {
                System.out.printf(getTestResultMessage(TestResult.IGNORED, method));
                ignored++;
                continue;
            }
            var test = newInstanceOf(clazz);
            runAnnotatedMethodsOf(test, Before.class);
            try {
                method.invoke(test);
                System.out.printf(getTestResultMessage(TestResult.OK, method));
                ok++;
            } catch (Exception e) {
                System.out.printf(getTestResultMessage(TestResult.ERROR, method));
            } finally {
                runAnnotatedMethodsOf(test, After.class);
            }
        }

        System.out.printf(
                "%s Total: %s, Ok: %s, Error: %s, Ignored: %s%n",
                TEST_ENGINE,
                testMethods.size(),
                ok,
                testMethods.size() - ok - ignored,
                ignored
        );
    }

    private static @NotNull String getMethodDescription(@NotNull final Method method) {
        if (method.isAnnotationPresent(Description.class)) {
            return method.getAnnotation(Description.class).value();
        } else {
            return method.getName() + "()";
        }
    }

    private static @NotNull String getTestResultMessage(@NotNull final TestResult testResult, @NotNull Method method) {
        var result = "";
        if (testResult == TestResult.IGNORED) {
            result = ConsoleColors.YELLOW + "Ignored";
        } else if (testResult == TestResult.OK) {
            result = ConsoleColors.GREEN + "Ok";
        } else if (testResult == TestResult.ERROR) {
            result = ConsoleColors.RED + "ERROR";
        }

        return "%s %s - %s%s\n".formatted(TEST_ENGINE, getMethodDescription(method), result, ConsoleColors.RESET);
    }

}
