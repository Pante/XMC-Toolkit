/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karusmc.xmc.util;

import com.karusmc.xmc.commands.DispatchCommand;
import com.karusmc.xmc.core.XMCommand;

import java.util.*;

import junitparams.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class CommandsTest {
    
    private Map<String, XMCommand> commands;
    
    private DispatchCommand command;
    
    public CommandsTest() {
        commands = new HashMap<>();
        
        command = new DispatchCommand(null, "command");
        
        DispatchCommand subcommandA = new DispatchCommand(null, "subcommandA");
        
        XMCommand subsubcommandA = mock(XMCommand.class);
        when(subsubcommandA.getName()).thenReturn("subsubcommandA");
        
        XMCommand subcommandB = mock(XMCommand.class);
        when(subcommandB.getName()).thenReturn("subcommandB");
        
        
        subcommandA.getCommands().put(subsubcommandA.getName(), subsubcommandA);
        
        command.getCommands().put(subcommandA.getName(), subcommandA);
        command.getCommands().put(subcommandB.getName(), subcommandB);
    }
    
    
    @Test
    @Parameters(method = "getOrDefault_ReturnsArgument_parameters")
    public void getOrDefault_ReturnsArgument(String[] arguments, int index, String expected) {
        String returned = Commands.getArgumentOrDefault(arguments, index, "");
        assertEquals(expected, returned);
    }
    
    public Object[] getOrDefault_ReturnsArgument_parameters() {
        return new Object[] {
            new Object[] {new String[] {"1", "2", "3"}, 1, "2"},
            new Object[] {new String[] {"1", "2", "3"}, 2, "3"},
            new Object[] {new String[] {"1", "2", "3"}, 3, ""}
        };
    }   
    
    
    @Test
    public void flatMapTo() {
        Commands.flatMap(command, commands);
        
        assertEquals(4, commands.size());
        assertTrue(commands.keySet().containsAll(Arrays.asList("command", "command subcommandA", "command subcommandA subsubcommandA", "command subcommandB")));
    } 
    
    
}
