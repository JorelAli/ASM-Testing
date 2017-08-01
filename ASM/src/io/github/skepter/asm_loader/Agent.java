package io.github.skepter.asm_loader;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

//Transformation occurs before it is loaded by the JVM
public class Agent implements ClassFileTransformer {
	
	//Reinstrumenting means "modifying"
	private static Instrumentation instrumentation = null;
	private static Agent transformer;
	
	//Agents use agentmain instead of static void main()
	public static void agentmain(String s, Instrumentation i) {
		System.out.println("Agent loaded!");
		
		// initialization code:
		transformer = new Agent();
		instrumentation = i;
		instrumentation.addTransformer(transformer);
		
		// to instrument, first revert all added bytecode:
		// call retransformClasses() on all modifiable classes...
		System.out.println("Preparing to redefine classes...");
		try {
			instrumentation.redefineClasses(new ClassDefinition(CraftFireball.class, Util.getBytesFromClass(CraftFireball.class)));
			//instrumentation.redefineClasses(new ClassDefinition(CraftPlayer.class, Util.getBytesFromClass(CraftPlayer.class)));
			System.out.println("Classes redefined!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to redefine class!");
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		
		//Sanity checks to prevent the profiler from profiling the profiler
		if (loader != ClassLoader.getSystemClassLoader()) {
			System.err.println(className + " is not using the system loader, and so cannot be loaded!");
			return classfileBuffer;
		}
		if (className.startsWith("io/github/skepter/asm_loader")) {
			//prevent stack overflow error by profiling the profiler.
			System.err.println(className + " is part of profiling classes. No StackOverflow for you!");
			return classfileBuffer;
		}		
				
		//Actual result creation here.
		
		byte[] result = classfileBuffer;
		
		if(className.contains("Fireball")) {
			try {
				// Create class reader from buffer
				ClassReader reader = new ClassReader(classfileBuffer);
				
				// Make writer
				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
				ClassAdapter profiler = new ProfileClassAdapter(writer, className);
				
				// Add the class adapter as a modifier
				reader.accept(profiler, 0);
				result = writer.toByteArray();
				System.out.println("Returning reinstrumented class: " + className);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
     * Kills this agent
     */
    public static void killAgent() {
        instrumentation.removeTransformer(transformer);
    }
}