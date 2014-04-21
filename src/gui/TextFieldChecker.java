package gui;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

abstract class TextFieldChecker implements ChangeListener<Boolean> {
	
	private TextField field;
	private String prompt;
	private HashMap<String, String> errorStrings;
	private boolean valid;
	
	public TextFieldChecker(TextField field, String prompt, HashMap<String, String> errorStrings) {
		this.field = field;
		this.prompt = prompt;
		this.errorStrings = errorStrings;
		field.setPromptText(prompt);
	}
	
	protected void validate() {
		valid = true;
		for (String s: errorStrings.keySet())
			if (field.getText().equals(s)) {
				field.setText("");
				field.setPromptText(errorStrings.get(s));
				valid = false;
			}
	}
	
	public boolean isValid() {
		return valid;
	}
}
