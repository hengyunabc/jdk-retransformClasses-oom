package com.example.jdkretransformClassesoom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

import net.bytebuddy.agent.ByteBuddyAgent;

public class JdkRetransformClassesOomApplication {

	public static void main(String[] args) throws IOException, UnmodifiableClassException, InterruptedException {

		Instrumentation install = ByteBuddyAgent.install();

		FFF fff = new FFF();

		String classDir = FFF.class.getProtectionDomain().getCodeSource().getLocation().getFile();

		final byte[] bytes = read(new File(classDir, FFF.class.getName().replace('.', '/') + ".class"));

		install.addTransformer(new ClassFileTransformer() {

			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				if (className.equals(FFF.class.getName().replace('.', '/'))) {
					System.err.println(" return bytes");
					return bytes;
				}

				return null;
			}
		}, true);

		for (;;) {

			install.retransformClasses(FFF.class);

//			TimeUnit.SECONDS.sleep(1);
			TimeUnit.MILLISECONDS.sleep(1);
		}

	}

	public static byte[] read(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}

}
