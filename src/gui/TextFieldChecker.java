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
	
	public TextFieldChecker(TextField field, String prompt, HashMap<String, String> errorStrings) {
		this.field = field;
		this.prompt = prompt;
		this.errorStrings = errorStrings;
		field.setPromptText(prompt);
	}
	
	public boolean validate() {
		for (String s: errorStrings.keySet())
			if (field.getText().equals(s)) {
				field.setText("");
				field.setPromptText(errorStrings.get(s));
				return false;
			}
		return true;
	}
}
