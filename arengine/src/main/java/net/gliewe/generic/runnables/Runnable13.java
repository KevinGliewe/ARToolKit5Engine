
package net.gliewe.generic.runnables;

import net.gliewe.generic.function.IFuncV13;
import java.lang.Runnable;

public class Runnable13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> implements Runnable {
    T1 _v1; T2 _v2; T3 _v3; T4 _v4; T5 _v5; T6 _v6; T7 _v7; T8 _v8; T9 _v9; T10 _v10; T11 _v11; T12 _v12; T13 _v13;
    IFuncV13 _callback;

    public Runnable13(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, IFuncV13 callback) {
        _v1=v1; _v2=v2; _v3=v3; _v4=v4; _v5=v5; _v6=v6; _v7=v7; _v8=v8; _v9=v9; _v10=v10; _v11=v11; _v12=v12; _v13=v13;;
        _callback = callback;
    }

    public void run() {
        _callback.call(_v1, _v2, _v3, _v4, _v5, _v6, _v7, _v8, _v9, _v10, _v11, _v12, _v13);
    }
}
