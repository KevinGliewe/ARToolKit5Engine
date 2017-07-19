
package net.gliewe.generic.runnables;

import net.gliewe.generic.function.IFuncV4;
import java.lang.Runnable;

public class Runnable4<T1, T2, T3, T4> implements Runnable {
    T1 _v1; T2 _v2; T3 _v3; T4 _v4;
    IFuncV4 _callback;

    public Runnable4(T1 v1, T2 v2, T3 v3, T4 v4, IFuncV4 callback) {
        _v1=v1; _v2=v2; _v3=v3; _v4=v4;;
        _callback = callback;
    }

    public void run() {
        _callback.call(_v1, _v2, _v3, _v4);
    }
}
