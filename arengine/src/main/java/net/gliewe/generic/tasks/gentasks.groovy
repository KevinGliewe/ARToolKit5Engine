import groovy.text.GStringTemplateEngine as Engine
 
def tplr = '''
package ${pack};

import net.gliewe.generic.function.*;


public class Task${count}<T, ${((1..count).collect {'T'+it} as String[]).join(', ')}> {
    IFuncR${count}<T, ${((1..count).collect {'T'+it} as String[]).join(', ')}> callable = null;
    Thread thread = null;

    boolean finished = false;
    T returnvalue = null;
    Throwable exception = null;

    public Task${count}(IFuncR${count}<T, ${((1..count).collect {'T'+it} as String[]).join(', ')}>callable) {
        this.callable = callable;
    }

    public Task${count}<T, ${((1..count).collect {'T'+it} as String[]).join(', ')}> start(${((1..count).collect {'final T'+it+" t"+it} as String[]).join(', ')}) {
        if(thread != null)
            return this;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    returnvalue = callable.call(${((1..count).collect {" t"+it} as String[]).join(', ')});
                } catch (Throwable ex) {
                    exception = ex;
                }

                finished = true;
            }
        });
        thread.start();
        return this;
    }

    public boolean isFinished() { return finished; }

    public Throwable getException() { return exception; }

    public T get() throws Throwable {
        while(!finished) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(exception == null)
            return returnvalue;
        else
            throw exception;
    }
}

'''

def e = new Engine()

for(i in 2..20) {
    def fileR = new File("Task" + i + ".java")

    fileR.write e.createTemplate(tplr).make([pack: 'net.gliewe.generic.tasks', count: i]).toString()
}