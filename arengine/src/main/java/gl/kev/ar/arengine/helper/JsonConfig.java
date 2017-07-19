package gl.kev.ar.arengine.helper;


import android.app.UiAutomation;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Kevin on 10.08.2016.
 */
public abstract class JsonConfig {

    protected boolean isDirty = false;

    protected ConfigChangedListener listener;

    public JsonConfig() { }
    public JsonConfig(ConfigChangedListener listener) {
        this.listener = listener;
    }

    public void fromJSON(JsonReader reader) {
        try {
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                Field f;

                try {
                    f = this.getClass().getField("__" + name + "__");
                } catch (NoSuchFieldException e) {
                    continue;
                }

                f.setAccessible(true);

                if(f.getType().isAssignableFrom(Integer.class)) {
                    f.set(this, reader.nextInt());
                } else if(f.getType().isAssignableFrom(Long.class)) {
                    f.set(this, reader.nextLong());
                } else if(f.getType().isAssignableFrom(Double.class)) {
                    f.set(this, reader.nextDouble());
                } else if(f.getType().isAssignableFrom(Boolean.class)) {
                    f.set(this, reader.nextBoolean());
                } else if(f.getType().isAssignableFrom(String.class)) {
                    f.set(this, reader.nextString());
                } else {
                    reader.skipValue();
                }

            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        isDirty = false;
    }

    public void toJSON(JsonWriter writer) {

        try {
            writer.beginObject();

            for (Field f : this.getClass().getFields()){
                if(f.getName().startsWith("__") && f.getName().endsWith("__")) {
                    f.setAccessible(true);

                    String name = f.getName();
                    name = name.substring(2, name.length() - 2);

                    if(Integer.class.isAssignableFrom(f.getType())) {
                        writer.name(name).value((Integer)f.get(this));
                    } else if(Long.class.isAssignableFrom(f.getType())) {
                        writer.name(name).value((Long)f.get(this));
                    } else if(Double.class.isAssignableFrom(f.getType())) {
                        writer.name(name).value((Double) f.get(this));
                    } else if(Boolean.class.isAssignableFrom(f.getType())) {
                        writer.name(name).value((Boolean) f.get(this));
                    } else if(String.class.isAssignableFrom(f.getType())) {
                        writer.name(name).value((String)f.get(this));
                    }
                }
            }


            writer.endObject();

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String name, Object val) {
        try {
            Field f = this.getClass().getField("__" + name + "__");

            f.setAccessible(true);

            if(f.get(this) == val)
                return;

            f.set(this, val);

            isDirty = true;

            if(listener != null)
                listener.onFieldChanged(name, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T get(String name, T def) {
        T temp = def;

        try {
            Field f = this.getClass().getField("__" + name + "__");
            f.setAccessible(true);
            temp = (T)f.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public Object get(String name) {
        return get(name, null);
    }

    public interface ConfigChangedListener {
        void onFieldChanged(String name, Object newVal);
    }

}
