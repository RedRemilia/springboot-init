package com.yupi.springbootinit.desig_parttern.command;

import java.util.HashMap;

public class TestCommand {

    public static void main(String[] args) {
        Editor editor = new Editor();
        Command copyCommand = new Copy(editor);
        Command pasteCommand = new Paste(editor);

        EditorInvoker editorInvoker = new EditorInvoker();
        editorInvoker.addCommand(copyCommand);
        editorInvoker.addCommand(pasteCommand);
        editorInvoker.execute();
    }
}
