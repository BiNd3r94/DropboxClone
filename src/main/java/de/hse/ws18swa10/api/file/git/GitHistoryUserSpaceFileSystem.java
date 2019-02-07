package de.hse.ws18swa10.api.file.git;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.hse.ws18swa10.api.file.FileSystemInterface;
import de.hse.ws18swa10.api.file.StreamableFile;
import de.hse.ws18swa10.api.file.entity.FileInfo;
import de.hse.ws18swa10.api.file.user.UserPathEncoderInterface;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystem;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PathDoesntExistException;

public class GitHistoryUserSpaceFileSystem extends UserSpaceFileSystem
{	
	private static final String GIT_DIR_NAME = ".git";
	
	private final GitCommitMessageBuilder builder;
	private final GitClientInterface gitClient;

	public GitHistoryUserSpaceFileSystem(
		final GitCommitMessageBuilder builder,
		final GitClientInterface gitClient, 
		final FileSystemInterface fileSystem,
		final UserPathEncoderInterface encoder
	) {
		super(fileSystem, encoder);
		this.builder = builder;
		this.gitClient = gitClient;
	}

	@Override
	public String createFile(final User user, StreamableFile file) 
		throws PathDoesntExistException, IOException
	{
		String hash = super.createFile(user, file);
		commit(user, builder.buildCreateMessage(
			"Created file " + file.getDisposition().getFileName()
		));
		
		return hash;
	}

	@Override
	public String createFolder(final User user, String path) 
		throws PathDoesntExistException, IOException
	{
		String pathHash = super.createFolder(user, path);
		/*commit(user, builder.buildCreateMessage(
			"Created folder " + path
		));*/
		
		return pathHash;
	}
	
	@Override
	public List<FileInfo> createFromStreamableZip(User user, StreamableFile file) 
		throws PathDoesntExistException, IOException
	{
		List<FileInfo> rootFilesInfos = super.createFromStreamableZip(user, file);
		commit(user, builder.buildCreateMessage(
			"Created from zip file " + file.getDisposition().getFileName()
		));
		
		return rootFilesInfos;
	}

	@Override
	public String moveFile(final User user, String sourcePath, String destinationPath)
		throws PathDoesntExistException, IOException
	{
		String newPathHash = super.moveFile(user, sourcePath, destinationPath);
		commit(user, builder.buildChangeMessage(
			"Moved file " + sourcePath + " to " + destinationPath
		));
		
		return newPathHash;
	}

	@Override
	public String moveFolder(final User user, String sourcePath, String destinationPath)
		throws PathDoesntExistException, IOException
	{
		String newPathHash = super.moveFolder(user, sourcePath, destinationPath);
		commit(user, builder.buildChangeMessage(
			"Moved folder " + sourcePath + " to " + destinationPath
		));
		
		return newPathHash;
	}

	@Override
	public void deleteFile(final User user, String path) 
		throws PathDoesntExistException, IOException
	{
		super.deleteFile(user, path);
		commit(user, builder.buildDeleteMessage(
			"Deleted file " + path
		));
	}

	@Override
	public void deleteFolder(final User user, String path) 
		throws PathDoesntExistException, IOException
	{
		super.deleteFolder(user, path);
		commit(user, builder.buildDeleteMessage(
			"Deleted folder " + path
		));
	}
	
	@Override
	public List<FileInfo> listDirectory(User user, String path) 
		throws PathDoesntExistException, IOException
	{
		return super.listDirectory(user, path).stream().filter((f) -> !GIT_DIR_NAME.equals(f.getName()))
			.collect(Collectors.toList());
	}
	
	private void commit(User user, String message) throws IOException
	{
		String fulluserSpacePath = getFullUserSpacePath(user);
		gitClient.addAll(fulluserSpacePath);
		gitClient.commit(fulluserSpacePath, message);
	}
	
	private String getFullUserSpacePath(User user)
	{
		return getFullPath(user.getFileSpaceName()).toAbsolutePath().toString();
	}
}
