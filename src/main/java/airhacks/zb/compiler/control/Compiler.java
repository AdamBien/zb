package airhacks.zb.compiler.control;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.tools.ToolProvider;

import airhacks.zb.discovery.control.JavaFiles;

interface Compiler {

  static boolean compile(Path rootDirectory) throws IOException {
    var javaFiles =  JavaFiles.findFrom(rootDirectory);
    return compile(javaFiles);
  }

  static boolean compile(List<Path> javaFiles) {
    var javac = ToolProvider.getSystemJavaCompiler();
    var fm = javac.getStandardFileManager(null, null, null);
    var cus = fm.getJavaFileObjectsFromPaths(javaFiles);
    var task = javac.getTask(null, fm, null, null, null, cus);
    return task.call();

  }
}
