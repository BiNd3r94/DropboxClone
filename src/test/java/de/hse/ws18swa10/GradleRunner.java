package de.hse.ws18swa10;

import java.io.File;
import java.net.URL;

public final class GradleRunner
{
	public void runGradleInitializeTestDbTask() throws Exception
	{
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		String binaryDir = new File(url.toURI()).getParent();
		String projectRootDir = binaryDir + File.separator + ".." + File.separator;
		
		ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "gradle initDb -Pmode=test");
		processBuilder.directory(new File(projectRootDir));
		Process process = processBuilder.start();
		process.waitFor();
					
		if (process.exitValue() != 0) {
			throw new Exception("Gradle initDb process exited with a non zero exit code");
		}
	}
}