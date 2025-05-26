package airhacks.zb.compiler.control;


import java.nio.file.Path;
import java.util.List;

import javax.tools.ToolProvider;

public interface Compiler {

 
  public static boolean compile(List<Path> javaFiles, Path outputDirectory) {
    var javac = ToolProvider.getSystemJavaCompiler();
    var fm = javac.getStandardFileManager(null, null, null);
    var cus = fm.getJavaFileObjectsFromPaths(javaFiles);
    var options = List.of("-d", outputDirectory.toString());
    var task = javac.getTask(null, fm, null, options, null, cus);
    return task.call();
  }
}
