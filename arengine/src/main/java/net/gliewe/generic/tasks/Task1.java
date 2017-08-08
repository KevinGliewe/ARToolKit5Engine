package net.gliewe.generic.tasks;

import net.gliewe.generic.function.*;

import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 08.08.17.
 */



public class Task1<T, T0> {
    IFuncR1<T, T0> callable = null;
    Thread thread = null;

    boolean finished = false;
    T returnvalue = null;
    Throwable exception = null;

    public Task1(IFuncR1<T, T0> callable) {
        this.callable = callable;
    }

    public Task1<T, T0> start(final T0 t0) {
        if(thread != null)
            return this;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    returnvalue = callable.call(t0);
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
