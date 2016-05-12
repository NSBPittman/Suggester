import java.io.IOException;
import java.util.List;

/**
 * Created by Nick Pittman on 1/21/2016.
 */
public interface ISuggester {
    /**
     * Calculates the closest word or sentence based on user's input
     * @param line string represents what is being sent in to check against dictionaries
     * @param numMatches number of matches to be returned
     * @return An ArrayList of the closest matches
     */
    public List<String> calculateBestMatches(String line, int numMatches) throws IOException;

    /**
     * Assembles a new sentence with the selected suggestion
     * @param suggestedWord string that has been suggested to the user
     * @param text string of what the user has entered
     * @return string that is full sentence based on what has been entered and what has been suggested
     */
    //public String getSuggested(String suggestedWord, String text);

}
