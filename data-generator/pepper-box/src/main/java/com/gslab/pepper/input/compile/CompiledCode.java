package com.gslab.pepper.input.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The Custom SimpleJavaFileObject which will have compiled code in the form of byte array stream.
 *
 * Created by trung on 5/3/15.
 *
 * Edited by Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0
 * @since 5/3/15
 */
public class CompiledCode extends SimpleJavaFileObject {

    //Output stream array on which compiled class is written
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


    /**
     *Constructor which initializes with class.
     *
     * @param className
     * @throws URISyntaxException
     */
    public CompiledCode(String className) throws URISyntaxException {
        super(new URI(className), Kind.CLASS);
    }

    /**
     * Returns output stream objects on which byte code is written
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        return outputStream;
    }

    /**
     *Returns compiled code in the form of java byte array
     *
     * @return byte code
     */
    public byte[] getByteCode() {
        return outputStream.toByteArray();
    }
}
