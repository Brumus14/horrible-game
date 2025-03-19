public class Tutorial {
    private String[] text = {
        "Long ago",
        "in a skibidi far far away",
        "there was a hobbit",
        "neville longbottom",
    };

    private int textIndex = 0;
    private GameArena arena;
    private Text currentText;
    private boolean finished = false;
    private CursorManager cursor;

    public Tutorial(GameArena a, CursorManager cursor) {
        arena = a;

        currentText = new Text(text[textIndex], 24, cursor.getWidth() / 2,
                               cursor.getHeight() / 2, "red");
        arena.addText(currentText);
    }

    public void nextText() {
        if (textIndex < text.length - 1) {
            textIndex++;

            currentText.setText(text[textIndex]);
        } else {
            finished = true;
            arena.removeText(currentText);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
