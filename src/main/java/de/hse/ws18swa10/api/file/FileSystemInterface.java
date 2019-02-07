package de.hse.ws18swa10.api.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface FileSystemInterface
{
	public void setPrefixPath(String basePath);
	
	public void clearPrefixPath();
	
	public void createFolder(String path) throws IOException;
	
	public void createFile(final InputStream inputStream, String path) throws IOException;
	
	public List<PathInfo> createFromZipFile(final File file, String path) throws IOException;
		
	public void createFileTree(final File rootFile, String path) throws IOException;
	
	public Path createTempFile(String path) throws IOException;
	
	public File writeToTemporaryFile(final InputStream inputStream, String path)
		throws IOException;
	
	public void deleteTempFile(String path) throws IOException;
		
	public void moveFile(String sourcePath, String destinationPath) throws IOException;
	
	public void moveFolder(String sourcePath, String destinationPath) throws IOException;
	
	public boolean isWritable(String path);
	
	public boolean isReadable(String path);
	
	public boolean exists(String path);
	
	public void deleteFile(String path) throws IOException;
	
	public void deleteFolder(String path) throws IOException;
	
	public Path getFullPath(String path);

	public byte[] getFileAsBinary(String path) throws IOException;
	
	public byte[] getTempFileBytes(String path) throws IOException;
	
	public List<File> listDirectory(String path) throws IOException;
	
	public File getFile(String path) throws IOException;
}
