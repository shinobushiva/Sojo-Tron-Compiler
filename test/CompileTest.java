import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CompileTest {

	public static String read(File f) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(f));

		StringBuilder buf = new StringBuilder();
		String line = null;
		while ((line = r.readLine()) != null) {
			buf.append(line).append("\n");
		}
		return buf.toString();
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {

		String cName1 = "MyTronBotStraight.java";
		String cName2 = "MyTronBotStraight2.java";

		// URL url = new URL(
		// "http://localhost:8080/SojoTronCompiler/CompilerServlet");
		URL url = new URL(
				"http://202.16.132.23/apps/SojoTronCompiler/CompilerServlet");
		URLConnection uc = url.openConnection();
		uc.setDoOutput(true);// POST可能にする

		OutputStream os = uc.getOutputStream();// POST用のOutputStreamを取得
		// POSTするデータ
		String postStr = "source1="
				+ URLEncoder.encode(read(new File("WebContent/WEB-INF/data/"
						+ cName1)), "UTF-8")
				+ "&"
				+ "source2="
				+ URLEncoder.encode(read(new File("WebContent/WEB-INF/data/"
						+ cName2)), "UTF-8")
				+ "&"
				+ "map="
				+ URLEncoder.encode(read(new File("WebContent/WEB-INF/maps/"
						+ "apocalyptic.txt")), "UTF-8");
		PrintStream ps = new PrintStream(os);
		ps.print(postStr);// データをPOSTする
		ps.close();

		System.out.println("post!");

		InputStream is = uc.getInputStream();// POSTした結果を取得
		{
			XMLDecoder dec = new XMLDecoder(new BufferedInputStream(is));
			String[] vals = (String[]) dec.readObject();
			dec.close();

			String win = vals[0];
			String result = vals[1];

			System.out.println(win);
			System.out.println(result);
		}

	}

	public static class TronClassLoader extends ClassLoader {

		private byte[] ba;

		public TronClassLoader(byte[] ba, ClassLoader p) {
			super(p);
			this.ba = ba;
		}

		public Class<?> findClass(String name) {
			byte[] b = loadClassData(name);
			return defineClass(name, b, 0, b.length);
		}

		private byte[] loadClassData(String name) {
			return ba;
		}
	}
}
