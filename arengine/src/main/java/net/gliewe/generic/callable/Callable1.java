
package net.gliewe.generic.callable;

import net.gliewe.generic.function.IFuncR1;

import java.util.concurrent.Callable;

public class Callable1<TR, T1> implements Callable<TR> {
    T1 _v1;
    IFuncR1<TR, T1> _callback;

    public Callable1(T1 v1,
            IFuncR1<TR, T1> callback) {
        T1 _v1;
        _callback = callback;
    }

    @Override
    public TR call() throws Exception {
        return _callback.call(_v1);
    }
}
