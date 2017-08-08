
package net.gliewe.generic.tasks;

import net.gliewe.generic.function.*;


public class Task19<T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {
    IFuncR19<T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> callable = null;
    Thread thread = null;

    boolean finished = false;
    T returnvalue = null;
    Throwable exception = null;

    public Task19(IFuncR19<T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>callable) {
        this.callable = callable;
    }

    public Task19<T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> start(final T1 t1, final T2 t2, final T3 t3, final T4 t4, final T5 t5, final T6 t6, final T7 t7, final T8 t8, final T9 t9, final T10 t10, final T11 t11, final T12 t12, final T13 t13, final T14 t14, final T15 t15, final T16 t16, final T17 t17, final T18 t18, final T19 t19) {
        if(thread != null)
            return this;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    returnvalue = callable.call( t1,  t2,  t3,  t4,  t5,  t6,  t7,  t8,  t9,  t10,  t11,  t12,  t13,  t14,  t15,  t16,  t17,  t18,  t19);
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

