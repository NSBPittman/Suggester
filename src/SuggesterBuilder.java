import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class SuggesterBuilder {
	private JTextArea inputTextArea;
	private List<String> dictionary;
	private int textAreaHeight;
	private ISuggester suggesterModel;
	//private suggesterType;
	
	public SuggesterBuilder(){}

	public SuggesterBuilder setInputTextArea(JTextArea inputTextArea) {
		this.inputTextArea = inputTextArea;
		return this;
	}
	
	public SuggesterBuilder setDictionary(List<String> dictionary) {
		this.dictionary = dictionary;
		return this;
	}
	
	public SuggesterBuilder setTextAreaHeight(int textAreaHeight) {
		this.textAreaHeight = textAreaHeight;
		return this;
	}

	public SuggesterBuilder setModelType(SuggesterType modelType){
		switch(modelType){
		case autocomplete:
			this.suggesterModel = new AutoCompleteModel(dictionary, 3);
			break;
			
		case spellchecker:
			this.suggesterModel = new SpellChecker(dictionary, 3);
			break;
			
		case tfidf:
			this.suggesterModel = new TFIDFmodel(dictionary, .5);
			
		}
		return this;
	}
	
	public SuggesterBuilder setModelTypeInt(int typeNum) {
		if (typeNum == 1)
            this.dictionary = dicStringToWords(this.dictionary);
            this.suggesterModel = new AutoCompleteModel(dictionary, 3);
		if (typeNum == 2)
			this.suggesterModel = new SpellChecker(dictionary, 3);
		if (typeNum == 3)
			this.suggesterModel = new TFIDFmodel(dictionary, .5);
		
		return this;
	}
	
	private List<String> dicStringToWords(List<String> dictionaryIn){
		List<String> newDict = new ArrayList<String>();
		for (String cPhrase : dictionaryIn){
			String[] words = cPhrase.split("\\s+");
			for(String cWord : words){
				if (!newDict.contains(cWord)){
					newDict.add(cWord);
				}
			}
		}
		
		return newDict;
	}
	
	public SuggesterBuilder setAll(JTextArea inputTextArea, List<String> dictionary, int textAreaHeight, SuggesterType modelType) {
		this.inputTextArea = inputTextArea;
		this.dictionary = dictionary;
		this.textAreaHeight = textAreaHeight;
		switch(modelType){
		case autocomplete:
            this.dictionary = dicStringToWords(this.dictionary);
            this.suggesterModel = new AutoCompleteModel(this.dictionary, 3);
			break;
			
		case spellchecker:
			this.dictionary = dicStringToWords(this.dictionary);
			this.suggesterModel = new SpellChecker(this.dictionary, 3);
			break;
			
		case tfidf:
			this.suggesterModel = new TFIDFmodel(dictionary, .5);
			
		}
		return this;
	}
	
	public SuggesterView createSuggester() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//JPanel panel = new JPanel();
		Color popUpBackground = Color.WHITE.brighter();
		Color textColor = Color.BLUE;
		Color suggestionFocusedColor = Color.RED;
		float opacity = .75f;
		
		SuggesterView autoSuggester = new SuggesterView(
				this.inputTextArea, frame, popUpBackground, textColor, suggestionFocusedColor, 
				opacity, this.suggesterModel, textAreaHeight);
		
		return autoSuggester;
		
	}
	
}