package org.kakapo.db.internal.test.common;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public class BaseKakapoDbAbstractTest {
    protected final Logger log = LogManager.getLogger(getClass());

    private long testStartNanos;

    @BeforeEach
    void printStartMessage(TestInfo testInfo) {
        log.info(">>> Starting test: {}#{}, displayName: {}",
                testInfo.getTestClass().map(Class::getSimpleName).orElse("<null>"),
                testInfo.getTestMethod().map(Method::getName).orElse("<null>"),
                testInfo.getDisplayName()
        );

        this.testStartNanos = System.nanoTime();
    }

    @AfterEach
    void printStopMessage(TestInfo testInfo) {
        log.info(">>> Stopping test: {}#{}, displayName: {}, cost: {}ms.",
                testInfo.getTestClass().map(Class::getSimpleName).orElse("<null>"),
                testInfo.getTestMethod().map(Method::getName).orElse("<null>"),
                testInfo.getDisplayName(),
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - testStartNanos)
        );
    }
}
