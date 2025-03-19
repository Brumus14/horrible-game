public class Tutorial {
    private String[] text = {
        "Welcome to !!! HORRIBLE GAME !!! (use t to continue, y to skip)",
        "Controls: WASD = move, Mouse/Arrow keys = turning, ...",
        "Ctrl = crouch, shift = sprint, Esc = pause, P = quit, ...",
        "M = disable mouse, K = decrease mouse, L = increase mouse, ...",
        "The objective is to get to the end of the 'maze', ...",
        "The bottom displays breath, Sprinting uses breath, ...",
        "crouching makes you hold your breath and makes you silent ...",
        "also watch your heartbeat on screen it may be helpful",
        "try not to make much noise because ...",
        "it cant see you but it can hear you 😈"};

    private int textIndex = 0;
    private GameArena arena;
    private Text currentText;
    private boolean finished = false;
    private CursorManager cursor;

    public Tutorial(GameArena a, CursorManager c) {
        arena = a;
        cursor = c;

        currentText =
            new Text(text[textIndex], 24,
                     cursor.getWidth() / 2 - (6 * text[textIndex].length()),
                     cursor.getHeight() / 2, "red");
        arena.addText(currentText);
    }

    public void nextText() {
        if (textIndex < text.length - 1) {
            textIndex++;

            currentText.setText(text[textIndex]);
            currentText.setXPosition(cursor.getWidth() / 2 -
                                     (6 * text[textIndex].length()));
        } else {
            finished = true;
            arena.removeText(currentText);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void endTutorial() {
        finished = true;
        arena.removeText(currentText);
    }
}
