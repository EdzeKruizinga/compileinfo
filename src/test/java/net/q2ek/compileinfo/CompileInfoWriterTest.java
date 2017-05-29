package net.q2ek.compileinfo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.CharArrayWriter;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("static-method")
public class CompileInfoWriterTest {
	private static final String TEST_CLASS_NAME = "CompileInfoTestOutput";
	private static final String TEST_PACKAGE_NAME = "test.package";

	@Test
	public void write_resultContainsPackageAndClass() {
		CharArrayWriter writer = new CharArrayWriter();
		new CompileInfoWriter(writer).write(TEST_PACKAGE_NAME, TEST_CLASS_NAME);
		String actual = String.valueOf(writer.toCharArray());
		assertThat(actual).contains("package " + TEST_PACKAGE_NAME);
		assertThat(actual).contains("import java.util.HashMap;");
		assertThat(actual).contains("import java.util.Map;");
		assertThat(actual).contains("import java.util.Set;");
		assertThat(actual).contains("import java.time.LocalDateTime;");
		assertThat(actual).contains("@author");
		assertThat(actual).contains("class " + TEST_CLASS_NAME);
		assertThat(actual).contains("LocalDateTime localDateTime()");
	}

	@Ignore
	@Test
	public void ResultLooksNice() {
		CharArrayWriter writer = new CharArrayWriter();
		new CompileInfoWriter(writer).write(TEST_PACKAGE_NAME, TEST_CLASS_NAME);
		String result = String.valueOf(writer.toCharArray());
		System.out.println(result);
	}
}
