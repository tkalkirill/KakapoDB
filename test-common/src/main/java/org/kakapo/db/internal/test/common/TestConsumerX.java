package org.kakapo.db.internal.test.common;

/** Consumer that may throw {@link Exception} when executed. */
@FunctionalInterface
public interface TestConsumerX<T> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t Input argument.
     * @throws Exception If any error occurs.
     */
    void accept(T t) throws Exception;
}
