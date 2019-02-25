package com.gslab.pepper.input.compile;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * The ExtendedStandardJavaFileManager which takes file objects and class loaders and passed to java compiler.
 *
 * Created by trung on 5/3/15.
 *
 * Edited by Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0
 * @since 5/3/15
 */

public class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    //Java Source file object
    private CompiledCode javaFileObject;

    //Custom classloader
    private DynamicClassLoader classLoader;

    /**
     * Constructor which initializes Fileobjects and class loaders
     * @param fileManager
     * @param javaFileObject
     * @param classLoader
     */
    protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, CompiledCode javaFileObject, DynamicClassLoader classLoader) {

        super(fileManager);

        this.javaFileObject = javaFileObject;
        this.classLoader = classLoader;
        this.classLoader.setCode(javaFileObject);
    }

    /**
     * Return current class
     * @param location
     * @param className
     * @param kind
     * @param sibling
     * @return
     * @throws IOException
     */
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return javaFileObject;
    }

    /**
     * Return current class loader
     * @param location
     * @return
     */
    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }
}
