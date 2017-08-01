package io.github.skepter.asm_loader;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;


public class ProfileMethodAdapter extends MethodAdapter {
	
	private String _className, _methodName;

	public ProfileMethodAdapter(MethodVisitor visitor, String className, String methodName) {
		super(visitor);
		_className = className;
		_methodName = methodName;
		//System.out.println("Profiled " + methodName + " in class " + className + ".");
	}

	//When a method is visited
	@Override
	public void visitCode() {
		
		if (_className.contains("Fireball")) {
			if(_methodName.contains("getYield")) {
				{
					//force every fireball to be an explosion size of 10
					mv.visitCode();
					Label l0 = new Label();
					mv.visitLabel(l0);
					//mv.visitLineNumber(20, l0);
					//mv.visitVarInsn(ALOAD, 0);
					//mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/craftbukkit/v1_12_R1/entity/CraftFireball", "getHandle", "()Lnet/minecraft/server/v1_12_R1/EntityFireball;");
					//mv.visitFieldInsn(GETFIELD, "net/minecraft/server/v1_12_R1/EntityFireball", "bukkitYield", "F");
					mv.visitLdcInsn(new Float("10.0"));
					mv.visitInsn(FRETURN);
					Label l1 = new Label();
					mv.visitLabel(l1);
					mv.visitLocalVariable("this", "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftFireball;", null, l0, l1, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
					
				}

			}

			// Invoking Profile.start(className, methodName)
			this.visitLdcInsn(_className);
			this.visitLdcInsn(_methodName);

			// INVOKEDYNAMIC is for non-static methods, INVOKESTATIC for static
			// methods
			this.visitMethodInsn(INVOKESTATIC, "io/github/skepter/asm_loader/Profile", "start",
					"(Ljava/lang/String;Ljava/lang/String;)V");
			
			

		}
		super.visitCode();
	}
	
	

	@Override
	public void visitInsn(int inst) {
//		switch (inst) {
//			case Opcodes.ARETURN:
//			case Opcodes.DRETURN:
//			case Opcodes.FRETURN:
//			case Opcodes.IRETURN:
//			case Opcodes.LRETURN:
//			case Opcodes.RETURN:
//			case Opcodes.ATHROW:
//				this.visitLdcInsn(_className);
//				this.visitLdcInsn(_methodName);
//				this.visitMethodInsn(Opcodes.INVOKESTATIC, "io/github/skepter/asm_loader/Profile", "end", "(Ljava/lang/String;Ljava/lang/String;)V");
//
//				break;
//			default:
//				break;
//		}

		super.visitInsn(inst);
	}

}