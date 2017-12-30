/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.effect.effects;

import com.karuslabs.commons.annotation.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


/**
 * Represents a cone.
 * <br>
 * <img src = "https://thumbs.gfycat.com/RegularUnknownFanworms-size_restricted.gif" alt = "Cone.gif">
 */
@Immutable
public class Cone implements Effect {
    
    private Particles particles;
    private float lengthGrowth;
    private float radiusGrowth;
    private float angularVelocity;
    private int size;
    private int perIteration;
    private float rotation;
    private boolean randomize;
    
    
    /**
     * Constructs a {@code Cone} with the specified particles, 
     * the default growth in length, 0.5, growth in radius, 0.006, angular velocity, π / 60,
     * size, 180, particles per iteration, 10, rotation, 0.6, randomization, false.
     * 
     * @param particles the particles
     */
    public Cone(Particles particles) {
        this(particles, 0.5F, 0.006F, (float) PI / 16, 180, 10, 0F, false);
    }
    
    /**
     * Constructs a {@code Cone} with the specified particles, growth in length, growth in radius, angular velocity,
     * size, particles per iteration, rotation, randomization.
     * 
     * @param particles the particles
     * @param lengthGrowth the growth in length per iteration
     * @param radiusGrowth the growth in radius per iteration
     * @param angularVelocity the angular velocity in radians
     * @param size the size
     * @param perIteration the total number of particles per iteration
     * @param rotation the rotation in radians
     * @param randomize true if the rotation of the cone is randomized each iteration; else false
     */
    public Cone(Particles particles, float lengthGrowth, float radiusGrowth, float angularVelocity, int size, int perIteration, float rotation, boolean randomize) {
        this.particles = particles;
        this.lengthGrowth = lengthGrowth;
        this.radiusGrowth = radiusGrowth;
        this.angularVelocity = angularVelocity;
        this.size = size;
        this.perIteration = perIteration;
        this.rotation = rotation;
        this.randomize = randomize;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        int steps = context.steps();
        float pitch = (float) toRadians(origin.getPitch() + 90);
        float yaw = (float) toRadians(-origin.getYaw());
        
        for (int i = 0; i < perIteration; i += particles.getAmount(), steps++) {
            if (steps > size) {
                steps = 0;
            }
            if (steps == 0 && randomize) {
                rotation = (float) randomAngle();
            }
            
            float angle = steps * angularVelocity + rotation;
            float length = steps * lengthGrowth;
            float radius = steps * radiusGrowth;
            
            offset.setX(cos(angle) * radius).setY(length).setZ(sin(angle) * radius);
            
            rotateAroundXAxis(offset, pitch);
            rotateAroundYAxis(offset, yaw);

            context.render(particles, origin, offset);
        }
        context.steps(steps);
    }
    
}
