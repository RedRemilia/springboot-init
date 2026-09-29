package com.yupi.springbootinit.desig_parttern.command;

public class Paste implements Command {

    private final Editor editor;
    public Paste(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        editor.paste();
    }
}
