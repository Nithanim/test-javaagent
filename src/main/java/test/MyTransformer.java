package test;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTransformer implements ClassFileTransformer {
    private static final Logger logger = LoggerFactory.getLogger(MyTransformer.class);
    private static final String TARGET_CLASS_NAME = "java.lang.Thread";
    private static final String CLASS_PATH = TARGET_CLASS_NAME.replaceAll("\\.", "/");

    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] originalClassBytes) {
        if (!className.equals(CLASS_PATH)) {
            return originalClassBytes;
        }

        logger.info("[Agent] Transforming class");
        try {
            ClassPool cp = ClassPool.getDefault(); //The pool is empty for us here:
            //cp.appendClassPath(new LoaderClassPath(loader)); //For classes in the bootstrap (like Thread) this is null and therefore useless.
            cp.insertClassPath(new ClassClassPath(System.class)); //This is the workaround to make classes visible for javassist but cumbersome

            CtClass cc = cp.makeClass(new ByteArrayInputStream(originalClassBytes));
            CtMethod m = cc.getDeclaredMethod("stop");
            m.insertBefore("java.lang.System.out.println(\"Thread.stop() called! Trace:\"); new Exception().printStackTrace();");

            return cc.toBytecode();
        } catch (Exception e) {
            logger.error("Exception", e);
            return originalClassBytes;
        }
    }
}
