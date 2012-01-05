package jp.ac.sojou.cis.izumi.sojotoroncompiler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javaxtools.compiler.CompilerUtils;

/**
 * Servlet implementation class CompilerServlet
 */
public class CompilerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// private final String PN = "jp.ac.sojou.cis.izumi.tron.utils";
	private final String PN = "tron.utils";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CompilerServlet() {
		super();
	}

	private String readString(String p) throws IOException {
		p = getServletContext().getRealPath(p);
		System.out.println("stream:" + p);
		BufferedReader r = new BufferedReader(new FileReader(new File(p)));

		StringBuilder buf = new StringBuilder();
		String ll = null;
		while ((ll = r.readLine()) != null) {
			buf.append(ll).append("\n");
		}
		r.close();

		return buf.toString();
	}

	public static byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();

		return data;
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

	/**
	 * メタオブジェクトにクラスバイト配列を設定します。
	 * 
	 * @param cls
	 * @param o
	 */
	public static byte[] setGetClassBytes(CtClass cls, Object o) {
		try {
			return cls.toBytecode();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * バイト配列からクラスを復元します。
	 * 
	 * @param ba
	 * @return
	 */
	public static CtClass getClass(byte[] ba) {
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ctC = pool.makeClass(new ByteArrayInputStream(ba));
			return ctC;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println(getServletContext().getRealPath("data"));

		System.out.println("post!");

		String className = request.getParameter("className");
		String source = request.getParameter("source");

		Map<String, CharSequence> srcMap = new HashMap<String, CharSequence>();
		srcMap.put(PN + ".Comm", readString("WEB-INF/data/Comm.java"));
		srcMap.put(PN + ".Map", readString("WEB-INF/data/Map.java"));
		srcMap.put(PN + ".Point", readString("WEB-INF/data/Point.java"));
		srcMap.put(PN + ".Tron", readString("WEB-INF/data/Tron.java"));
		srcMap.put(PN + ".TronBot", readString("WEB-INF/data/TronBot.java"));
		srcMap.put(PN + ".TronBotWriter",
				readString("WEB-INF/data/TronBotWriter.java"));
		srcMap.put(PN + ".TronMap", readString("WEB-INF/data/TronMap.java"));
		srcMap.put(PN + ".TronUtils", readString("WEB-INF/data/TronUtils.java"));

		srcMap.put(className, source);

		ClassLoader contextCl = Thread.currentThread().getContextClassLoader();

		StringBuilder sb = new StringBuilder();
		Map<String, Class<Object>> compiledClassMap = CompilerUtils.compile(
				srcMap, sb, contextCl);
		System.out.println("ContextClassLoader : " + contextCl);
		System.out.println("BuildString : " + sb);
		System.out.println("Result : " + compiledClassMap);

		Class<Object> cls1 = compiledClassMap.get(className);
		System.out.println("Compiled1: " + cls1);

		ClassLoader compilerCL = cls1.getClassLoader();
		System.out.println("Compiler ClassLoader : " + compilerCL);

		byte[] ba1 = CompilerUtils.csc.getByteCode(className);

		TronClassLoader tcl = new TronClassLoader(ba1, Thread.currentThread()
				.getContextClassLoader());
		System.out.println("Test Load : " + tcl.findClass(className));

		Thread.currentThread().setContextClassLoader(compilerCL);

		ObjectOutputStream encoder = new ObjectOutputStream(
				new BufferedOutputStream(response.getOutputStream()));
		encoder.writeObject(ba1);
		encoder.flush();
		System.out.println("Classes are sent.");

	}

}
