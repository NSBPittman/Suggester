import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;

public class ExampleClass {
    public static List<String> makeDictionary(){
        List<String> dictionary = new ArrayList<>();
        dictionary.add("Patient has cancer");
        dictionary.add("Patient has AIDS");
        dictionary.add("Patient has allergies");
        dictionary.add("Patient has Alzheimer's disease");
        dictionary.add("Patient has chronic fatigue syndrome (CFS)");
        dictionary.add("Patient has common cold");
        dictionary.add("Patient has endometriosis");
        dictionary.add("Patient has Hashimoto's thyroiditis");
        dictionary.add("Patient has hepatitis");
        dictionary.add("Patient has Hodgkin's disease");
        dictionary.add("Patient has liver cancer");
        dictionary.add("Patient has myopia (short-sightedness)");
        dictionary.add("Patient has prostate disorders");
        dictionary.add("Patient has SARS");
        dictionary.add("Patient has thyroid disorders");
        dictionary.add("Patient has nauseous");
        dictionary.add("Patient has palpitations");
        dictionary.add("Patient has sleep disorders");
        return dictionary;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        JTextArea userInput = new JTextArea(1, 25);
        p.add(userInput);
        frame.add(p);
        frame.pack();
        frame.setVisible(true);

        List<String> dictionary = makeDictionary();

        SuggesterBuilder suggesterBuilder = new SuggesterBuilder();
        suggesterBuilder.setAll(userInput, dictionary, 1, SuggesterType.autocomplete);

        SuggesterView suggester = suggesterBuilder.createSuggester();


    }
}