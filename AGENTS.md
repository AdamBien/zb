# zb - Agent Instructions

Use zb to build Java 21+ projects without external dependencies.

## When to Use

- Java projects with `void main()` entry point
- No Maven/Gradle dependencies required
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
```

## Verification

Run the built JAR:

```bash
java -jar zbo/app.jar
```
