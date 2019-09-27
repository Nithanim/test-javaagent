package test;

import com.sun.tools.attach.VirtualMachine;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        //Unstable but works on all java versions
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String pid = runtime.getName().split("@")[0];

        //Completely safe to use but only Java9+
        //String pid = String.valueOf(ProcessHandle.current().pid());

        VirtualMachine jvm = VirtualMachine.attach(pid);
        jvm.loadAgent(Paths.get("./target/javaagent-1.0-SNAPSHOT.jar").toAbsolutePath().toString());
        jvm.detach();

        new Threadbog().crash();
    }
}
