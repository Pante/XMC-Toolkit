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
package com.karuslabs.commons.command.tree.nodes;

import com.karuslabs.commons.command.*;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.function.*;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An {@code ArgumentCommandNode} that provides additional convenience methods.
 * 
 * @param <T> the type of the source
 * @param <V> the type of the argument
 */
public class Argument<T, V> extends ArgumentCommandNode<T, V> implements Describable, Mutable<T> {
    
    /**
     * Creates an {@code Argument} builder with the given name and type.
     * 
     * @param <T> the type of the source
     * @param <V> the type of the argument
     * @param name the name
     * @param type the type of the argument
     * @return a {@code Builder}
     */
    public static <T, V> Builder<T, V> builder(String name, ArgumentType<V> type) {
        return new Builder<>(name, type);
    }
    
    /**
     * Creates an {@code Argument} builder with {@code CommandSender} as the source
     * type, and the given name and type.
     * 
     * @param <V> the type of the argument
     * @param name the name
     * @param type the type of the argument
     * @return a {@code Builder}
     */
    public static <V> Builder<CommandSender, V> of(String name, ArgumentType<V> type) {
        return new Builder<>(name, type);
    }
    
    private final Consumer<CommandNode<T>> addition;
    private final String description;
    private CommandNode<T> destination;
    
    /**
     * Creates an {@code Argument} with the given arguments.
     * 
     * @param name the name of the argument
     * @param type the type of the argument
     * @param description the description of this argument
     * @param command the command to be executed
     * @param requirement the requirement
     * @param suggestions the {@code SuggestionProvider}
     */
    public Argument(String name, ArgumentType<V> type, String description, Command<T> command, Predicate<T> requirement, SuggestionProvider<T> suggestions) {
        this(name, type, description, command, requirement, null, null, false, suggestions);
    }
    
    /**
     * Creates an {@code Argument} with the given arguments.
     * 
     * @param name the name of this argument
     * @param type the type of this argument
     * @param description the description for this argument
     * @param execution the execution to be executed
     * @param requirement the requirement
     * @param suggestions the {@code SuggestionProvider}
     */
    public Argument(String name, ArgumentType<V> type, String description, Execution<T> execution, Predicate<T> requirement, SuggestionProvider<T> suggestions) {
        this(name, type, description, execution, requirement, null, null, false, suggestions);
    }
    
    /**
     * Creates an {@code Argument} with the given arguments.
     * 
     * @param name the name of this argument
     * @param type the type of this argument
     * @param description the description for this argument
     * @param command the command to be executed
     * @param requirement the requirement
     * @param destination the destination to which this argument is redirected
     * @param modifier the redirection modifier
     * @param fork the fork
     * @param suggestions the {@code SuggestionProvider}
     */
    public Argument(String name, ArgumentType<V> type, String description, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork, SuggestionProvider<T> suggestions) {
        super(name, type, command, requirement, destination, modifier, fork, suggestions);
        this.destination = destination;
        this.description = description;
        this.addition = super::addChild;
    }
    
    
    @Override
    public String description() {
        return description;
    }
    
    
    @Override
    public void addChild(CommandNode<T> child) {
        Nodes.addChild(this, child, addition);
    }
    
    
    @Override
    public CommandNode<T> removeChild(String child) {
        return Commands.remove(this, child);
    }
    
    
    @Override
    public void setCommand(Command<T> command) {
        Commands.execution(this, command);
    }
    
    
    @Override
    public @Nullable CommandNode<T> getRedirect() {
        return destination;
    }

    @Override
    public void setRedirect(CommandNode<T> destination) {
        this.destination = destination;
    }

    /**
     * An {@code Argument} builder.
     * 
     * @param <T> the type of the source
     * @param <V> the type of the argument
     */
    public static class Builder<T, V> extends Nodes.Builder<T, Builder<T, V>> {
        
        private final String name;
        private final ArgumentType<V> type;
        private @Nullable SuggestionProvider<T> suggestions;
        
        /**
         * Creates a {@code Builder} with the given name and type.
         * 
         * @param name the name
         * @param type the type
         * 
         * @see #builder(String, ArgumentType) 
         * @see #of(String, ArgumentType) 
         */
        protected Builder(String name, ArgumentType<V> type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Sets the {@code SuggestionProvider}.
         * 
         * @param suggestions the {@code SuggestionProvider}
         * @return {@code this}
         */
        public Builder<T, V> suggests(SuggestionProvider<T> suggestions) {
            this.suggestions = suggestions;
            return getThis();
        }

        /**
         * Builds the {@code Argument}.
         * 
         * @return the {@code Argument}
         */
        @Override
        public Argument<T, V> build() {
            var parameter = new Argument<>(name, type, description, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), suggestions);
            for (var child : getArguments()) {
                parameter.addChild(child);
            }
            
            return parameter;
        }
        
        @Override
        protected Builder<T, V> getThis() {
            return this;
        }

    }
    
}
