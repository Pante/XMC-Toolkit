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

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.effect.effects.Constants.*;
import static com.karuslabs.commons.world.Vectors.*;


/**
 * Represents a cylinder.
 * <br>
 * <img src = "https://thumbs.gfycat.com/BlaringVariableIndianpangolin-size_restricted.gif" alt = "Cylinder.gif">
 */
@Immutable
public class Cylinder extends ResettableEffect {
    
    private Particles particles;
    private float radius;
    private float height;
    private float ratio;
    private Vector angularVelocity;
    private Vector rotation;
    private int perRow;
    boolean rotate;
    private boolean solid;
    
    
    /**
     * Constructs a {@code Cylinder} with the specified particles, the default total number of steps, 200, radius, 1, height, 3, 
     * ratio of sides to total surface-area, 0.4884726437, angular velocity, (π / 200, π / 170, π / 155), rotation angle, (0, 0, 0), 
     * particles per row, 100, rotation, true, solidness, false.
     * 
     * @param particles the particles
     */
    public Cylinder(Particles particles) {
        this(particles, 200, 1, 3, 0.4884726437F, ANGULAR_VELOCITY, NONE, 100, true, false);
    }
    
    /**
     * Constructs a {@code Cylinder} with the specified particles, total number of steps, radius, height, ratio of sides to total surface-area,
     * angular velocity, rotation angle, particles per row, rotation, solidness.
     * 
     * @param particles the particles
     * @param steps the total number of steps
     * @param radius the radius
     * @param height the height
     * @param ratio the side to surface-area ratio
     * @param angularVelocity the angular velocity in radians
     * @param rotation the rotation in radians
     * @param perRow the number of particles per row
     * @param rotate true if the cylinder should rotate; else false
     * @param solid  true if the cylinder is solid; else false
     */
    public Cylinder(Particles particles, int steps, float radius, float height, float ratio, Vector angularVelocity, Vector rotation, int perRow, boolean rotate, boolean solid) {
        super(steps);
        this.particles = particles;
        this.radius = radius;
        this.height = height;
        this.ratio = ratio;
        this.angularVelocity = angularVelocity;
        this.rotation = rotation;
        this.perRow = perRow;
        this.rotate = rotate;
        this.solid = solid;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        ThreadLocalRandom random = ThreadLocalRandom.current();        
        renderSurface(context, origin, offset, random);
    }
    
    void renderSurface(Context context, Location origin, Vector offset, ThreadLocalRandom random) {
        int steps = context.steps();
        
        float x = (float) rotation.getX();
        float y = (float) rotation.getY();
        float z = (float) rotation.getZ();
        
        if (rotate) {
            x += steps * angularVelocity.getX();
            y += steps * angularVelocity.getY();
            z += steps * angularVelocity.getZ();
        }
        
        for (int i = 0; i < perRow; i += particles.getAmount(), steps++) {
            randomCircle(offset).multiply(radius);
            calculate(offset, random, solid ? random.nextFloat() : 1);
            
            if (rotate) {
                rotate(offset, x, y, z);
            }

            context.render(particles, origin, offset);
        }
        context.render(particles, origin);
        context.steps(steps);
    }
    
    void calculate(Vector offset, ThreadLocalRandom random, float multiple) {
        if (random.nextFloat() <= ratio) {
            offset.multiply(multiple).setY(random.nextDouble(-1, 1) * (height / 2));
            
        } else {
            offset.multiply(random.nextFloat());
            float y = multiple * (height / 2);
            if (random.nextBoolean()) {
                offset.setY(y);
                
            } else {
                offset.setY(-y);
            }
        }
    }
    
}
