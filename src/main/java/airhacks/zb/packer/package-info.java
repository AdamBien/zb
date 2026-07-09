/// # Packer
/// > Assemble compiled classes, resources, and version metadata into a runnable JAR.
///
/// ## Boundary
/// - `create-jar` — package classes and resources into a JAR file at a target location
///
/// ## Requirements
/// ### R1: Create the JAR
/// - R1.1 — When a classes directory and a target location are provided, the BC shall package every compiled class into a JAR at that location.
/// - R1.2 — While a resources directory is present, the BC shall include every resource file in the JAR.
/// - R1.3 — If a JAR already exists at the target location, then the BC shall replace it.
/// - R1.4 — If the target directory does not exist, then the BC shall create it.
///
/// ### R2: Make the JAR runnable
/// - R2.1 — When a main class is known, the BC shall record it in the JAR manifest so the JAR is directly executable.
/// - R2.2 — While no main class is known, the BC shall create the JAR without a manifest.
///
/// ### R3: Version metadata
/// - R3.1 — When a `version.txt` exists in the project root, the BC shall record its content as the implementation version in the manifest and include the file in the JAR.
/// - R3.2 — If `version.txt` is absent from the project root but present in the resources directory, then the BC shall use the resources copy. _(why: root placement won over resources when both were supported; root wins on conflict)_
/// - R3.3 — If no `version.txt` exists, then the BC shall omit version metadata.
/// - R3.4 — The BC shall include `version.txt` at most once in the JAR.
///
/// ## Out of scope
/// - Compilation (owned by `compiler`).
/// - Signing, compression tuning, or bundling external dependencies.
package airhacks.zb.packer;
