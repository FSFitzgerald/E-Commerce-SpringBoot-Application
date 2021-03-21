package com.hcmus.exceptions;

public class IllegalException extends RuntimeException {
	public IllegalException(int id) {
		super("Illegal resource with id " + id);
	}
}
