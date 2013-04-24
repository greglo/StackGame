package ox.stackgame.stackmachine.instructions;

import java.util.*;
import java.lang.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.exceptions.TypeException;

import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;

public class Operations {
    private static final Hashtable<String, Operation> ht = new Hashtable<String, Operation>();
    private static boolean initialised = false;

    public static List<Class<?>> typeList(Class<?>... types) {
        ArrayList<Class<?>> res = new ArrayList<Class<?>>();

        for (Class<?> type : types)
            res.add(type);

        return res;
    }

    private static void init() {
        ht.put("add", new BinOperation() {
            public StackValue<?> binop(StackValue<?> x, StackValue<?> y)
            throws TypeException {
            return x.add(y);
            }
        });

        ht.put("sub", new BinOperation() {
            public StackValue<?> binop(StackValue<?> x, StackValue<?> y)
            throws TypeException {
            return x.sub(y);
            }
        });

        ht.put("mul", new BinOperation() {
            public StackValue<?> binop(StackValue<?> x, StackValue<?> y)
            throws TypeException {
            return x.mul(y);
            }
        });

        ht.put("div", new BinOperation() {
            public StackValue<?> binop(StackValue<?> x, StackValue<?> y)
                    throws TypeException {
                return x.div(y);
            }
        });

        ht.put("const", new SeqOperation() {
            public void apply(StackMachine m, List<StackValue<?>> args)
                    throws StackRuntimeException {
                m.getStack().push(args.get(0));
            }

            public List<Class<?>> argTypes() {
                return typeList(Integer.class, Character.class, String.class);
            }
        });

        ht.put("load", new SeqOperation() {
            public void apply(StackMachine m, List<StackValue<?>> args)
                    throws StackRuntimeException {
                m.getStack().push(m.getStore((Integer) args.get(0).getValue()));
            }

            public List<Class<? extends Object>> argTypes() {
                return typeList(Integer.class);
            }
        });

        ht.put("store", new SeqOperation() {
            public void apply(StackMachine m, List<StackValue<?>> args)
                    throws StackRuntimeException {
                m.setStore((Integer) args.get(0).getValue(), m.getStack().pop());
            }

            public List<Class<?>> argTypes() {
                return typeList(Integer.class);
            }
        });

        ht.put("label", new SeqOperation() {
            public void apply(StackMachine m, List<StackValue<?>> args)
                    throws StackRuntimeException {
            }

            public List<Class<?>> argTypes() {
                return typeList(String.class);
            }
        });

        ht.put("jump", new BranchOperation() {
            @Override
            protected boolean p(StackMachine machine) {
                return true;
            } 
        });
    }

    public static Operation get(String name) {
        if (!initialised)
            init();
        if (ht.get(name) == null)
            System.out.println(name);
        return ht.get(name);
    }
}
