# zb - Agent Instructions

Use zb to build Java 21+ projects without external dependencies.

## When to Use

- Java projects with `void main()` entry point
- No Maven/Gradle dependency resolution — external JARs are listed explicitly via `classpath` in `.zb`
- Single-module applications

## Build Command

```bash
java -jar ~/bin/zb.jar
```

Or with shell wrapper:

```bash
zb.sh
```

## Source Detection

zb auto-detects sources in this order:
1. `src/main/java`
2. `src/`
3. Current directory

## Output

Executable JAR: `zbo/app.jar`

## Configuration

Optional `.zb` file in project root:

```properties
sources.dir=src/main/java
resources.dir=src/main/resources
classes.dir=<temp>
jar.dir=zbo/
jar.file.name=app.jar
classpath=lib/a.jar:lib/b.jar
```

## Verification

Run the built JAR:

```bash
java -jar zbo/app.jar
```

## Publishing (zpublish)

`zpublish` is a single-file Java 25 script in the repository root that publishes the built
JAR to Maven Central. It never builds — build first. Configuration is split: `.zb` supplies
the build paths (`jar.dir`, `jar.file.name`, `classpath`), `.zpublish` supplies the POM
metadata. Never write to `.zb` from `zpublish`; the build tool owns that file.

Its tests live inside the script, not under `src/`, so zunit does not reach them and
`java -jar zb.jar` does not exercise them:

```bash
java --source 25 zpublish -selftest   # in-script assertions
java --source 25 zpublish -dry-run    # stage, sign, checksum, bundle - no upload
```

Both run on every pull request and every push to `main`
(`.github/workflows/publish-central.yml`).
