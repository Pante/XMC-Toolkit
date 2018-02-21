/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.annotation.JDK9;
import com.karuslabs.commons.command.completion.Completion;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.command.completion.Completion.PLAYER_NAMES;
import static com.karuslabs.commons.configuration.Yaml.*;
import static java.util.Collections.EMPTY_MAP;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class CompletionsTokenTest {
    
    CompletionToken completion = when(mock(CompletionToken.class).from(any(), any())).thenReturn(PLAYER_NAMES).getMock();
    CompletionsToken token = new CompletionsToken(completion);
    
    
    @Test
    void getNull() {
        assertEquals(EMPTY_MAP, token.getNull(COMMANDS, "path"));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, help.completions", "false, help.aliases"})
    void isAssignable(boolean expected, String key) {
        assertEquals(expected, token.isAssignable(COMMANDS, key));
    }
    
    @ParameterizedTest
    @CsvSource({"negative, -1", "string, integer"})
    void isAssignable_ThrowsException(String key, String index) {
        assertEquals(
            "Invalid completion index: " + index + " at: " + key + "." + index + ", index must be a non-negative integer",
            assertThrows(ParserException.class, () -> token.isAssignable(INVALID, key)).getLocalizedMessage()
        );
    }
    
    
    @Test
    void get() {
        @JDK9("Replace with Map.of(...)")
        Map<Integer, Completion> completions = new HashMap<>();
        completions.put(0, PLAYER_NAMES);
        completions.put(2, PLAYER_NAMES);
        
        assertEquals(completions, token.from(COMMANDS, "help.completions"));
        
    }
    
}
