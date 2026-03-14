/*
AsyncConfig.java
Purpose:
 - Configure TaskExecutor for @Async background tasks (emails, notification sending).
 - Set thread pool size, queue capacity based on environment.
 - Provide Named beans for async tasks (e.g., "notificationExecutor").
Notes:
 - Use asynchronous for non-critical flows (send confirmation email after reservation commit).
 - Ensure exception handling, and use CompletableFuture for results if needed.

File: config/AsyncConfig.java
*/

package com.resortmanagement.system.config;

public class AsyncConfig {
    // Async task executor configuration
}