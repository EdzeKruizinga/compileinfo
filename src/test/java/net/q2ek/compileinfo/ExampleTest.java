package net.q2ek.compileinfo;

import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ExampleTest {
	private static final Pattern DATE_PATTERN = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d.*");
	private final MyClass myClass = new MyClass();

	@Test
	public void compileDateTime_containsSomething() {
		String actual = this.myClass.compileDateTime();
		Assertions.assertThat(actual).matches(DATE_PATTERN);
	}
}
