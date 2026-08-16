package airhacks.zb.compiler.control;


import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.ToolProvider;

public interface Compiler {


  public static boolean compile(List<Path> javaFiles, Path outputDirectory) {
    return compile(javaFiles, outputDirectory, List.of());
  }

  public static boolean compile(List<Path> javaFiles, Path outputDirectory, List<Path> classpath) {
    var javac = ToolProvider.getSystemJavaCompiler();
    var fm = javac.getStandardFileManager(null, null, null);
    var cus = fm.getJavaFileObjectsFromPaths(javaFiles);
    var options = new ArrayList<>(List.of("-d", outputDirectory.toString()));
    if (!classpath.isEmpty()) {
      var joined = classpath.stream()
          .map(Path::toString)
          .collect(Collectors.joining(File.pathSeparator));
      options.addAll(List.of("--class-path", joined));
    }
    var task = javac.getTask(null, fm, null, options, null, cus);
    return task.call();
  }
}
