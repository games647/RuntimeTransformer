package me.yamakaja.runtimetransformer.agent;

import me.yamakaja.runtimetransformer.transform.ClassTransformer;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yamakaja on 19.05.17.
 */
public class Agent {

    private static Agent instance;

    private Instrumentation instrumentation;

    private Agent(Instrumentation inst){
        this.instrumentation = inst;
    }

    public static void agentmain(String agentArgument, Instrumentation instrumentation) {
        instance = new Agent(instrumentation);
    }

    public static Agent getInstance() {
        return instance;
    }

    public void process(Class<?> ... transformerClasses) {
        List<AgentJob> agentJobs = new ArrayList<>();

        for (Class<?> clazz : transformerClasses)
            agentJobs.add(new AgentJob(clazz));

        ClassTransformer classTransformer = new ClassTransformer(agentJobs);
        instrumentation.addTransformer(classTransformer, true);

        try {
            instrumentation.retransformClasses(classTransformer.getClassesToTransform());
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
    }

}
