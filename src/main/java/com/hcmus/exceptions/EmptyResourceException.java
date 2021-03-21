package com.hcmus.exceptions;

public class EmptyResourceException extends RuntimeException {
	public EmptyResourceException() {
		super("Resource has not content");
	}
}
