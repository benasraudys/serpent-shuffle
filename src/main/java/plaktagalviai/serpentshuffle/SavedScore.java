package plaktagalviai.serpentshuffle;

import java.io.*;


public class SavedScore implements Serializable {
    private static int score;

    public SavedScore() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}