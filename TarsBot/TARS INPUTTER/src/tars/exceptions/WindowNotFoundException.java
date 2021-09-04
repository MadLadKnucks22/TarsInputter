package tars.exceptions;

public class WindowNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public WindowNotFoundException(String errorMsg){
		super(errorMsg);
	}
}
