/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist;

import com.karuslabs.typist.Identity.Type;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.typist.Identity.Type.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

class IdentityTest {

    static final Identity argument = new Identity(ARGUMENT, "a");
    static final Identity literal = new Identity(LITERAL, "b");
    
    @ParameterizedTest
    @MethodSource("equals_parameters")
    void equals(Object other, boolean expected) {
        assertEquals(expected, argument.equals(other));
        assertEquals(expected, argument.hashCode() == other.hashCode());
    }
    
    static Stream<Arguments> equals_parameters() {
        return Stream.of(
            of(argument, true),
            of(new Identity(ARGUMENT, "a"), true),
            of(new Object(), false)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("toString_parameters")
    void toString_(Identity identity, String expected) {
        assertEquals(expected, identity.toString());
    }
    
    static Stream<Arguments> toString_parameters() {
        return Stream.of(
            of(argument, "<a>"),
            of(literal, "b")
        );
    }
    
}

class TypeTest {
    
    @ParameterizedTest
    @MethodSource("toString_parameters")
    void toString_(Type type, String expected) {
        assertEquals(expected, type.toString());
    }
    
    static Stream<Arguments> toString_parameters() {
        return Stream.of(
            of(ARGUMENT, "argument"),
            of(LITERAL, "literal")
        );
    }
    
}
