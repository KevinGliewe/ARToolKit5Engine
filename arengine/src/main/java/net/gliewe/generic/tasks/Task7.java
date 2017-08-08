
package net.gliewe.generic.tasks;

import net.gliewe.generic.function.*;


public class Task7<T, T1, T2, T3, T4, T5, T6, T7> {
    IFuncR7<T, T1, T2, T3, T4, T5, T6, T7> callable = null;
    Thread thread = null;

    boolean finished = false;
    T returnvalue = null;
    Throwable exception = null;

    public Task7(IFuncR7<T, T1, T2, T3, T4, T5, T6, T7>callable) {
        this.callable = callable;
    }

    public Task7<T, T1, T2, T3, T4, T5, T6, T7> start(final T1 t1, final T2 t2, final T3 t3, final T4 t4, final T5 t5, final T6 t6, final T7 t7) {
        if(thread != null)
            return this;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    returnvalue = callable.call( t1,  t2,  t3,  t4,  t5,  t6,  t7);
                } catch (Throwable ex) {
                    exception = ex;
                }

                finished = true;
            }
        });
        thread.start();
        return this;
    }

    public boolean isFinished() { return finished; }

    public Throwable getException() { return exception; }

    public T get() throws Throwable {
        while(!finished) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(exception == null)
            return returnvalue;
        else
            throw exception;
    }
}

