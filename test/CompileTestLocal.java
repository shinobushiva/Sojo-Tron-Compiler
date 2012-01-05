import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javaxtools.compiler.CompilerUtils;

public class CompileTestLocal {

	private static String readString(File f) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(f));

		StringBuilder buf = new StringBuilder();
		String ll = null;
		while ((ll = r.readLine()) != null) {
			buf.append(ll).append("\n");
		}

		return buf.toString();
	}

	public static void main(String[] args) throws Exception {

		File f = new File("WebContent/WEB-INF/data/MyTronBotStraight.java");
		System.out.println(f.getAbsolutePath());
		BufferedReader r = new BufferedReader(new FileReader(f));

		StringBuilder buf = new StringBuilder();
		String ll = null;
		while ((ll = r.readLine()) != null) {
			buf.append(ll).append("\n");
		}

		String source = buf.toString();
		String filename = "MyTronBotStraight.java";

		String cName = filename.split("\\.")[0];

		// read package name from source code
		int mode = 1;
		String packageName = "";
		BufferedReader br = new BufferedReader(new StringReader(source));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.indexOf("*/") != -1) {
				mode = 1;
			} else if (line.indexOf("/*") != -1) {
				mode = 2;
			} else if (mode == 2) {
			} else if (line.indexOf("//") != -1) {
				line = line.replaceAll("//.*", "");
			}
			if (mode == 1) {
				if (line.contains("package")) {
					packageName = line.replace("package", "").replace(";", "")
							.trim();
				}
			}
		}

		Map<String, CharSequence> srcMap = new HashMap<String, CharSequence>();

		srcMap.put(packageName + "." + cName, source);

		StringBuilder sb = new StringBuilder();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		System.out.println(cl);
		URLClassLoader ucl = new URLClassLoader(new URL[0], cl);
		System.out.println(ucl);

		Map<String, Class<Object>> compile = CompilerUtils.compile(srcMap, sb,
				ucl);
		System.out.println(compile);
		System.out.println(compile.get(packageName + "." + cName)
				.getClassLoader());
		System.out.println(compile.get(packageName + "." + cName)
				.getClassLoader().getParent());

		// ISimpleCompiler compiler = CompilerFactoryFactory
		// .getDefaultCompilerFactory().newSimpleCompiler();
		// compiler.cook(source);
		// Class<?> loadClass = compiler.getClassLoader().loadClass(
		// packageName + "." + cName);
		// System.out.println(loadClass);

		// PrintStream ps = new PrintStream(os);
		// ps.print(postStr);// データをPOSTする
		// ps.close();
		//
		// InputStream is = uc.getInputStream();// POSTした結果を取得
		// BufferedReader bis = new BufferedReader(new InputStreamReader(is));
		// String l = null;
		// while ((l = bis.readLine()) != null) {
		// System.out.println(l);
		// }

		// XMLDecoder d = new XMLDecoder(is);
		//
		// Object readObject = d.readObject();
		// System.out.println(readObject);

	}
}
