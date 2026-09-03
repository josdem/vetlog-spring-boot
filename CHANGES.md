# Changes

## Issue

- Issue: #923
- Title: As admin, I am not able to search an user if username is 5 characters long
- URL: https://github.com/josdem/vetlog-spring-boot/issues/923

## Problem

The `UsernameCommand` class had a `@Size(min = 6, max = 50)` validation constraint. This meant usernames with exactly 5 characters (e.g., "Orion") were rejected with a validation error instead of being accepted and searched properly.

## Changes Made

- Updated `UsernameCommand.java`: changed `@Size(min = 6, max = 50)` to `@Size(min = 5, max = 50)` to allow 5-character usernames.

## Implementation Details

File changed: `src/main/java/com/josdem/vetlog/command/UsernameCommand.java`
The validation annotation minimum value was reduced from 6 to 5, allowing 5-character usernames to pass validation and be processed by the `VetController.search()` method.

## Testing

- Build/test command attempted: `./gradlew test`
- Result: Gradle wrapper download failed due to network timeout (`java.net.SocketTimeoutException`); this is an infrastructure limitation, not a code failure.
- The code change is a single-character adjustment (`6` → `5`) directly addressing the validation constraint described in the issue.

## Result

The fix allows usernames with 5 characters to pass validation, resolving the reported search failure for users with 5-character usernames.
