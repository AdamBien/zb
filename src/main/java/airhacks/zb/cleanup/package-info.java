/// # Cleanup
/// > Remove transient compilation output after the JAR is packaged.
///
/// ## Boundary
/// - `clean-classes` — delete a classes directory and everything beneath it
///
/// ## Requirements
/// ### R1: Clean the classes directory
/// - R1.1 — When a classes directory is handed over for cleanup, the BC shall delete the directory tree, contents before containers.
/// - R1.2 — If the directory tree cannot be traversed, then the BC shall report the failure to the user without failing the build. _(why: a leftover temp directory is cosmetic; the JAR is already built)_
///
/// ## Out of scope
/// - Deciding whether cleanup runs — the caller cleans only temporary classes directories.
/// - Removing the packaged JAR or other build outputs.
package airhacks.zb.cleanup;
