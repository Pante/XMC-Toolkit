/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.typist.Command;
import com.karuslabs.utilitary.Source;

import java.util.*;
import javax.lang.model.element.TypeElement;

/**
 * A generator which emits the body of a method that creates commands from a class
 * annotated with {@code @Command}.
 */
public record MethodBody(CommandInstantiation instantiation) {
    
    /**
     * Emits the body of a method that creates commands from a class annotated with
     * {@code @Command}.
     * 
     * @param source the source
     * @param site the class annotated with {@code @Command}
     * @param commands the commands declared in the scope of the class
     */
    public void emit(Source source, TypeElement site, Collection<Command> commands) {
        source.line("public static Map<String, CommandNode<CommandSender>> of(", site.getQualifiedName(), " source) {")
              .indent()
                .line("var commands = new HashMap<String, CommandNode<CommandSender>>();");
                 for (var command : commands) {
                     var name = emit(source, command);
                     source.line("commands.put(", name, ".getName(), ", name, ");")
                           .line();
                 }
          source.line("return commands;")
              .unindent()
              .line("}");
    }
    
    /**
     * Recursively emits the instantiation of the given command and it's children.
     * The children are instantiated before the parent. 
     * 
     * @param source the source
     * @param command the command
     * @return the generated variable's name
     */
    String emit(Source source, Command command) {
        var children = new ArrayList<String>();
        for (var child : command.children().values()) {
            children.add(emit(source, child));
            source.line();
        }
        
        var name = instantiation.emit(source, command);
        for (var child : children) {
            source.line(name, ".addChild(", child, ");");
        }
        
        return name;
    }
    
}
