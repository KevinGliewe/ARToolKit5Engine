package gl.kev.ar.arengine.helper;

import android.view.View;
import android.widget.AbsoluteLayout;

/**
 * Created by kevingliewe on 24.07.17.
 */

public class ViewX {
    public static void setPosition(View view, int x, int y) {
        AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams)view.getLayoutParams();
        params.x = x;
        params.y = y;
        view.setLayoutParams(params);
    }
    public static void setVisible(View view, boolean visible) {
        if(view == null)
            return;
        if(visible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.INVISIBLE);
    }
}
