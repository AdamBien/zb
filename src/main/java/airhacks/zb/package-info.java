/// # zb — Zero Dependencies Builder
/// > Compile and package a dependency-free Java project into a runnable JAR with one zero-configuration command.
///
/// ## Vision
/// - A build tool so small and fast it disappears — one readable jar, no install, no waiting.
///
/// ## Components
/// - The application shell (base package `airhacks`) adapts the CLI and delegates to `build`, then runs the post-build `hook`, timed by `stopwatch`.
/// - `build` orchestrates one run: configuration → discovery → prereqs → compiler → packer → cleanup.
/// - Any BC may call `log`; `log` calls no other BC.
/// - `discovery` and `cleanup` may call `hints`; `hints` may call only `log`.
/// - `hook` may call `configuration`.
/// - `compiler` and `packer` call no other BC.
/// - BCs never call the application shell. _(why: the shell composes BCs; an upward call inverts the build's dependency direction)_
///
/// ## System invariants
/// - S1 — If any build step fails, then the system shall not execute the post-build hook.
/// - S2 — While the classes directory is temporary, the system shall remove it after the JAR is packaged. _(why: default builds must not clutter the project or the machine)_
///
/// ## Ubiquitous language
/// - Build — one compile-and-package run that produces the JAR.
/// - Source directory — the root of the Java sources; configured or discovered.
/// - Resources directory — non-Java files bundled into the JAR; configured or discovered.
/// - Classes directory — where compiled classes land; a temporary directory by default.
/// - JAR — the runnable build output.
/// - Main class — the entry point recorded in the JAR manifest.
/// - Configuration file — the `.zb` properties file in the project root. Owned by `configuration`.
/// - Setting — one named value in the configuration file; `<discovered by zb>` defers it to discovery.
/// - Post-build hook — a user-configured command run after a successful build. Owned by `hook`.
/// - Version file — `version.txt`; its content becomes the JAR's implementation version.
///
/// ## Stack
/// - java-cli-app · base package `airhacks.zb` · verification: build with zb (`zb.sh`); zunit runs as the post-build hook
package airhacks.zb;
