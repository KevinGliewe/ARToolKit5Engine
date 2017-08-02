package gl.kev.ar.arengine.config;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.NativeInterface;

import java.util.Map;

import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 01.08.17.
 */

public class ARMarkerOptions {
    public Map<String, String> int_type = null;
    public Map<String, Float> float_type = null;
    public Map<String, Boolean> bool_type = null;

    public void apply(int markerUID) {
        if(int_type != null)
            applyInt(markerUID);

        if(float_type != null)
            applyFloat(markerUID);

        if(bool_type != null)
            applyBool(markerUID);
    }

    private void applyInt(int markerUID) {
        for(String key: int_type.keySet()) {
            try {
                int option = Integer.MIN_VALUE;
                int value = Integer.MIN_VALUE;

                try { option = Integer.parseInt(key); } catch (Exception ex) { }
                try { value = Integer.parseInt(int_type.get(key)); } catch (Exception ex) { }

                if(option == Integer.MIN_VALUE)
                    option = findIntField(key);
                if(value == Integer.MIN_VALUE)
                    value = findIntField(int_type.get(key));

                NativeInterface.arwSetMarkerOptionInt(markerUID, option, value);
                //NativeInterface.arwSetTrackerOptionInt(option, value);

                GLog.info("Did set marker int option of " + markerUID + ": '" + key + "' -> '" + int_type.get(key) + "'");
            } catch (Exception ex) {
                GLog.exception("Can't apply int option to AR: '" + key + "' -> '" + int_type.get(key) + "'", ex);
            }
        }
    }
    private void applyFloat(int markerUID) {
        for(String key: float_type.keySet()) {
            try {
                int option = Integer.MIN_VALUE;
                float value = float_type.get(key);

                try { option = Integer.parseInt(key); } catch (Exception ex) { }

                if(option == Integer.MIN_VALUE)
                    option = findIntField(key);

                NativeInterface.arwSetMarkerOptionFloat(markerUID, option, value);
                //NativeInterface.arwSetTrackerOptionFloat(option, value);
                GLog.info("Did set marker float option of " + markerUID + ": '" + key + "' -> '" + float_type.get(key) + "'");
            } catch (Exception ex) {
                GLog.exception("Can't apply float option to AR: '" + key + "' -> '" + int_type.get(key) + "'", ex);
            }
        }
    }
    private void applyBool(int markerUID) {
        for(String key: bool_type.keySet()) {
            try {
                int option = Integer.MIN_VALUE;
                boolean value = bool_type.get(key);

                try { option = Integer.parseInt(key); } catch (Exception ex) { }

                if(option == Integer.MIN_VALUE)
                    option = findIntField(key);

                NativeInterface.arwSetMarkerOptionBool(markerUID, option, value);
                //NativeInterface.arwSetTrackerOptionBool(option, value);
                GLog.info("Did set marker bool option of " + markerUID + ": '" + key + "' -> '" + bool_type.get(key) + "'");
            } catch (Exception ex) {
                GLog.exception("Can't apply float option to AR: '" + key + "' -> '" + int_type.get(key) + "'", ex);
            }
        }
    }

    private int findIntField(String name) throws NoSuchFieldException, IllegalAccessException {
        return NativeInterface.class.getField(name).getInt(null);
    }
}
