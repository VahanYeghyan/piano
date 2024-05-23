import com.fasterxml.jackson.databind.ObjectMapper;
import exception.WrongNumberException;
import model.Note;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/json/input.json";
    private static final String OUTPUT_FILE = "src/main/resources/json/output.json";
    private static final int MINIMUM_OCTAVE_NUMBER = -3;
    private static final int MAXIMUM_OCTAVE_NUMBER = 5;
    private static final int MINIMUM_NOTE_NUMBER = 1;
    private static final int MAXIMUM_NOTE_NUMBER = 12;

    public static void main(String[] args) throws WrongNumberException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Note noteInput = mapper.readValue(new File(INPUT_FILE), Note.class);
        List<int[]> values = noteInput.getValues();

        checkNotes(values);

        List<int[]> transposedNotes = getTransposedNotes(values, -3);
        Note noteOutput = new Note();
        noteOutput.setValues(transposedNotes);
        mapper.writeValue(new File(OUTPUT_FILE), noteOutput);
    }

    private static void checkNotes(List<int[]> noteList) throws WrongNumberException {
        for (int[] note : noteList) {
            int octaveNumber = note[0];
            int noteNumber = note[1];
            if (octaveNumber < MINIMUM_OCTAVE_NUMBER || octaveNumber > MAXIMUM_OCTAVE_NUMBER) {
                throw new WrongNumberException(String.format("Wrong octaveNumber: %s", octaveNumber));
            }
            if (noteNumber > MAXIMUM_NOTE_NUMBER || noteNumber < MINIMUM_NOTE_NUMBER){
                throw new WrongNumberException(String.format("Wrong noteNumber: %s", noteNumber));
            }
            if (octaveNumber == MAXIMUM_OCTAVE_NUMBER && noteNumber > 1 || octaveNumber == MINIMUM_OCTAVE_NUMBER && noteNumber < 10) {
                throw new WrongNumberException(String.format("Wrong octaveNumber: %s, noteNumber: %s",
                        octaveNumber, noteNumber));
            }
        }
    }

    private static List<int[]> getTransposedNotes(List<int[]> noteList, int transposeNumber)
            throws WrongNumberException {
        List<int[]> transposedNotes = new ArrayList<>();
        if (transposeNumber == 0) {
            //returning the same list
            return noteList;
        }
        for (int[] note : noteList) {
            int octaveNumber = note[0];
            int noteNumber = note[1];
            transposedNotes.add(getTransposedNote(octaveNumber, noteNumber, transposeNumber));
        }
        return transposedNotes;
    }

    private static int[] getTransposedNote(int octaveNumber, int noteNumber, int transposeNumber)
            throws WrongNumberException {
        //means it is a negative number
        if (transposeNumber < 0) {
            transposeNumber = -transposeNumber;
            return getDecreasedNoteNumber(octaveNumber, noteNumber, transposeNumber);
        } else {
            return getIncreasedNoteNumber(octaveNumber, noteNumber, transposeNumber);
        }
    }

    private static int[] getIncreasedNoteNumber(int octaveNumber, int noteNumber, int transposeNumber)
            throws WrongNumberException {
        int[] result = new int[2];
        for (int i = 0; i < transposeNumber; i++) {
            if (noteNumber == MAXIMUM_NOTE_NUMBER) {
                noteNumber = MINIMUM_NOTE_NUMBER;
                octaveNumber++;
            } else {
                noteNumber++;
            }
        }
        result[0] = octaveNumber;
        result[1] = noteNumber;
        if (result[0] == MAXIMUM_OCTAVE_NUMBER && result[1] > 1) {
            throw new WrongNumberException(String.format("Resulting notes falls out of the keyboard range, " +
                    "octaveNumber: %s, noteNumber: %s", octaveNumber, noteNumber));
        }
        return result;
    }

    private static int[] getDecreasedNoteNumber(int octaveNumber, int noteNumber, int transposeNumber)
            throws WrongNumberException {
        int[] result = new int[2];
        for (int i = 0; i < transposeNumber; i++) {
            if (noteNumber == MINIMUM_NOTE_NUMBER) {
                noteNumber = MAXIMUM_NOTE_NUMBER;
                octaveNumber--;
            } else {
                noteNumber--;
            }
        }
        result[0] = octaveNumber;
        result[1] = noteNumber;
        if (result[0] == MINIMUM_OCTAVE_NUMBER && result[1] < 10) {
            throw new WrongNumberException(String.format("Resulting notes falls out of the keyboard range, " +
                    "octaveNumber: %s, noteNumber: %s", octaveNumber, noteNumber));
        }
        return result;
    }
}
