
package net.gliewe.generic.callable;

import net.gliewe.generic.function.IFuncR18;

import java.util.concurrent.Callable;

public class Callable18<TR, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> implements Callable<TR> {
    T1 _v1; T2 _v2; T3 _v3; T4 _v4; T5 _v5; T6 _v6; T7 _v7; T8 _v8; T9 _v9; T10 _v10; T11 _v11; T12 _v12; T13 _v13; T14 _v14; T15 _v15; T16 _v16; T17 _v17; T18 _v18;
    IFuncR18<TR, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> _callback;

    public Callable18(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18,
            IFuncR18<TR, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> callback) {
        T1 _v1; T2 _v2; T3 _v3; T4 _v4; T5 _v5; T6 _v6; T7 _v7; T8 _v8; T9 _v9; T10 _v10; T11 _v11; T12 _v12; T13 _v13; T14 _v14; T15 _v15; T16 _v16; T17 _v17; T18 _v18;
        _callback = callback;
    }

    @Override
    public TR call() throws Exception {
        return _callback.call(_v1, _v2, _v3, _v4, _v5, _v6, _v7, _v8, _v9, _v10, _v11, _v12, _v13, _v14, _v15, _v16, _v17, _v18);
    }
}
