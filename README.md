# zb (Zero Dependencies Builder)

A lightweight Java build tool that requires zero external dependencies. Built using pure Java 21 functionality.

![Duke Builder](dukebuilder.png)


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
# Copy it to your desired location
cp target/zb.jar ~/bin/zb.jar
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
| Classes Directory | `zbo/classes` |
| JAR Output Directory | `zbo` |
| JAR Filename | `app.jar` |

## Configuration File (.zb)

zb supports configuration through a `.zb` properties file in your project root. If not present, zb will automatically create one with default values on first run.

### Supported Properties

| Property | Description | Default Value |
|----------|-------------|---------------|
| `sources.dir` | Source directory path | `<discovered by zb>` |
| `resources.dir` | Resources directory path | `<discovered by zb>` |
| `classes.dir` | Compiled classes output directory | `zbo/classes` |
| `jar.dir` | JAR output directory | `zbo/` |
| `jar.file.name` | Name of the generated JAR file | `app.jar` |

### Example Configuration

```properties
# .zb configuration file
sources.dir=src/main/java
resources.dir=src/main/resources
classes.dir=target/classes
jar.dir=target/
jar.file.name=myapp.jar
```

### Auto-Discovery

When `sources.dir` or `resources.dir` are set to `<discovered by zb>`, the tool will automatically:
- Search for source directories in common locations (`src/main/java`, `src`, or current directory)
- Locate resource directories relative to the source directory

## How It Works

1. **Source Discovery**: Automatically finds all Java files in the source directory
2. **Main Class Detection**: Identifies the main class for the executable JAR
3. **Compilation**: Compiles all Java files to bytecode
4. **Packaging**: Creates an executable JAR with proper manifest
