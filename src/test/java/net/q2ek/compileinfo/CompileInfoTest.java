package net.q2ek.compileinfo;

import java.time.LocalDateTime;

import org.junit.Test;

@CompileInfo
public class CompileInfoTest {

	@Test
	public void compileInfo_containsDatetime() {
		LocalDateTime actual = CompileInfoTestCompileInfo.localDateTime();
	}
}
