/// # Build
/// > Orchestrate one compile-and-package run: discovery → configuration → compiler → packer → cleanup.
///
/// ## Boundary
/// - `perform` — run a single build for the given arguments and report whether it succeeded.
///
/// ## Requirements
/// ### R1: Compile then package
/// - R1.1 — When sources are discovered, the BC shall compile them before packaging.
/// - R1.2 — If compilation fails, then the BC shall stop and report failure without packaging.
/// - R1.3 — When compilation succeeds, the BC shall package the classes and resources into the JAR.
///
/// ### R2: Clean up temporary output
/// - R2.1 — While the classes directory is temporary, the BC shall remove it after the JAR is packaged.
/// - R2.2 — While the classes directory is explicit, the BC shall retain it.
///
/// ## Out of scope
/// - CLI argument parsing (owned by the application shell).
/// - The post-build hook (owned by `hook`); a build only reports success.
package airhacks.zb.build;
