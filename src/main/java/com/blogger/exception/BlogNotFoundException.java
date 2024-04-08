package com.blogger.exception;

public class BlogNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BlogNotFoundException() {
        super("Blog not found.");
    }

    public BlogNotFoundException(String message) {
        super(message);
    }
}
