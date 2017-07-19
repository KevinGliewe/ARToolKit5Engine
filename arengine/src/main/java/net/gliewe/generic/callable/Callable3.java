
package net.gliewe.generic.callable;

import net.gliewe.generic.function.IFuncR3;

import java.util.concurrent.Callable;

public class Callable3<TR, T1, T2, T3> implements Callable<TR> {
    T1 _v1; T2 _v2; T3 _v3;
    IFuncR3<TR, T1, T2, T3> _callback;

    public Callable3(T1 v1, T2 v2, T3 v3,
            IFuncR3<TR, T1, T2, T3> callback) {
        T1 _v1; T2 _v2; T3 _v3;
        _callback = callback;
    }

    @Override
    public TR call() throws Exception {
        return _callback.call(_v1, _v2, _v3);
    }
}
