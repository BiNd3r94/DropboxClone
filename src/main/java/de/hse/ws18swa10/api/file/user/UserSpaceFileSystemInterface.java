package de.hse.ws18swa10.api.file.user;

import java.io.IOException;
import java.util.List;

import de.hse.ws18swa10.api.file.StreamableFile;
import de.hse.ws18swa10.api.file.entity.FileInfo;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PathDoesntExistException;

public interface UserSpaceFileSystemInterface
{
	public String createFile(final User user, final StreamableFile file) 
		throws PathDoesntExistException, IOException;
	
	public List<FileInfo> createFromStreamableZip(User user, StreamableFile file) 
		throws PathDoesntExistException, IOException;
	
	public String createFolder(final User user, String path)
		throws PathDoesntExistException, IOException;
	
	public String moveFile(final User user, String sourcePath, String destinationPath)
		throws PathDoesntExistException, IOException;
	
	public String moveFolder(final User user, String sourcePath, String destinationPath)
		throws PathDoesntExistException, IOException;
	
	public void deleteFile(final User user, String path) 
		throws PathDoesntExistException, IOException;
	
	public void deleteFolder(final User user, String path)
		throws PathDoesntExistException, IOException;

	public byte[] getFileAsBinary(final User user, String path) 
		throws PathDoesntExistException, IOException;
	
	public byte[] getFileAsBinaryByHash(final User user, String pathHash) 
		throws PathDoesntExistException, IOException;

	public byte[] getFolderAsBinary(final User user, String path)
		throws PathDoesntExistException, IOException;
	
	public byte[] getFolderAsBinaryByHash(final User user, String pathHash)
		throws PathDoesntExistException, IOException;

	public List<FileInfo> listDirectory(final User user, String path)
		throws PathDoesntExistException, IOException;
	
	public FileInfo getFileInfo(final User user, String pathHash)
		throws PathDoesntExistException, IOException;
	
	public String getParentPathHashFor(String pathHash);
}
