package javaxtools.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class CompilerUtils {

	public String getClassPath() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL[] urls = ((URLClassLoader) classLoader).getURLs();
		StringBuilder buf = new StringBuilder(1000);
		buf.append(".");
		String separator = System.getProperty("path.separator");
		for (URL url : urls) {
			buf.append(separator).append(url.getFile());
		}
		return buf.toString();
	}

	public static CharSequenceCompiler<Object> csc;

	public static Map<String, Class<Object>> compile(
			Map<String, CharSequence> map, StringBuilder sb, ClassLoader cl) {
		csc = new CharSequenceCompiler<Object>(cl, Arrays.asList(new String[] {
				"-verbose", "-target", "1.6" }));
		// CharSequenceCompiler<Object> csc = new
		// CharSequenceCompiler<Object>(cl,
		// Arrays.asList(new String[] {}));

		// CharSequenceCompiler<Object> csc = new
		// CharSequenceCompiler<Object>(cl,
		// new ArrayList<String>());

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

		Map<String, Class<Object>> c = null;
		try {
			c = csc.compile(map, diagnostics);
		} catch (CharSequenceCompilerException e) {
			e.printStackTrace();

			for (Diagnostic<? extends JavaFileObject> diagnostic : e
					.getDiagnostics().getDiagnostics()) {
				// System.err.println(diagnostic);
				sb.append(diagnostic).append("\n");
			}
		}

		return c;
	}
}
