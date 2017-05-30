package net.q2ek.compileinfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.tools.FileObject;

/**
 * This class generates a java class source code file. It is used by
 * {@link CompileInfoAnnotationProcessor}
 *
 * @author Edze Kruizinga
 */
public class CompileInfoWriter {
	private final Writer writer;
	private final Properties properties;

	CompileInfoWriter(Writer writer, Properties properties) {
		this.writer = writer;
		this.properties = properties;
	}

	/**
	 * @throws IOException
	 *             when FileObject cannot be used.
	 * @throws IOProblem
	 *             when {@link IOException} happens
	 */
	static void writeFile(String packageName, String name, FileObject resource) throws IOException {
		try (OutputStream stream = resource.openOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(stream)) {
			new CompileInfoWriter(writer, System.getProperties()).write(packageName, name);
			writer.flush();
		}
	}

	/**
	 * @throws IOProblem
	 *             when {@link IOException} happens
	 */
	private void append(CharSequence value) {
		try {
			this.writer.append(value);
		} catch (IOException e) {
			throw new IOProblem("Could not append to writer " + this.writer + " value " + value, e);
		}
	}

	void write(String packageName, String name) {
		append("package " + packageName + ";\n\n");
		imports();
		classJavaDoc();
		classDeclaration(name);
		isoDateTimeConstant();
		zonedDateTimeConstant();
		writeLocalDateTime();
		writeZonedDateTime();
		writeTime();
		writePropertiesMap();
		writeProperties();
		writeKeySetMethod();
		writePropertiesMapCreater();
		classEnd();
	}

	private void imports() {
		append("import java.util.HashMap;\n");
		append("import java.util.Map;\n");
		append("import java.util.Set;\n");
		append("import java.time.LocalDateTime;\n");
		append("import java.time.ZonedDateTime;\n");
		append("\n");
	}

	private void classJavaDoc() {
		append("/**\n");
		append(" * @author Generated by " + CompileInfoAnnotationProcessor.class.getCanonicalName() + "\n");
		append(" * @see " + CompileInfo.class.getSimpleName() + "\n");
		append(" */\n");
	}

	private void classDeclaration(String name) {
		append("public class " + name + "\n");
		append("{\n");
	}

	private void classEnd() {
		append("}\n");
	}

	private void isoDateTimeConstant() {
		append("    static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.parse(\"");
		append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
		append("\");\n");
		append("    \n");
	}

	private void zonedDateTimeConstant() {
		append("    static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.parse(\"");
		append(ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
		append("\");\n");
		append("    \n");
	}

	private void writeLocalDateTime() {
		append("    static LocalDateTime localDateTime() {\n");
		append("        return LOCAL_DATE_TIME;\n");
		methodEnd();
	}

	private void writeZonedDateTime() {
		append("    static ZonedDateTime zonedDateTime() {\n");
		append("        return ZONED_DATE_TIME;\n");
		methodEnd();
	}

	private void writeTime() {
		append("    static String time() {\n");
		append("        return \"" + LocalDate.now() + " " + LocalTime.now() + "\";\n");
		methodEnd();
	}

	private void writePropertiesMap() {
		append("    private static final Map<String, String> properties = createMap();\n\n");
	}

	private void writePropertiesMapCreater() {
		append("    private static Map<String, String> createMap() {\n");
		append("        Map<String, String> result = new HashMap<>();\n");
		List<String> keys = sortedKeys(this.properties);
		for (String key : keys) {
			putKeyValue(key);
		}
		append("        return result;\n");
		methodEnd();
	}

	private void putKeyValue(String key) {
		String value = this.properties.get(key).toString();
		String filteredKey = filter(key);
		String filteredValue = filter(value);
		if (filteredKey.contains("\\\"")) {
			filteredKey = fixDoubleQuotes(filteredKey);
		}
		if (filteredValue.contains("\\\"")) {
			filteredValue = fixDoubleQuotes(filteredValue);
		}
		String mapPutCommand = String.format("        result.put(%s, %s);\n", filteredKey, filteredValue);
		append(mapPutCommand);
	}

	private static List<String> sortedKeys(Properties properties) {
		Set<Object> keySet = properties.keySet();
		List<String> keys = new ArrayList<>(keySet.size());
		keySet.forEach(key -> keys.add(key.toString()));
		Collections.sort(keys);
		return keys;
	}

	private void writeProperties() {
		append("    static String get(String key) {\n");
		append("        return properties.get(key);\n");
		methodEnd();
	}

	private void writeKeySetMethod() {
		addJavaDocToKeySetMethod();
		append("    static Set<String> keySet() {\n");
		append("        return properties.keySet();\n");
		methodEnd();
	}

	private void methodEnd() {
		append("    }\n\n");
	}

	private void addJavaDocToKeySetMethod() {
		append("    /**\n");
		append("     * @returns<br/>\n");
		List<String> keys = sortedKeys(this.properties);
		keys.forEach(key -> append("     * " + key + "<br/>\n"));
		append("     */\n");
	}

	private static String filter(String value) {
		return "\"" + filterDoubleQuotes(filterLineSeperators(value)) + "\"";
	}

	private static String filterLineSeperators(String value) {
		return value.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
	}

	private static String filterDoubleQuotes(String value) {
		return value.replace("\"", "\\\"");
	}

	private static String fixDoubleQuotes(String value) {
		String result = "new String(new char[]{";
		char[] charArray = value.toCharArray();
		for (char c : charArray) {
			if (c == '\\')
				result = result + "'\\\\', ";
			else
				result = result + "'" + c + "', ";
		}
		result = result + "})";
		return result;
	}
}
