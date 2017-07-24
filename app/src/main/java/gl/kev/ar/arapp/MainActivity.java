package gl.kev.ar.arapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import gl.kev.ar.arengine.AREngineActivity;

public class MainActivity extends AREngineActivity {

    TextView tv_HelloWorld;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_HelloWorld = new TextView(this);
        tv_HelloWorld.setText("Hello World");

        addTrackedView("mymarker", tv_HelloWorld);
    }
}
