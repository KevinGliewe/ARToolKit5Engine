
package net.gliewe.generic.runnables;

import net.gliewe.generic.function.IFuncV1;
import java.lang.Runnable;

public class Runnable1<T1> implements Runnable {
    T1 _v1;
    IFuncV1 _callback;

    public Runnable1(T1 v1, IFuncV1 callback) {
        _v1=v1;;
        _callback = callback;
    }

    public void run() {
        _callback.call(_v1);
    }
}
