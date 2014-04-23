package gui;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldValidator implements ChangeListener<Boolean> {
	
	protected TextField field;
	protected String prompt;
	protected HashMap<String, String> invalidStrings;
	protected Pattern pattern;
	protected String patternMismatchMessage;
	protected boolean valid;
	
	public TextFieldValidator(TextField field, String prompt, HashMap<String, String> invalidStrings, String pattern, String patternMismatchMessage) {
		this.field = field;
		this.prompt = prompt;
		this.invalidStrings = invalidStrings;
		this.pattern = Pattern.compile(pattern);
		this.patternMismatchMessage = patternMismatchMessage;
		field.setPromptText(prompt);
	}
	
	protected void validate() {
		valid = true;
		for (String s: invalidStrings.keySet())
			if (valid && field.getText().equals(s)) {
				field.setText("");
				field.setPromptText(invalidStrings.get(s));
				valid = false;
			}
		
		if (valid) {
			Matcher matcher = pattern.matcher(field.getText());
			if (!matcher.matches()) {
				field.setText("");
				field.setPromptText(patternMismatchMessage);
				valid = false;
			}
		}
		
	}
	
	public boolean isValid() { return valid; }

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (!newValue)
			validate();
	}
}
