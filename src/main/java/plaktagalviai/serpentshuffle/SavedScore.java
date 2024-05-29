package plaktagalviai.serpentshuffle;

import java.io.*;


public class SavedScore implements Serializable {
    private static final long serialVersionUID = 1L;
    private int score;
    private static final String SCORE_FILE = "savedScore.txt";

    public SavedScore() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Method to save the score to a file
    public void saveScore() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORE_FILE))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SavedScore loadScore() {
        File file = new File(SCORE_FILE);
        if (!file.exists()) {
            return new SavedScore(); // Return a new SavedScore if the file doesn't exist
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORE_FILE))) {
            return (SavedScore) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new SavedScore(); // Return a new SavedScore if there is an error
        }
    }
}