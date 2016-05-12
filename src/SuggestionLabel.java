import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Nick Pittman on 2/15/2016.
 * creates SuggestionLabels
 * Suggestion Labels are an extension of JLabel
 * Contains Suggested String, and adds ability for the Label to be highlighted and selected
 */
public class SuggestionLabel extends JLabel{

    private boolean focused = false;
    private final JWindow autoSuggestionsPopUpWindow;
    
    //private final JTextField textField;
    private final JTextComponent textField;
    
    private final SuggesterView autoSuggestor;
    private Color suggestionsTextColor, suggestionBorderColor;
    private ISuggester theModel;
    private String selectedString;

    /**
     * Constructor for SuggestionLabel
     * @param string string for contents of SuggestionLabel
     * @param borderColor color to outline label in when it is focused
     * @param suggestionsTextColor color of text for SuggestionLabel
     * @param autoSuggestor AutoSuggester, used for getTextField and getAutoSuggestionPopUpWindow
     */
    public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, SuggesterView autoSuggestor, ISuggester theModel) {
        super(string);
        this.suggestionsTextColor = suggestionsTextColor;
        this.autoSuggestor = autoSuggestor;
        this.textField = autoSuggestor.getTextField();
        this.suggestionBorderColor = borderColor;
        this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();
        //this.theModel = theModel;
        this.selectedString = string;
        //System.out.println("In SuggestionLabel constructor: " + string);
        initComponent();
    }

    /**
     * Adds listeners for mouse click selection and enter button selection
     */
    private void initComponent() {
        setFocusable(true);
        setForeground(suggestionsTextColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);

                replaceWithSuggestedText();

                autoSuggestionsPopUpWindow.setVisible(false);
            }
        });

        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
        getActionMap().put("Enter released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                replaceWithSuggestedText();
                autoSuggestionsPopUpWindow.setVisible(false);
            }
        });
    }

    /**
     * Changes style of SuggestionLabel if it's focused
     * @param focused boolean for if SuggestionLabel is focused
     */
    public void setFocused(boolean focused) {
        if (focused) {
            setBorder(new LineBorder(suggestionBorderColor));
        } else {
            setBorder(null);
        }
        repaint();
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    /**
     * Replaces string in textbox with the selectedString
     */
    private void replaceWithSuggestedText() {
//        String suggestedWord = getText();
//        String text = textField.getText();
//        String tmp = theModel.getSuggested(suggestedWord, text);
//        textField.setText(tmp + " ");
        //System.out.println("replace: " + selectedString);
        textField.setText(selectedString);
    }
}


