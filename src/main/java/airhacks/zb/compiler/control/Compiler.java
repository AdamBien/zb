
import java.util.List;
import static java.lang.System.out;

import java.nio.file.Path;

import javax.tools.Tool;
import javax.tools.ToolProvider;

import airhacks.zb.discovery.control.JavaFiles;

interface Compiler {

  static boolean compile(Path rootDirectory) {
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
