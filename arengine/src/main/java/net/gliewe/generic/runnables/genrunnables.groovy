import groovy.text.GStringTemplateEngine as Engine
 
def tplr = '''
package ${pack};

import net.gliewe.generic.function.IFuncV${count};
import java.lang.Runnable;

public class Runnable${count}<${((1..count).collect {'T'+it} as String[]).join(', ')}> implements Runnable {
    ${((1..count).collect {'T'+it+" _v"+it} as String[]).join('; ')};
    IFuncV${count} _callback;

    public Runnable${count}(${((1..count).collect {'T'+it+" v"+it} as String[]).join(', ')}, IFuncV${count} callback) {
        ${((1..count).collect {"_v"+it + "=v"+it+";"} as String[]).join(' ')};
        _callback = callback;
    }

    public void run() {
        _callback.call(${((1..count).collect {"_v"+it} as String[]).join(', ')});
    }
}
'''

def e = new Engine()

for(i in 1..20) {
    def fileR = new File("Runnable" + i + ".java")

    fileR.write e.createTemplate(tplr).make([pack: 'net.gliewe.generic.runnables', count: i]).toString()
}