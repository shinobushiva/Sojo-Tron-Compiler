package jp.ac.sojou.cis.izumi.sojotoroncompiler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javaxtools.compiler.CompilerUtils;

import tron.utils.Point;
import tron.utils.Tron;
import tron.utils.TronBot;
import tron.utils.TronMap;

/**
 * Servlet implementation class CompilerServlet
 */
public class CopyOfCompilerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// private final String PN = "jp.ac.sojou.cis.izumi.tron.utils";
	private final String PN = "tron.utils";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CopyOfCompilerServlet() {
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

	private String getFQCN(String src) throws IOException {
		// read package name from source code
		int mode = 1;
		String packageName = "";
		String cName = "";
		BufferedReader br = new BufferedReader(new StringReader(src));
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
				if (line.contains("class")) {
					String[] split = line.split(" ");
					for (int i = 0; i < split.length; i++) {
						if (split[i].trim().equals("class")) {
							cName = split[i + 1];
							break;
						}
					}
				}
			}
		}
		String name = null;
		if (packageName.isEmpty()) {
			name = cName;
		} else {
			name = packageName + "." + cName;
		}

		return name;

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

		String source1 = request.getParameter("source1");
		String source2 = request.getParameter("source2");
		String map = request.getParameter("map");

		String getBytes = request.getParameter("getBytes");

		System.out.println("getBytes : " + getBytes);

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

		String cn1 = getFQCN(source1);
		System.out.println(cn1);
		srcMap.put(cn1, source1);
		String cn2 = getFQCN(source2);
		System.out.println(cn2);
		srcMap.put(cn2, source2);

		ClassLoader contextCl = Thread.currentThread().getContextClassLoader();

		StringBuilder sb = new StringBuilder();
		Map<String, Class<Object>> compiledClassMap = CompilerUtils.compile(
				srcMap, sb, contextCl);
		System.out.println("ContextClassLoader : " + contextCl);
		System.out.println("BuildString : " + sb);
		System.out.println("Result : " + compiledClassMap);

		Class<Object> clsMap = compiledClassMap.get(PN + ".Map");
		Class<Object> cls1 = compiledClassMap.get(cn1);
		System.out.println("Compiled1: " + cls1);
		Class<Object> cls2 = compiledClassMap.get(cn2);
		System.out.println("Compiled2: " + cls2);

		if (getBytes != null) {
			ClassLoader compilerCL = cls1.getClassLoader();
			System.out.println("Compiler ClassLoader : " + compilerCL);

			// byte[] ba1 = getBytes(cls1);
			// byte[] ba2 = getBytes(cls2);

			byte[] ba1 = CompilerUtils.csc.getByteCode(cn1);
			byte[] ba2 = CompilerUtils.csc.getByteCode(cn2);

			TronClassLoader tcl = new TronClassLoader(ba1, Thread
					.currentThread().getContextClassLoader());
			System.out.println("Test Load : " + tcl.findClass(cn1));

			Thread.currentThread().setContextClassLoader(compilerCL);

			ObjectOutputStream encoder = new ObjectOutputStream(
					new BufferedOutputStream(response.getOutputStream()));
			// XMLEncoderWrapper encoder = new XMLEncoderWrapper(
			// response.getOutputStream(), compilerCL);
			encoder.writeObject(ba1);
			encoder.writeObject(ba2);
			encoder.flush();
			System.out.println("Classes are sent.");
		} else {

			// to get class loader
			ClassLoader compilerCL = cls1.getClassLoader();
			System.out.println("ClassLoader: " + compilerCL);

			TronBotWrapper tbw1 = new TronBotWrapper(cls1, clsMap);
			TronBotWrapper tbw2 = new TronBotWrapper(cls2, clsMap);

			Tron t = new Tron();
			String[] result = t.tron(new TronMap(new StringReader(map)), tbw1,
					tbw2, 0, 1000);

			System.out.println(result[0]);
			System.out.println(result[1]);

			XMLEncoderWrapper encoder = new XMLEncoderWrapper(
					response.getOutputStream(), compilerCL);
			encoder.writeObject(result);
			encoder.flush();
		}
	}

	class XMLEncoderWrapper {
		private Class<?> c;
		private Object encoder;

		public XMLEncoderWrapper(OutputStream os, ClassLoader l) {
			try {
				c = l.loadClass("java.beans.XMLEncoder");
				Constructor<?> cs = c.getConstructor(OutputStream.class);
				encoder = cs.newInstance(os);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void writeObject(Object o) {
			try {
				Method m = c.getMethod("writeObject", Object.class);
				m.invoke(encoder, o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void flush() {
			try {
				Method m = c.getMethod("flush");
				m.invoke(encoder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class TronBotWrapper implements TronBot {

		private Object instance;
		private Class<?> mapClass;
		private Method makeMove;
		private Method copy;

		public TronBotWrapper(Class<?> bc, Class<?> mc) {
			try {
				mapClass = mc;
				instance = bc.newInstance();
				makeMove = bc.getMethod("makeMove", new Class<?>[] { mc });
				copy = mc.getDeclaredMethod("copy", new Class<?>[] {
						boolean[][].class, int.class, int.class, int.class,
						int.class, int.class, int.class });
				copy.setAccessible(true);

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("deprecation")
		private void copyMap(tron.utils.Map s, tron.utils.Map d) {
			boolean[][] walls = s.getWalls();
			int width = s.getWidth();
			int height = s.getHeight();
			Point myLocation = s.getMyLocation();
			Point opponentLocation = s.getOpponentLocation();

			boolean[][] ws = new boolean[walls.length][];
			for (int i = 0; i < width; i++) {
				ws[i] = new boolean[walls[i].length];
				for (int j = 0; j < height; j++) {
					ws[i][j] = walls[i][j];
				}
			}
			Point ml = new Point(myLocation.getX(), myLocation.getY());
			Point ol = new Point(opponentLocation.getX(),
					opponentLocation.getY());
			d.setWalls(ws);
			d.setWidth(width);
			d.setHeight(height);
			d.setMyLocation(ml);
			d.setOpponentLocation(ol);

		}

		@Override
		public String makeMove(tron.utils.Map m) {

			Object mapInst;
			try {
				mapInst = mapClass.newInstance();
				copyMap(m, (tron.utils.Map) mapInst);

				// copy.invoke(mapInst, m.getWalls(), m.getWidth(),
				// m.getHeight(),
				// m.getMyLocation().getX(), m.getMyLocation().getY(), m
				// .getOpponentLocation().getX(), m
				// .getOpponentLocation().getY());
				return (String) makeMove.invoke(instance, mapInst);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
