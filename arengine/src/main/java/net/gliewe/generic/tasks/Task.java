package net.gliewe.generic.tasks;

import net.gliewe.generic.function.*;
/**
 * Created by kevingliewe on 08.08.17.
 */



public class Task<T> {
    IFuncR0<T> callable = null;
    Thread thread = null;

    boolean finished = false;
    T returnvalue = null;
    Throwable exception = null;

    public Task(IFuncR0<T> callable) {
        this.callable = callable;
    }

    public Task<T> start() {
        if(thread != null)
            return this;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    returnvalue = callable.call();
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

        if(exception != null)
            return returnvalue;
        else
            throw exception;
    }
}
