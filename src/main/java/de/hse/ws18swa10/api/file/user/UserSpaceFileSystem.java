package de.hse.ws18swa10.api.file.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hse.ws18swa10.api.file.FileSystemInterface;
import de.hse.ws18swa10.api.file.StreamableFile;
import de.hse.ws18swa10.api.file.ZipStation;
import de.hse.ws18swa10.api.file.entity.FileInfo;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PathDoesntExistException;

public class UserSpaceFileSystem implements UserSpaceFileSystemInterface
{
	public static final String ZIP_FILE_SUFFIX = ".zip";
	
	private final FileSystemInterface fileSystem;
	private final UserPathEncoderInterface encoder;

	public UserSpaceFileSystem(
		final FileSystemInterface fileSystem, 
		final UserPathEncoderInterface encoder
	) {
		this.fileSystem = fileSystem;
		this.encoder = encoder;
	}
	
	@Override
	public String createFile(final User user, final StreamableFile file) 
		throws PathDoesntExistException, IOException
	{	
		String preparedTargetPath = stripOffInvalidPaths(file.getTargetPath());
		
		prepareWriteOnTarget(user, preparedTargetPath);
		
		String fullTargetPath = preparedTargetPath + File.separator + file.getDisposition().getFileName();
		throwOnTargetAlreadyExists(fullTargetPath);
		
		fileSystem.createFile(file.getInputStream(), fullTargetPath);
		fileSystem.clearPrefixPath();
		
		return encoder.encode(fullTargetPath);
	}
	
	@Override
	public List<FileInfo> createFromStreamableZip(final User user, final StreamableFile file)
		throws PathDoesntExistException, IOException
	{
		String targetPath = stripOffInvalidPaths(file.getTargetPath());
		File tempFile = fileSystem.writeToTemporaryFile(
			file.getInputStream(), 
			buildTempZipFileName(user, file.getDisposition().getFileName())
		);
		
		prepareWriteOnTarget(user, targetPath);
		
		List<FileInfo> rootFileInfos;
		
		try {
			rootFileInfos = fileSystem.createFromZipFile(tempFile, targetPath)
				.stream()
				.map((pathInfo) -> { 
					String hash = encoder.encode(targetPath + pathInfo.getName());
					
					FileInfo fileInfo = new FileInfo();
					fileInfo.setName(pathInfo.getName());
					fileInfo.setIsDirectory(pathInfo.isDirectory());
					fileInfo.setLastModifiedAt(pathInfo.getLastModifiedAt());
					fileInfo.setHash(hash);
					fileInfo.setParentHash(encoder.encode(targetPath));
					
					return fileInfo; 
				})
				.collect(Collectors.toList());
		} finally {
			fileSystem.clearPrefixPath();
		}
		
		fileSystem.deleteTempFile(tempFile.getAbsolutePath());
		
		return rootFileInfos;
	}
	
	@Override
	public String createFolder(final User user, String path)
		throws PathDoesntExistException, IOException
	{	
		prepareWriteOnFileSystem(user);
		
		String preparedPath = stripOffInvalidPaths(path);
		
		throwOnTargetAlreadyExists(preparedPath);
		
		fileSystem.createFolder(preparedPath);
		fileSystem.clearPrefixPath();
		
		return encoder.encode(preparedPath);
	}
	
	@Override
	public String moveFile(final User user, String sourcePath, String destinationPath)
		throws PathDoesntExistException, IOException
	{
		prepareWriteOnFileSystem(user);
		
		String preparedSourcePath = stripOffInvalidPaths(sourcePath);
		String preparedDestinationPath = stripOffInvalidPaths(destinationPath);
				
		fileSystem.moveFile(preparedSourcePath, preparedDestinationPath);
		fileSystem.clearPrefixPath();
		
		return encoder.encode(destinationPath);
	}
	
	@Override
	public String moveFolder(User user, String sourcePath, String destinationPath)
		throws PathDoesntExistException, IOException
	{
		prepareWriteOnFileSystem(user);
		
		String preparedSourcePath = stripOffInvalidPaths(sourcePath);
		String preparedDestinationPath = stripOffInvalidPaths(destinationPath);
		
		fileSystem.moveFolder(preparedSourcePath, preparedDestinationPath);
		fileSystem.clearPrefixPath();
		
		return encoder.encode(destinationPath);
	}
	
	@Override
	public void deleteFile(final User user, String path) 
		throws PathDoesntExistException, IOException
	{
		prepareWriteOnTarget(user, path);
		
		fileSystem.deleteFile(path);
		fileSystem.clearPrefixPath();
	}
	
	@Override
	public void deleteFolder(final User user, String path)
		throws PathDoesntExistException, IOException
	{
		prepareWriteOnTarget(user, path);
		
		fileSystem.deleteFolder(path);
		fileSystem.clearPrefixPath();
	}
	
	@Override
	public byte[] getFileAsBinary(final User user, String path) 
		throws PathDoesntExistException, IOException
	{
		prepareReadOnFileSystem(user, path);
		
		byte[] bytes = fileSystem.getFileAsBinary(path);
		fileSystem.clearPrefixPath();
		
		return bytes;
	}
	
	@Override
	public byte[] getFileAsBinaryByHash(User user, String pathHash) 
		throws PathDoesntExistException, IOException
	{
		return getFileAsBinary(user, encoder.decode(pathHash));
	}

	@Override
	public byte[] getFolderAsBinary(User user, String path) 
		throws PathDoesntExistException, IOException
	{
		prepareReadOnFileSystem(user, path);
		
		String tempZipFileName = buildTempZipFileName(user, path);
		Path tempZipPath = fileSystem.createTempFile(tempZipFileName);
		
		ZipStation zipStation = new ZipStation();
		zipStation.zipFolder(getFullPath(path), tempZipPath);
		
		byte[] bytes = fileSystem.getTempFileBytes(tempZipFileName);
				
		fileSystem.clearPrefixPath();
		fileSystem.deleteTempFile(tempZipFileName);
		
		return bytes;
	}

	@Override
	public byte[] getFolderAsBinaryByHash(User user, String pathHash) 
		throws PathDoesntExistException, IOException
	{
		return getFolderAsBinary(user, encoder.decode(pathHash));
	}

	@Override
	public List<FileInfo> listDirectory(final User user, String path) 
		throws PathDoesntExistException, IOException
	{
		prepareReadOnFileSystem(user, path);
		
		List<File> files = fileSystem.listDirectory(path);
		String userSpacePath = getFullPath("").toAbsolutePath().toString();
		
		List<FileInfo> fileInfos = new ArrayList<>();
		files.forEach((f) -> {
			FileInfo fileInfo = buildFileInfo(userSpacePath, f);
			fileInfos.add(fileInfo);
		});
		
		fileSystem.clearPrefixPath();
		
		return fileInfos;
	}
	
	@Override
	public String getParentPathHashFor(String pathHash)
	{
		String path = encoder.decode(pathHash);		
		int offset = path.lastIndexOf(File.separator);
		String parentPath = (offset <= 0 ? "/" : path.substring(0, offset));
				
		return encoder.encode(parentPath);
	}
	
	@Override
	public FileInfo getFileInfo(User user, String pathHash) 
		throws PathDoesntExistException, IOException
	{
		String path = encoder.decode(pathHash);
		
		prepareReadOnFileSystem(user, path);
		
		File file = fileSystem.getFile(path);
		String userSpacePath = getFullPath("").toAbsolutePath().toString();
		FileInfo fileInfo = buildFileInfo(userSpacePath, file);
		
		return fileInfo;
	}
	
	private FileInfo buildFileInfo(String userSpacePath, File file)
	{
		String relativeFilePath = getPathRelativeToUserSpace(
			userSpacePath, 
			file.getAbsolutePath()
		);
		String relativeParentPath = getPathRelativeToUserSpace(
			userSpacePath, 
			file.getParentFile().getAbsolutePath().toString()
		);
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setName(file.getName());
		fileInfo.setHash(encoder.encode(relativeFilePath));
		fileInfo.setParentHash(encoder.encode(relativeParentPath));
		fileInfo.setIsDirectory(file.isDirectory());
		fileInfo.setLastModifiedAt(file.lastModified());
		
		return fileInfo;
	}
	
	private String getPathRelativeToUserSpace(String userSpacePath, String path)
	{
		String relativeFilePath = path.replace(userSpacePath, "");
		
		if (relativeFilePath.startsWith(File.separator)) {
			relativeFilePath = relativeFilePath.substring(File.separator.length());
		}
		
		return relativeFilePath;
	}
		
	private String stripOffInvalidPaths(String path)
	{
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		
		path = path.replaceAll("(/*)\\.\\.(/*)", "/");
		
		return path.replaceAll("/", "\\" + File.separator);
	}

	private void prepareReadOnFileSystem(final User user, String targetPath) 
		throws IOException, PathDoesntExistException
	{
		if (! fileSystem.isReadable(user.getFileSpaceName())) {
			throw new IOException("Cannot read from " + user.getFileSpaceName());
		}
		
		fileSystem.setPrefixPath(user.getFileSpaceName());
		
		if (! fileSystem.isReadable(targetPath)) {
			throw new PathDoesntExistException(targetPath);
		}
	}
	
	private void prepareWriteOnFileSystem(final User user) throws IOException
	{
		if (! fileSystem.isWritable(user.getFileSpaceName())) {
			throw new IOException("Cannot write to " + user.getFileSpaceName());
		}
		
		fileSystem.setPrefixPath(user.getFileSpaceName());
	}

	private void prepareWriteOnTarget(final User user, String targetPath)
		throws PathDoesntExistException, IOException
	{
		prepareWriteOnFileSystem(user);
		
		if (! fileSystem.isWritable(targetPath)) {
			throw new PathDoesntExistException(targetPath);
		}
	}
	
	private void throwOnTargetAlreadyExists(final String fullTargetPath)
		throws IOException
	{
		if (fileSystem.exists(fullTargetPath)) {
			throw new IOException("A file already exists at destination path: " + fullTargetPath);
		}
	}
	
	private String buildTempZipFileName(final User user, String path)
	{
		String name = (path.isEmpty() ? "" : path.replaceAll("\\" + File.separator, "-"));
		
		return user.getFileSpaceName() 
			+ "_" 
			+ name
			+ System.currentTimeMillis()
			+ ZIP_FILE_SUFFIX;
	}
	
	protected Path getFullPath(String path)
	{
		return fileSystem.getFullPath(path);
	}
}
