/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Webserv.ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.Iterator;
import java.util.NoSuchElementException;

import java.lang.reflect.Method;
import java.net.URI;

import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 *
 * @author Андрей
 */
@WebService(serviceName = "ILTemplateCompilerWebService")
public class ILTemplateCompilerWebService {

    
    public interface ICompiled { 
	//Double calcular(Number valorA, Number valorB);	 
        public void SayOutput();
    } 

    /**
     * Операция веб-службы
     */
    @WebMethod(operationName = "Compile")
    public String Compile(@WebParam(name = "source") String src) {
        CompileString comp = new CompileString();
        String s = "class CompiledCode{" + "   public static void main (String [] args){"
        + "      System.out.println (\"Hello, World\");"
        + "      System.out.println (args.length);" + "   }" + "}";
        comp.compile(s);
        return "Passed";
    }
    
    public class CompileString {
  public void compile(String str) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    String program = str;

    Iterable<? extends JavaFileObject> fileObjects;
    fileObjects = getJavaSourceFromString(program);

    compiler.getTask(null, null, null, null, null, fileObjects).call();

    Class<?> clazz = Class.forName("CompiledCode");
    //Method m = clazz.getMethod("CompiledCode", new Class[] { String[].class });
    //Object[] _args = new Object[] { new String[0] };
    //m.invoke(null, _args);
  }

  Iterable<JavaSourceFromString> getJavaSourceFromString(String code) {
    final JavaSourceFromString jsfs;
    jsfs = new JavaSourceFromString("code", code);
    return new Iterable<JavaSourceFromString>() {
      public Iterator<JavaSourceFromString> iterator() {
        return new Iterator<JavaSourceFromString>() {
          boolean isNext = true;

          public boolean hasNext() {
            return isNext;
          }

          public JavaSourceFromString next() {
            if (!isNext)
              throw new NoSuchElementException();
            isNext = false;
            return jsfs;
          }

          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }
    };
  }
}

class JavaSourceFromString extends SimpleJavaFileObject {
  final String code;

  JavaSourceFromString(String name, String code) {
    super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
    this.code = code;
  }

  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return code;
  }
}
}