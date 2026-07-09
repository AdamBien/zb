/// # Configuration
/// > Provide build settings from the project's `.zb` configuration file, creating it with defaults on first contact.
///
/// ## Boundary
/// - `read-setting` — look up a build setting, falling back to a caller-supplied default
/// - `initialize-configuration` — create the configuration file with all settings and their defaults if absent
///
/// ## Requirements
/// ### R1: Read a setting
/// - R1.1 — When a setting is requested, the BC shall return its configured value.
/// - R1.2 — If a setting is absent or marked as discovered, then the BC shall return the caller-supplied default. _(why: discovery at build time beats stale hardcoded paths)_
/// - R1.3 — If the configuration file cannot be read, then the BC shall report the failure and return the caller-supplied default.
///
/// ### R2: Initialize the configuration file
/// - R2.1 — If no configuration file exists, then the BC shall create one listing every setting with its default, and tell the user.
/// - R2.2 — While a configuration file exists, the BC shall never overwrite it.
///
/// ## Out of scope
/// - Command-line argument parsing and precedence (owned by the application shell).
/// - Discovering source or resources directories (owned by `discovery`).
package airhacks.zb.configuration;
