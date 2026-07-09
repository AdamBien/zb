/// # Hints
/// > Turn missing or broken build inputs into actionable guidance for the user.
///
/// ## Boundary
/// - `show-build-hints` — inspect the discovered inputs and explain everything that is missing
/// - `report-error` — surface a failure to the user
///
/// ## Requirements
/// ### R1: Guide on missing inputs
/// - R1.1 — If the source directory does not exist, then the BC shall report it and show usage guidance with an example invocation.
/// - R1.2 — If no Java files were found, then the BC shall warn the user and suggest checking the directory.
/// - R1.3 — If no main class was found, then the BC shall warn the user and explain how to declare one.
///
/// ### R2: Report failures
/// - R2.1 — When an error is reported, the BC shall present the message to the user as an error.
/// - R2.2 — If a directory cannot be accessed, then the BC shall report it and stop the build.
///
/// ## Out of scope
/// - Detecting the problems themselves (owned by `discovery` and the callers).
/// - Message rendering and severity colors (owned by `log`).
package airhacks.zb.hints;
