package net.q2ek.compileinfo;

import java.io.IOException;

public class IOProblem extends RuntimeException {

	public IOProblem(String message, IOException cause) {
		super(message, cause);
	}

}
