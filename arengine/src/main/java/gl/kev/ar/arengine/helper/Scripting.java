package gl.kev.ar.arengine.helper;

import android.content.ContentProvider;
import android.text.TextUtils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.Map;

import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

// http://www.javased.com/?api=org.mozilla.javascript.NativeJavaObject

public class Scripting {
    public static Object execute(String[] script, Map<String, Object> context) {
        return execute(TextUtils.join("\n", script), context);
    }

    public static Object execute(String script, Map<String, Object> context) {

        try {
            Context cx = Context.enter();
            cx.setOptimizationLevel(-1);
            Scriptable scope = cx.initStandardObjects();

            for (String name : context.keySet()) {
                ScriptableObject.putProperty(scope, name, Context.javaToJS(context.get(name), scope));
            }

            return jsToJava(cx.evaluateString(scope, script, "EvaluationScript", 1, null));
        }catch (Exception ex) {
            GLog.exception("Exception during Script", ex);
        } finally {
            Context.exit();
        }
        return null;
    }

    public static Object jsToJava(Object jsObject)
    {
        if (jsObject == null) return null;
        if (jsObject == org.mozilla.javascript.Context.getUndefinedValue()) return null;
        if (jsObject instanceof String) return jsObject;
        if (jsObject instanceof Boolean) return jsObject;
        if (jsObject instanceof Integer) return jsObject;
        if (jsObject instanceof Long) return jsObject;
        if (jsObject instanceof Float) return jsObject;
        if (jsObject instanceof Double) return jsObject;
        if (jsObject instanceof NativeArray) return convertArray((NativeArray) jsObject);
        if (jsObject instanceof NativeObject) return convertObject((NativeObject) jsObject);
        if (jsObject instanceof NativeJavaObject) return ((NativeJavaObject) jsObject).unwrap();
        return jsObject;
    }

    private static Object[] convertArray(NativeArray jsArray)
    {
        Object[] ids = jsArray.getIds();
        Object[] result = new Object[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            Object id = ids[i];
            int index = (Integer) id;
            Object jsValue = jsArray.get(index, jsArray);
            result[i] = jsToJava(jsValue);
        }
        return result;
    }

    private static Object convertObject(NativeObject jsObject)
    {
        Object[] ids = jsObject.getIds();
        Map result = new HashMap(ids.length);
        for (Object id : ids)
        {
            if (id instanceof String)
            {
                Object jsValue = jsObject.get((String) id, jsObject);
                result.put(id, jsToJava(jsValue));
            }
            else if (id instanceof Integer)
            {
                Object jsValue = jsObject.get((Integer) id, jsObject);
                result.put(id, jsToJava(jsValue));
            }
            else
                throw new AssertionError();
        }
        return result;
    }

    public static boolean isJavaScriptObject(Object object)
    {
        // Do not remove: called from JavaScript's env.js
        return object instanceof NativeObject;
    }

}
