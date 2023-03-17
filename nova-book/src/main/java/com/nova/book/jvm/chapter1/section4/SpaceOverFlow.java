package com.nova.book.jvm.chapter1.section4;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @description: 元空间内存溢出
 * @author: wzh
 * @date: 2023/3/17 16:42
 */
class SpaceOverFlow extends ClassLoader {

    public static void main(String[] args) {
        int j = 0;
        try {
            SpaceOverFlow flow = new SpaceOverFlow();
            for (int i = 0; i < 20000; i++, j++) {
                ClassWriter cw = new ClassWriter(0);
                cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, "Class" + i, null, "java/lang/Object", null);
                byte[] bytes = cw.toByteArray();
                flow.defineClass("Class" + i, bytes, 0, bytes.length);
            }
        } finally {
            System.out.println("j = " + j);
        }
    }
}
