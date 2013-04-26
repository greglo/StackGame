package ox.stackgame.stackmachine.instructions;

import java.util.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.exceptions.TypeException;

import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.StringStackValue;

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
            public void apply(StackMachine m, StackValue<?> arg)
                    throws StackRuntimeException {
                m.getStack().push(arg);
            }

            public List<Class<?>> argTypes() {
                return typeList(IntStackValue.class, CharStackValue.class,
                    StringStackValue.class);
            }
        });

        ht.put("load", new SeqOperation() {
            public void apply(StackMachine m, StackValue<?> arg)
                    throws StackRuntimeException {
                m.getStack().push(m.getStore((Integer) arg.getValue()));
            }

            public List<Class<? extends Object>> argTypes() {
                return typeList(IntStackValue.class);
            }
        });

        ht.put("store", new SeqOperation() {
            public void apply(StackMachine m, StackValue<?> arg)
                    throws StackRuntimeException {
                m.setStore((Integer) arg.getValue(), m.getStack().pop());
            }

            public List<Class<?>> argTypes() {
                return typeList(IntStackValue.class);
            }
        });

        ht.put("label", new SeqOperation() {
            public void apply(StackMachine m, StackValue<?> arg)
                    throws StackRuntimeException {
            }

            public List<Class<?>> argTypes() {
                return typeList(StringStackValue.class);
            }
        });

        ht.put("jump", new BranchOperation() {
            @Override
            protected boolean p(StackMachine machine) {
                return true;
            }

            public List<Class<?>> argTypes() {
                return typeList(StringStackValue.class);
            }
        });

        ht.put("input", new SeqOperation() {
            @Override
            public void apply(StackMachine m, StackValue<?> arg)
                    throws StackRuntimeException {
                m.getStack().push(m.consumeInput());
            }
        });

        ht.put("output", new SeqOperation() {
            @Override
            public void apply(StackMachine m, StackValue<?> arg)
                    throws StackRuntimeException {
                m.addOutput(m.getStack().pop());
            }
        });

        ht.put("nop", new SeqOperation() {
            public void apply(StackMachine m, StackValue<?> arg) {
            }
        });

        ht.put("dump", new SeqOperation() {
            public void apply(StackMachine m, StackValue<?> arg) {
                m.dump();
            }
        });
    }

    public static Operation get(String name) {
        name = name.trim().toLowerCase();
        if (!initialised)
            init();
        if (ht.get(name) == null)
            System.out.println(name);
        return ht.get(name);
    }
}
