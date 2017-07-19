
package net.gliewe.generic.callable;

import net.gliewe.generic.function.IFuncR2;

import java.util.concurrent.Callable;

public class Callable2<TR, T1, T2> implements Callable<TR> {
    T1 _v1; T2 _v2;
    IFuncR2<TR, T1, T2> _callback;

    public Callable2(T1 v1, T2 v2,
            IFuncR2<TR, T1, T2> callback) {
        T1 _v1; T2 _v2;
        _callback = callback;
    }

    @Override
    public TR call() throws Exception {
        return _callback.call(_v1, _v2);
    }
}
