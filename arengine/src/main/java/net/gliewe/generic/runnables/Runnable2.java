
package net.gliewe.generic.runnables;

import net.gliewe.generic.function.IFuncV2;
import java.lang.Runnable;

public class Runnable2<T1, T2> implements Runnable {
    T1 _v1; T2 _v2;
    IFuncV2 _callback;

    public Runnable2(T1 v1, T2 v2, IFuncV2 callback) {
        _v1=v1; _v2=v2;;
        _callback = callback;
    }

    public void run() {
        _callback.call(_v1, _v2);
    }
}
