
import java.util.List;
import static java.lang.System.out;

import java.nio.file.Path;

import javax.tools.Tool;
import javax.tools.ToolProvider;

interface Compiler {

  static boolean compile(Path rootDirectory) {
    var javac = ToolProvider.getSystemJavaCompiler();
    var fm = javac.getStandardFileManager(null, null, null);
    var cus = fm.getJavaFileObjects(rootDirectory);
    var task = javac.getTask(null, fm, null, null, null, cus);
    return task.call();
  }
}
