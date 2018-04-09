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
package com.karuslabs.commons.command.annotation.resolvers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.*;
import com.karuslabs.commons.command.completion.Completion;


public class RegisteredResolver implements Resolver {
    
    private References references;
    
    
    public RegisteredResolver(References references) {
        this.references = references;
    }

    
    @Override
    public void resolve(CommandExecutor executor, Command... commands) {
        for (Registered registered : executor.getClass().getAnnotationsByType(Registered.class)) {
            resolve(commands, executor, registered);
        }
    }
    
    protected void resolve(Command[] commands, CommandExecutor executor, Registered registered) {
        Completion completion = references.getCompletion(registered.completion());
        if (completion != null) {
            for (Command command : commands) {
                command.getCompletions().put(registered.index(), completion);
            }
            
        } else {
            throw new IllegalArgumentException("Unresolvable reference: \"" + registered.completion() + "\" for: " + executor.getClass().getName());
        }
    }
    

    @Override
    public boolean isResolvable(CommandExecutor executor) {
        return executor.getClass().getAnnotationsByType(Registered.class).length != 0;
    }
    
}
