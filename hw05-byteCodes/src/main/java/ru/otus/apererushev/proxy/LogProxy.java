package ru.otus.apererushev.proxy;

import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.logger.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("java:S106")
public class LogProxy<T> {

    private final Class<T> proxyInterface;

    public LogProxy(@NotNull final Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;
    }

    @SuppressWarnings({"unchecked", "java:S3878"})
    public T createInstanceWithLodging(Class<? extends T> clazz) {
        try {
            T instance = clazz.getConstructor(new Class<?>[0]).newInstance();
            var handler = new LogProxy.InvocationHandlerImpl<>(instance);

            return (T)Proxy.newProxyInstance(
                    LogProxy.class.getClassLoader(),
                    new Class<?>[] {proxyInterface},
                    handler
            );
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new LogProxyException(e.getMessage(), e);
        }
    }

    static class InvocationHandlerImpl<T> implements InvocationHandler {
        private final T forLogging;

        private final List<Method> loggingMethods;

        InvocationHandlerImpl(T forLogging) {
            this.forLogging = forLogging;
            this.loggingMethods = getLoggedMethods(forLogging.getClass());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            for (var loggingMethod : loggingMethods) {
                if (compareMethodSignatures(loggingMethod, method)) {
                    var logBuilder = new StringBuilder();
                    logBuilder.append("[LOG] executed method: %s.%s".formatted(forLogging.getClass(), method.getName()));
                    if (method.getParameterCount() > 0) {
                        logBuilder.append(", parameters: ");
                        for (var i = 0; i < method.getParameterCount(); i++) {
                            var param = method.getParameters()[i];
                            logBuilder.append("%s: %s; ".formatted(param.getName(), args[i]));
                        }
                    }
                    System.out.println(logBuilder);
                }
            }

            return method.invoke(forLogging, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + forLogging + '}';
        }

        private @NotNull List<Method> getLoggedMethods(@NotNull final Class<?> clazz) {
            return Arrays.stream(clazz.getMethods())
                    .filter(m -> Objects.nonNull(m.getAnnotation(Log.class)))
                    .toList();
        }

        public @NotNull Boolean compareMethodSignatures(@NotNull final Method method1, @NotNull final Method method2) {
            // Сравниваем имена методов
            if (! method1.getName().equals(method2.getName())) {
                return false;
            }

            // Сравниваем возвращаемые типы
            if (!method1.getReturnType().equals(method2.getReturnType())) {
                return false;
            }

            // Сравниваем параметры
            if (method1.getParameterCount() != method2.getParameterCount()) {
                return false;
            }

            var params1 = method1.getParameterTypes();
            var params2 = method2.getParameterTypes();

            for (int i = 0; i < params1.length; i++) {
                if (!params1[i].equals(params2[i])) {
                    return false;
                }
            }

            return true;
        }
    }
}
