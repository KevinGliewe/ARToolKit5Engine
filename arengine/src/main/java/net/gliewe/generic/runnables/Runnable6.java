
package net.gliewe.generic.runnables;

import net.gliewe.generic.function.IFuncV6;
import java.lang.Runnable;

public class Runnable6<T1, T2, T3, T4, T5, T6> implements Runnable {
    T1 _v1; T2 _v2; T3 _v3; T4 _v4; T5 _v5; T6 _v6;
    IFuncV6 _callback;

    public Runnable6(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, IFuncV6 callback) {
        _v1=v1; _v2=v2; _v3=v3; _v4=v4; _v5=v5; _v6=v6;;
        _callback = callback;
    }

    public void run() {
        _callback.call(_v1, _v2, _v3, _v4, _v5, _v6);
    }
}
