package com.yupi.springbootinit.desig_parttern.command;

public class Copy implements Command {

    private final Editor editor;

    public Copy(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        editor.copy();
    }

}
