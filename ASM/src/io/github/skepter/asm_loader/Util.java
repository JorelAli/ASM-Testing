package io.github.skepter.asm_loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class Util {
	
	/**
	 * Gets bytes from InputStream
	 *
	 * @param stream
	 *            The InputStream
	 * @return Returns a byte[] representation of given stream
	 */

	public static byte[] getBytesFromIS(InputStream stream) {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = stream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
		} catch (Exception e) {
			System.err.println("Failed to convert IS to byte[]!");
			e.printStackTrace();
		}

		return buffer.toByteArray();

	}

	/**
	 * Gets bytes from class
	 *
	 * @param clazz
	 *            The class
	 * @return Returns a byte[] representation of given class
	 */

	public static byte[] getBytesFromClass(Class<?> clazz) {
		return getBytesFromIS(clazz.getClassLoader().getResourceAsStream(clazz.getName().replace('.', '/') + ".class"));
	}
	

	public static String getPID() {
		String jvm = ManagementFactory.getRuntimeMXBean().getName();
		String pid = jvm.substring(0, jvm.indexOf('@'));
		return pid;
	}

	/*
	 * Code which attaches an agent to the current JVM. This will basically
	 * add a temporary .jar file to the current JVM for us to use at will.
	 * 
	 * the agentClasses are the classes to add to the JVM.
	 */
	public static void attachAgentToJVM(String JVMPid, Class<?>... agentClasses) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
		//Creating temporary agent jar file
		final File jarFile = File.createTempFile("agent", ".jar");
		jarFile.deleteOnExit();
		
		//Creating a Manifest file for the jar file
		final Manifest manifest = new Manifest();
		final Attributes mainAttributes = manifest.getMainAttributes();
		mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		mainAttributes.put(new Attributes.Name("Agent-Class"), Agent.class.getName());
		mainAttributes.put(new Attributes.Name("Can-Retransform-Classes"), "true");
		mainAttributes.put(new Attributes.Name("Can-Redefine-Classes"), "true");
		
		final JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), manifest);
		
		//Adding all of the classes to the jar file
		for (Class<?> clazz : agentClasses) {
			final JarEntry agent = new JarEntry(clazz.getName().replace('.', '/') + ".class");
			jos.putNextEntry(agent);

			//Writing the bytes from the classes to the jar
			jos.write(Util.getBytesFromIS(clazz.getClassLoader().getResourceAsStream(clazz.getName().replace('.', '/') + ".class")));
			jos.closeEntry();
		}
		
		//close the jar
		jos.close();
		
		//Attach the agent to the current JVM.
		VirtualMachine vm = VirtualMachine.attach(JVMPid);
		vm.loadAgent(jarFile.getAbsolutePath());
		vm.detach();
	}
	
}
