package net.q2ek.compileinfo;

import java.io.IOException;

/**
 * Simple exception to create a RuntimeException IOException wrapper
 *
 * @author Edze Kruizinga
 */
public class IOProblem extends RuntimeException {
	private static final long serialVersionUID = -5518438508622155938L;

	public IOProblem(String message, IOException cause) {
		super(message, cause);
	}

}
