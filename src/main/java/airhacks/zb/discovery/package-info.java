/// # Discovery
/// > Locate the build inputs: source and resources directories, Java sources, the main class, and service configuration files.
///
/// ## Boundary
/// - `find-java-sources` — collect all Java source files beneath a root directory
/// - `select-main-class` — determine the single main class among the discovered sources
/// - `detect-source-directory` — pick the conventional source root of the project
/// - `detect-resources-directory` — pick the conventional resources root of the project
/// - `find-service-configuration-files` — collect service configuration files beneath a root directory
///
/// ## Requirements
/// ### R1: Find Java sources
/// - R1.1 — When a root directory is provided, the BC shall return every Java source file beneath it.
/// - R1.2 — If the root directory cannot be read, then the BC shall report the failure to the user and return no files.
///
/// ### R2: Select the main class
/// - R2.1 — When exactly one source declares a main method, the BC shall select it.
/// - R2.2 — If no source declares a main method, then the BC shall warn the user and select none.
/// - R2.3 — While multiple sources declare a main method, when a main class is configured, the BC shall select the source matching the configured class name.
/// - R2.4 — If multiple sources declare a main method and no main class is configured, then the BC shall instruct the user to configure one and stop the build. _(why: guessing among several entry points produces a JAR that starts the wrong app)_
/// - R2.5 — If the configured main class matches none of the candidates, then the BC shall report the mismatch and stop the build.
///
/// ### R3: Detect the source directory
/// - R3.1 — When the source directory is requested, the BC shall return the first existing conventional candidate, preferring `src/main/java`, then `src`, then the working directory.
///
/// ### R4: Detect the resources directory
/// - R4.1 — When the resources directory is requested, the BC shall return the first existing conventional candidate, preferring `src/main/resources`, then `resources`.
/// - R4.2 — If no candidate exists, then the BC shall indicate that the project has no resources.
///
/// ### R5: Find service configuration files
/// - R5.1 — When a root directory is provided, the BC shall return every service configuration file (`META-INF/services`) beneath it.
///
/// ## Out of scope
/// - Compiling or packaging the discovered files.
/// - Explaining missing inputs to the user (owned by `hints`).
package airhacks.zb.discovery;
