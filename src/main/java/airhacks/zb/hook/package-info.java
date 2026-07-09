/// # Hook
/// > Run a user-configured post-build command after a successful build.
///
/// ## Boundary
/// - `run-post-build-hook` — execute the configured command, if any, after a successful build
///
/// ## Requirements
/// ### R1: Execute the configured hook
/// - R1.1 — When a build succeeds and a hook is configured, the BC shall execute the hook command in the project directory and expose the build outputs (JAR path, source directory, JAR directory, JAR file name) to it.
/// - R1.2 — While no hook is configured, or the configured hook is blank or `<none>`, the BC shall do nothing.
/// - R1.3 — If the hook exits with a non-zero code, then the BC shall warn the user without failing the build.
/// - R1.4 — If the hook cannot be started or is interrupted, then the BC shall warn the user without failing the build.
///
/// ### R2: Guard against recursion
/// - R2.1 — While running inside a hook-spawned process, the BC shall suppress hook execution. _(why: a hook like zunit may invoke zb again — unbounded recursion)_
/// - R2.2 — When executing a hook, the BC shall mark the hook's environment so descendant builds suppress their own hooks.
///
/// ## Out of scope
/// - Deciding whether the build succeeded (the application shell decides).
/// - The hook command's content and side effects.
package airhacks.zb.hook;
