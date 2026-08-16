/// # Compiler
/// > Compile a set of Java source files into a classes directory.
///
/// ## Boundary
/// - `compile-sources` — compile the given Java source files into an output directory
///
/// ## Requirements
/// ### R1: Compile sources
/// - R1.1 — When Java source files and an output directory are provided, the BC shall compile every file into the output directory, preserving package structure.
/// - R1.2 — When all files compile, the BC shall report success.
/// - R1.3 — If any file fails to compile, then the BC shall report failure.
///
/// ### R2: Compile against a classpath
/// - R2.1 — When classpath entries are configured, the BC shall pass them to javac via `--class-path`.
/// - R2.2 — While no classpath is configured, the BC shall compile without a classpath option.
///
/// ## Out of scope
/// - Discovering which files to compile (owned by `discovery`).
/// - Creating the output directory (owned by `prereqs`).
/// - Dependency resolution or download — classpath entries are given, never fetched.
/// - Validating that classpath entries exist (owned by the caller via `hints`).
package airhacks.zb.compiler;
