/// # Log
/// > Print color-coded, severity-tagged messages to the console.
///
/// ## Boundary
/// - `log-user` — tell the user about build progress
/// - `log-warning` — warn about a recoverable problem
/// - `log-error` — report a failure
/// - `log-debug` — emit diagnostic detail
///
/// ## Requirements
/// ### R1: Severity-colored output
/// - R1.1 — When a message is logged, the BC shall render it in the color assigned to its severity.
/// - R1.2 — When an error is logged, the BC shall write it to the error stream.
/// - R1.3 — When a non-error message is logged, the BC shall write it to standard output.
///
/// ## Out of scope
/// - Log files, log-level filtering, and structured logging.
/// - Composing hint or guidance texts (owned by `hints`).
package airhacks.zb.log;
