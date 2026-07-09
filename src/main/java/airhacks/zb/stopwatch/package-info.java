/// # Stopwatch
/// > Measure and report the elapsed build time.
///
/// ## Boundary
/// - `start-measurement` — capture the moment the build starts
/// - `stop-measurement` — compute the elapsed time and report it
///
/// ## Requirements
/// ### R1: Measure elapsed time
/// - R1.1 — When a measurement is started, the BC shall capture the current instant.
/// - R1.2 — When a measurement is stopped, the BC shall report the elapsed time in seconds and milliseconds to the user. _(why: the build summary reports build time only)_
///
/// ## Out of scope
/// - Timing individual build phases.
package airhacks.zb.stopwatch;
