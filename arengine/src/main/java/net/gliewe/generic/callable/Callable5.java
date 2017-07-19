
package net.gliewe.generic.callable;

import net.gliewe.generic.function.IFuncR5;

import java.util.concurrent.Callable;

public class Callable5<TR, T1, T2, T3, T4, T5> implements Callable<TR> {
    T1 _v1; T2 _v2; T3 _v3; T4 _v4; T5 _v5;
    IFuncR5<TR, T1, T2, T3, T4, T5> _callback;

    public Callable5(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5,
            IFuncR5<TR, T1, T2, T3, T4, T5> callback) {
        T1 _v1; T2 _v2; T3 _v3; T4 _v4; T5 _v5;
        _callback = callback;
    }

    @Override
    public TR call() throws Exception {
        return _callback.call(_v1, _v2, _v3, _v4, _v5);
    }
}
