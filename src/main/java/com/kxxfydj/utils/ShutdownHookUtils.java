package com.kxxfydj.utils;

import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 程序退出钩子
 */
public class ShutdownHookUtils {
    private static final Logger log = getLogger(ShutdownHookUtils.class);

    private ShutdownHookUtils() {
    }

    public static void hook(final ExecutorService executor, final int timeoutSeconds) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Into shutdown hook!");
            if (executor == null){
                return;
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                    log.warn("Executor did not terminate in the specified time.");
                    List<Runnable> droppedTasks = executor.shutdownNow();
                    log.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }));
    }

    public static void hook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable));
    }

}
