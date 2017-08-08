package gl.kev.ar.arengine.config;

import com.google.gson.GsonBuilder;

import net.gliewe.generic.function.IFuncR0;
import net.gliewe.generic.function.IFuncR1;
import net.gliewe.generic.tasks.Task;
import net.gliewe.generic.tasks.Task1;

import gl.kev.ar.arengine.AREngineActivity;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 08.08.17.
 */

public class ConfigLoader {
    Task1<ARSceneConfig, AREngineActivity> task;

    public ConfigLoader(final AREngineActivity activity) {

        task = new Task1<>(new IFuncR1<ARSceneConfig, AREngineActivity>() {
            @Override
            public ARSceneConfig call(AREngineActivity v1) {
                ARSceneConfig ret = activity.provideConfig();

                GLog.success("Config loaded");
                GLog.debug("Config:\n" + new GsonBuilder().setPrettyPrinting().create().toJson(ret));

                // Preload Models
                for(ARMarker marker : ret.marker)
                    for(ARModel model : marker.models)
                        ARModel.preloadModel(v1, model);

                return ret;
            }
        }).start(activity);
    }

    public boolean isFinished() { return task.isFinished(); }
    public ARSceneConfig get() throws Throwable { return task.get(); }
}
