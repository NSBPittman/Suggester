import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Created by Nick Pittman on 1/19/2016.
 * AutoSuggesterView creates the view for AutoSuggester
 * It creates dropdown suggester panel for passed in model
 */
public class SuggesterView {

    //private final JTextField textField;
    private final JTextArea textArea;
    private final Window container;
    private JPanel suggestionsPanel;
    private JWindow autoSuggestionPopUpWindow;
    private String typedWord;
    private final ArrayList<String> dictionary = new ArrayList<>();
    private int currentIndexOfSpace, tW, tH;
    private ISuggester theModel;
    private int textAreaHeight;
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent de) {
        	if (textArea.isVisible())
        		checkForAndShowSuggestions();
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
        	if (textArea.isVisible())
        		checkForAndShowSuggestions();
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
        	if (textArea.isVisible())
        		checkForAndShowSuggestions();
        }
    };
    private final Color suggestionsTextColor;
    private final Color suggestionFocusedColor;

    /**
     * Constructor
     * @param textField where user enters their ideas
     * @param mainWindow window that AutoSuggestor occupies, used to size suggestionPanel
     * @param popUpBackground color for suggestionPanel
     * @param textColor text color for text in suggestionPanel
     * @param suggestionFocusedColor color to highlight focused suggestion
     * @param opacity opacity of suggestionPanel
     * @param modelIn model to be used to get suggestions
     */
    public SuggesterView(JTextArea textField, Window mainWindow, Color popUpBackground, Color textColor, Color suggestionFocusedColor, float opacity, ISuggester modelIn, int textAreaHeight) {
        this.textArea = textField;
        this.suggestionsTextColor = textColor;
        this.container = mainWindow;
        this.suggestionFocusedColor = suggestionFocusedColor;
        this.textArea.getDocument().addDocumentListener(documentListener);
        this.textAreaHeight = textAreaHeight;
        theModel = modelIn;

        typedWord = "";
        currentIndexOfSpace = 0;
        tW = 0;
        tH = 0;

        autoSuggestionPopUpWindow = new JWindow(mainWindow);
        autoSuggestionPopUpWindow.setOpacity(opacity);

        suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new GridLayout(0, 1));
        suggestionsPanel.setBackground(popUpBackground);

        addKeyBindingToRequestFocusInPopUpWindow();
    }

    /**
     * Adds action listener for down arrow key
     */
    private void addKeyBindingToRequestFocusInPopUpWindow() {
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
        textArea.getActionMap().put("Down released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
                for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
                    if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                        ((SuggestionLabel) suggestionsPanel.getComponent(i)).setFocused(true);
                        autoSuggestionPopUpWindow.toFront();
                        autoSuggestionPopUpWindow.requestFocusInWindow();
                        suggestionsPanel.requestFocusInWindow();
                        suggestionsPanel.getComponent(i).requestFocusInWindow();
                        break;
                    }
                }
            }
        });
        suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
        suggestionsPanel.getActionMap().put("Down released", new AbstractAction() {
            int lastFocusableIndex = 0;

            @Override
            public void actionPerformed(ActionEvent ae) {//allows scrolling of labels in pop window (I know very hacky for now :))

                List<SuggestionLabel> sls = getAddedSuggestionLabels();
                int max = sls.size();

                if (max > 1) {//more than 1 suggestion
                    for (int i = 0; i < max; i++) {
                        SuggestionLabel sl = sls.get(i);
                        if (sl.isFocused()) {
                            if (lastFocusableIndex == max - 1) {
                                lastFocusableIndex = 0;
                                sl.setFocused(false);
                                autoSuggestionPopUpWindow.setVisible(false);
                                setFocusToTextField();
                                if (textArea.isVisible())
                                	checkForAndShowSuggestions();//fire method as if document listener change occured and fired it

                            } else {
                                sl.setFocused(false);
                                lastFocusableIndex = i;
                            }
                        } else if (lastFocusableIndex <= i) {
                            if (i < max) {
                                sl.setFocused(true);
                                autoSuggestionPopUpWindow.toFront();
                                autoSuggestionPopUpWindow.requestFocusInWindow();
                                suggestionsPanel.requestFocusInWindow();
                                suggestionsPanel.getComponent(i).requestFocusInWindow();
                                lastFocusableIndex = i;
                                break;
                            }
                        }
                    }
                } else {//only a single suggestion was given
                    autoSuggestionPopUpWindow.setVisible(false);
                    setFocusToTextField();
                    checkForAndShowSuggestions();//fire method as if document listener change occured and fired it
                }
            }
        });
    }

    private void setFocusToTextField() {
        container.toFront();
        container.requestFocusInWindow();
        textArea.requestFocusInWindow();
    }

    /**
     * get's suggestion from panel
     * @return List of SuggestionLabel's
     */
    public List<SuggestionLabel> getAddedSuggestionLabels() {
        List<SuggestionLabel> sls = new ArrayList<>();
        for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
            if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
                sls.add(sl);
                //System.out.println("in getAddedSuggestionLabels, line 208:\n sl: " + sl + "\n sls: " + sls);
            }
        }
        return sls;
    }

    private void checkForAndShowSuggestions() {
        typedWord = textArea.getText();

        suggestionsPanel.removeAll();//remove previous words/jlabels that were added

        //used to calcualte size of JWindow as new Jlabels are added
        tW = 0;
        tH = 0;

        boolean added = wordTyped(typedWord);

        if (!added) {
            if (autoSuggestionPopUpWindow.isVisible()) {
                autoSuggestionPopUpWindow.setVisible(false);
                //System.out.println("In checkForAndShowSuggestion\n typedWord = " + typedWord);
            }
        } else {
            showPopUpWindow();
            setFocusToTextField();
        }
    }

    /**
     * Creates suggestionLabel and adds it to suggestionLabel
     * @param word string to create suggestionLabel of
     */
    protected void addWordToSuggestions(String word) {
        //System.out.println("Make Suggestion Label of: " + word);
        SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this, theModel);
        calculatePopUpWindowSize(suggestionLabel);
        suggestionsPanel.add(suggestionLabel);
    }


    private void calculatePopUpWindowSize(JLabel label) {
        //so we can size the JWindow correctly
        if (tW < label.getPreferredSize().width) {
            tW = label.getPreferredSize().width;
        }
        tH += label.getPreferredSize().height;
    }

    private void showPopUpWindow() {
        autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textArea.getWidth(), 30));
        autoSuggestionPopUpWindow.setSize(tW, tH);
        autoSuggestionPopUpWindow.setVisible(true);

        int windowX = 0;
        int windowY = 0;

        
        Point textAreaPT = textArea.getLocationOnScreen();
        
        windowX = textAreaPT.x;
        windowY = textAreaPT.y + (this.textAreaHeight*22);

        autoSuggestionPopUpWindow.setLocation(windowX, windowY);
        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textArea.getWidth(), 30));
        autoSuggestionPopUpWindow.revalidate();
        autoSuggestionPopUpWindow.repaint();

    }

    public void setDictionary(ArrayList<String> words) {
        dictionary.clear();
        if (words == null) {
            return;//so we can call constructor with null value for dictionary without exception thrown
        }
        for (String word : words) {
            dictionary.add(word);
        }
    }

    public JWindow getAutoSuggestionPopUpWindow() {
        return autoSuggestionPopUpWindow;
    }

    public Window getContainer() {
        return container;
    }

    public JTextComponent getTextField() {
        return textArea;
    }

    public void addToDictionary(String word) {
        dictionary.add(word);
    }

    boolean wordTyped(String typedWord) {
        List<String> res = null;

        if (typedWord.isEmpty()) {
            return false;
        }
        boolean suggestionAdded = false;
        try {
            res = theModel.calculateBestMatches(typedWord, 5);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        for (String hyp : res){
            addWordToSuggestions(hyp);
            suggestionAdded = true;
        }

        return suggestionAdded;
    }
}
