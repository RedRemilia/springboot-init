package com.yupi.springbootinit.desig_parttern.command;

import java.util.ArrayList;
import java.util.List;

public class EditorInvoker {

    private final List<Command> commands = new ArrayList<>();

    public Command addCommand(Command command) {
        commands.add(command);
        return command;
    }

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

}
