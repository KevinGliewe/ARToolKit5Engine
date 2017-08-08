package gl.kev.ar.arengine;

public class BuildInfo {
    public static final String TIMESTAMP = "2017-08-08 23:39:28";
    public static final String PROJECTDIR = "/Users/kevingliewe/Documents/prog/Fraunhofer/ARApp/arengine";
    public static final String VERSION = "5.3.2.3";
    public static final String USER = "kevingliewe";
    public static final String MACHINE = "Kevins-MacBook-Pro-2.local";
    public static final String GIT_BRANCH = "master";
    public static final String GIT_ORIGIN = "git@github.com:KevinGliewe/ARToolKit5Engine.git";
    public static final String GIT_REVISION = "6a9bcfa";
    public static String getInfoText() {
        return
                "TIMESTAMP:    " + TIMESTAMP + "\n" +
                "PROJECTDIR:   " + PROJECTDIR + "\n" +
                "VERSION:      " + VERSION + "\n" +
                "USER:         " + USER + "\n" +
                "MACHINE:      " + MACHINE + "\n" +
                "GIT_BRANCH:   " + GIT_BRANCH + "\n" +
                "GIT_ORIGIN:   " + GIT_ORIGIN + "\n" +
                "GIT_REVISION: " + GIT_REVISION;
    }
}