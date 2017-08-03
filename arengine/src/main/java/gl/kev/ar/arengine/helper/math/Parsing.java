package gl.kev.ar.arengine.helper.math;

/**
 * Created by kevingliewe on 03.08.17.
 */

public class Parsing {

    public static int parse(String val, int default_) {
        try {
            return Integer.parseInt(val);
        }catch (Exception ex) {
            return default_;
        }
    }

    public static float parse(String val, float default_) {
        try {
            return Float.parseFloat(val);
        }catch (Exception ex) {
            return default_;
        }
    }

    public static double parse(String val, double default_) {
        try {
            return Double.parseDouble(val);
        }catch (Exception ex) {
            return default_;
        }
    }

    public static boolean parse(String val, boolean default_) {
        try {
            return Boolean.parseBoolean(val);
        }catch (Exception ex) {
            return default_;
        }
    }
}
