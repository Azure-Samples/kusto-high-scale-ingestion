package com.gslab.pepper.input.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * The Custom SimpleJavaFileObject which will have java source code.
 *
 * Created by trung on 5/3/15.
 *
 * Edited by Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 *
 * @Version 1.0
 * @since 5/3/15
 */

public class SourceCode extends SimpleJavaFileObject {
    private String contents = null;

    public SourceCode(String className, String contents) throws Exception {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = contents;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }
}
