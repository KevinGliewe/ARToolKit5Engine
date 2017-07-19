
package net.gliewe.generic.runnables;

import net.gliewe.generic.function.IFuncV3;
import java.lang.Runnable;

public class Runnable3<T1, T2, T3> implements Runnable {
    T1 _v1; T2 _v2; T3 _v3;
    IFuncV3 _callback;

    public Runnable3(T1 v1, T2 v2, T3 v3, IFuncV3 callback) {
        _v1=v1; _v2=v2; _v3=v3;;
        _callback = callback;
    }

    public void run() {
        _callback.call(_v1, _v2, _v3);
    }
}
