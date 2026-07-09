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
/// ## Out of scope
/// - Discovering which files to compile (owned by `discovery`).
/// - Creating the output directory (owned by `prereqs`).
/// - Classpath or dependency resolution — zb builds dependency-free projects.
package airhacks.zb.compiler;
