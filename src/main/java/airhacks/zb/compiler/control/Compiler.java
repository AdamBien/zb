package airhacks.zb.compiler.control;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.tools.ToolProvider;

import airhacks.zb.discovery.control.JavaFiles;

interface Compiler {

  static boolean compile(Path rootSourceDirectory,Path rootOutputDirectory) throws IOException {
    var javaFiles =  JavaFiles.findFrom(rootSourceDirectory);
    return compile(javaFiles, rootOutputDirectory);
  }

  static boolean compile(List<Path> javaFiles, Path outputDirectory) {
    var javac = ToolProvider.getSystemJavaCompiler();
    var fm = javac.getStandardFileManager(null, null, null);
    var cus = fm.getJavaFileObjectsFromPaths(javaFiles);
    var options = List.of("-d", outputDirectory.toString());
    var task = javac.getTask(null, fm, null, options, null, cus);
    return task.call();
  }
}
