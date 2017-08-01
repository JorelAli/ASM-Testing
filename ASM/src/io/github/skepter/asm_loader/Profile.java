package io.github.skepter.asm_loader;

/**
 * A profiler class.
 * 
 * Profiling is analysing the space or time complexity of a program,
 * the usage of certain instructions (e.g. methods), or frequency and
 * duration of method calls.
 */
public class Profile {

	//Start method (can be anything, but this is a profiler)
	public static void start(String className, String methodName) {
		System.out.println(new StringBuilder(className).append('\t').append(methodName).append("\tstart\t")
				.append(System.currentTimeMillis()));
	}

	//End method (can be anything, but this is a profiler)
	public static void end(String className, String methodName) {
		System.out.println(new StringBuilder(className).append('\t').append(methodName).append("\tend\t")
				.append(System.currentTimeMillis()));
	}
	
	//End method (can be anything, but this is a profiler)
		public static void erm(String className, String methodName) {
			System.out.println(new StringBuilder(className).append('\t').append(methodName).append("\tend\t")
					.append(System.currentTimeMillis()));
		}
	
	/*
	 * Normally, to profile methods, you'd have to put start(blah blah) and end(blah blah) at 
	 * the start and end of EACH METHOD.
	 * 
	 * Let's use ASM to do this for us, instead of spending ages doing all of that.
	 */

}
