package com.blogger.exception;

public class CommentNotFoundException  extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentNotFoundException() {
        super("Comment not found.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
