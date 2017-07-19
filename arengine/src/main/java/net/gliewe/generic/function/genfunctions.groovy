import groovy.text.GStringTemplateEngine as Engine
 
def tplr = '''
package ${pack};

public interface IFuncR${count}<TR<% for(i in 1..count) {%>, T<%= i %><%}%>> {
    TR call(${((1..count).collect {'T'+it+" v"+it} as String[]).join(', ')});
}
'''

def tplv = '''
package ${pack};

public interface IFuncV${count}<${((1..count).collect {'T'+it} as String[]).join(', ')}> {
    void call(${((1..count).collect {'T'+it+" v"+it} as String[]).join(', ')});
}
'''

def e = new Engine()

for(i in 1..20) {
    def fileR = new File("IFuncR" + i + ".java")
    def fileV = new File("IFuncV" + i + ".java")

    fileR.write e.createTemplate(tplr).make([pack: 'net.gliewe.generic.function', count: i]).toString()
    fileV.write e.createTemplate(tplv).make([pack: 'net.gliewe.generic.function', count: i]).toString()
}