package com.gslab.pepper.loadgen;

import java.io.Serializable;

/**
 * The BaseLoadGenerator is base load generator.
 *
 * @Author Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0
 * @since 01/03/2017
 */
public interface BaseLoadGenerator  extends Serializable{

    public Object nextMessage();
}
