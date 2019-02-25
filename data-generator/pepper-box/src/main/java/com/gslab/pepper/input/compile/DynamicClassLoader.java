package com.gslab.pepper.input.compile;

import java.util.HashMap;
import java.util.Map;

/**
 * The DynamicClassLoader is custom class loader which loads class using compiled class byte array.
 *
 * Created by trung on 5/3/15.
 *
 * Edited by Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0
 * @since 5/3/15.
 */
public class DynamicClassLoader extends ClassLoader {

    /**
     *Map which represents class name and its compiled java object
     */
    private Map<String, CompiledCode> javaFileObjectMap = new HashMap<>();

    /**
     * Initialize custom classloader with current existing classloader
     * @param parent
     */
    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * Add Compiled class object for given JavaFileObject
     * @param fileObject
     */
    public void setCode(CompiledCode fileObject) {
        javaFileObjectMap.put(fileObject.getName(), fileObject);
    }

    /**
     * Define class using compiled code
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        //Get compiled code from class name
        CompiledCode fileObject = javaFileObjectMap.get(name);

        //If class is not present in map then do class lookup
        if (fileObject == null) {
            return super.findClass(name);
        }

        //Get byt code from fileObject
        byte[] byteCode = fileObject.getByteCode();

        //Define class
        return defineClass(name, byteCode, 0, byteCode.length);
    }
}
