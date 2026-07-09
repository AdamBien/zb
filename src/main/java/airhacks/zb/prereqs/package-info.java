/// # Prereqs
/// > Ensure the directories a build needs exist before compilation and packaging.
///
/// ## Boundary
/// - `ensure-directory` — create a directory, including missing parents, if it does not exist
///
/// ## Requirements
/// ### R1: Ensure a directory exists
/// - R1.1 — When a directory is requested, the BC shall create it together with any missing parents and confirm the creation to the user.
/// - R1.2 — While the directory already exists, the BC shall leave its contents untouched.
/// - R1.3 — If the directory cannot be created, then the BC shall report the failure to the user.
///
/// ## Out of scope
/// - Deleting or cleaning directories (owned by `cleanup`).
package airhacks.zb.prereqs;
