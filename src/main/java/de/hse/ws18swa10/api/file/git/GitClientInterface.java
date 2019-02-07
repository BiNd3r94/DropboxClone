package de.hse.ws18swa10.api.file.git;

import java.io.IOException;
import java.util.List;

public interface GitClientInterface
{	
	public void setProjectDir(String directory);
	
	public void init(String path) throws IOException;
	
	public void addAll(String path) throws IOException;
	
	public void commit(String path, String message) throws IOException;
	
	public List<String> getCommits(String path) throws IOException;
}
