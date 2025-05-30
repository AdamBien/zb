# zb (Zero Dependencies Build)

zb was built using only Java 21 functionality.

# Build from Source

## Prerequisites
- Java 21 or later


```bash
# Clone the repository
git clone https://github.com/[username]/zb.git
cd zb

# Build with Maven
mvn clean package

# The executable JAR will be in targetzb.jar
# Copy it to your desired location
cp targetzb.jar ~/bin/zb.jar
```


## Basic Usage

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

## Default Directories
- Source: `src/main/java`
- Classes: `zb/classes`
- JAR output: `zb/jar`
- JAR filename: `zb.jar`

## Features
- Automatically discovers all Java files in source directory
- Detects main class automatically
- Compiles all Java files
- Packages into executable JAR with manifest
