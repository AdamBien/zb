# zb (Zero Dependencies Build)

A lightweight Java build tool that requires zero external dependencies. Built using pure Java 21 functionality.

![Duke Builder](dukebuilder.png)


## Features

- 🚀 Zero external dependencies
- ⚡ Fast compilation and packaging
- 🔍 Automatic main class detection
- 📦 Executable JAR generation
- 🎯 Simple and intuitive usage
- 💻 Cross-platform compatibility

## Prerequisites

- Java 21 or later
- Git (for cloning the repository)

## Installation

### Build from Source

```bash
# Clone the repository
git clone https://github.com/[username]/zb.git
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
| Source Directory | `src/main/java` |
| Classes Directory | `zb/classes` |
| JAR Output Directory | `zb/jar` |
| JAR Filename | `zb.jar` |

## How It Works

1. **Source Discovery**: Automatically finds all Java files in the source directory
2. **Main Class Detection**: Identifies the main class for the executable JAR
3. **Compilation**: Compiles all Java files to bytecode
4. **Packaging**: Creates an executable JAR with proper manifest
