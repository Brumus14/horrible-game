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

    public Tutorial(GameArena a) {
        arena = a;

        currentText = new Text(text[textIndex], 24, 50, 50, "red");
        arena.addText(currentText);
    }

    public void nextText() {
        if (textIndex < text.length) {
            textIndex++;

            currentText.setText(text[textIndex]);
        }
    }
}
