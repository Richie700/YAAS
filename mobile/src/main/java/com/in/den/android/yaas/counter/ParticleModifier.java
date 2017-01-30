package com.in.den.android.yaas.counter;

import com.in.den.android.yaas.engine.GameEngine;

/**
 * Created by harumi on 26/01/2017.
 */

public interface ParticleModifier {
    public void apply(ParticleSystem.Particle particle, long elapsedMillis);
}
