# zb (Zero Dependencies Builder)

A lightweight Java build tool that requires zero external dependencies. Built using pure Java 25 functionality.

<img src="dukebuilder.png" alt="Duke Builder" width="200">


## Features

- 🚀 Zero external dependencies
- ⚡ Fast compilation and packaging
- 🔍 Automatic main class detection
- 📦 Executable JAR generation
- 🎯 Simple and intuitive usage

## Prerequisites

- Java 21 or later
- Git (for cloning the repository)

## Installation

### Build from Source

```bash
# Clone the repository
git clone https://github.com/AdamBien/zb.git
cd zb

# Build with Maven
mvn clean package

# The executable JAR will be in target/zb.jar
# Copy it along with the shell wrapper to your desired location
cp target/zb.jar src/main/sh/zb.sh ~/bin/
chmod +x ~/bin/zb.sh
```

### Shell Wrapper

The `zb.sh` script provides a convenient way to run zb without typing `java -jar`:

```bash
# Instead of: java -jar zb.jar
zb.sh
```

## Usage

### Basic Usage

```bash
# Compile and package with defaults
java -jar zb.jar

# Custom source directory
java -jar zb.jar src/main/java

# Custom source and output directories
java -jar zb.jar src/main/java target/classes target/jar

# Full customization (source, classes, jar directory, jar filename)
java -jar zb.jar src/main/java target/classes target/jar myapp.jar
```

### Default Configuration

| Parameter | Default Value |
|-----------|---------------|
| Source Directory | `src/main/java`, `src` or current directory |
| Classes Directory | `<temp.dir>` (temporary directory) |
| JAR Output Directory | `zbo` |
| JAR Filename | `app.jar` |

## Configuration File (.zb)

zb supports configuration through a `.zb` properties file in your project root. If not present, zb will automatically create one with default values on first run.

### Supported Properties

| Property | Description | Default Value |
|----------|-------------|---------------|
| `sources.dir` | Source directory path | `<discovered by zb>` |
| `resources.dir` | Resources directory path | `<discovered by zb>` |
| `classes.dir` | Compiled classes output directory | `<temp.dir>` |
| `jar.dir` | JAR output directory | `zbo/` |
| `jar.file.name` | Name of the generated JAR file | `app.jar` |
| `post.build.hook` | Script to execute after a successful build | `<none>` |

### Example Configuration

```properties
# .zb configuration file
sources.dir=src/main/java
resources.dir=src/main/resources
classes.dir=<temp.dir>  # or specify a custom directory like target/classes
jar.dir=target/
jar.file.name=myapp.jar
```

### Auto-Discovery

When `sources.dir` or `resources.dir` are set to `<discovered by zb>`, the tool will automatically:
- Search for source directories in common locations (`src/main/java`, `src`, or current directory)
- Locate resource directories relative to the source directory

### Temporary Directory for Classes

When `classes.dir` is set to `<temp.dir>`, zb will:
- Create a unique temporary directory for compiled classes
- Display the temporary directory path during build
- Automatically clean up the directory after JAR creation
- This is the default behavior to avoid cluttering your project

### Post-Build Hook

zb can execute any script after a successful build. Configure `post.build.hook` in your `.zb` file:

```properties
# Run zunit tests after every successful build
post.build.hook=zunit
```

The hook receives build context as environment variables: `ZB_JAR_PATH`, `ZB_SOURCE_DIR`, `ZB_JAR_DIR`, `ZB_JAR_FILE_NAME`. A non-zero exit code is logged as a warning but does not fail the build.

## How It Works

1. **Source Discovery**: Automatically finds all Java files in the source directory
2. **Main Class Detection**: Identifies the main class for the executable JAR
3. **Compilation**: Compiles all Java files to bytecode
4. **Packaging**: Creates an executable JAR with proper manifest

## Testing with zunit

[zunit](https://github.com/AdamBien/zunit) is a zero-dependency, single-file Java test runner that integrates with zb. It reads the `.zb` configuration file to resolve the JAR classpath automatically:

```bash
# Build with zb, then run tests with zunit
zb && zunit
```

A [/zunit skill](https://github.com/AdamBien/airails/tree/main/java/zunit) is available for AI-assisted generation and execution of zunit tests.

## AI-Assisted Development

zb includes a [SKILL.md](SKILL.md) for use with [airails.dev](https://airails.dev) AI-assisted development workflows.

## Videos

[![zb - Zero Dependencies Builder](https://img.youtube.com/vi/7Bes0O3bPwo/0.jpg)](https://www.youtube.com/watch?v=7Bes0O3bPwo)
