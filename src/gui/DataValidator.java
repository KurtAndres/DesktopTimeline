package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;

public class DataValidator implements ChangeListener<Boolean> {
	
	private String prompt;
	private String format;
	private String errorMessage;
	
	private TextInputControl field;
	
	public DataValidator(TextInputControl field, String prompt, String format, String errorMessage) {
		this.field = field;
		this.prompt = prompt;
		this.format = format;
		this.errorMessage = errorMessage;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
		// TODO Auto-generated method stub
		
	}

}
