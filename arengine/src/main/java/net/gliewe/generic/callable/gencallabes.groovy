import groovy.text.GStringTemplateEngine as Engine
 
def tplr = '''
package ${pack};

import net.gliewe.generic.function.IFuncR${count};

import java.util.concurrent.Callable;

public class Callable${count}<TR, ${((1..count).collect {'T'+it} as String[]).join(', ')}> implements Callable<TR> {
    ${((1..count).collect {'T'+it+" _v"+it} as String[]).join('; ')};
    IFuncR${count}<TR, ${((1..count).collect {'T'+it} as String[]).join(', ')}> _callback;

    public Callable${count}(${((1..count).collect {'T'+it+" v"+it} as String[]).join(', ')},
            IFuncR${count}<TR, ${((1..count).collect {'T'+it} as String[]).join(', ')}> callback) {
        ${((1..count).collect {'T'+it+" _v"+it} as String[]).join('; ')};
        _callback = callback;
    }

    @Override
    public TR call() throws Exception {
        return _callback.call(${((1..count).collect {"_v"+it} as String[]).join(', ')});
    }
}
'''

def e = new Engine()

for(i in 1..20) {
    def fileR = new File("Callable" + i + ".java")

    fileR.write e.createTemplate(tplr).make([pack: 'net.gliewe.generic.callable', count: i]).toString()
}