package de.hse.ws18swa10.api.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.hse.ws18swa10.api.file.visitor.DeleteFilesVisitor;
import de.hse.ws18swa10.api.file.visitor.MoveFilesVisitor;

public class LocalStorageFileSystem implements FileSystemInterface
{
	private final PathProviderInterface pathProvider;
	private String prefixPath;
	
	public LocalStorageFileSystem(final PathProviderInterface pathProvider)
	{
		this.pathProvider = pathProvider;
		clearPrefixPath();
	}
	
	@Override
	public void setPrefixPath(String prefixPath)
	{
		this.prefixPath = prefixPath;
	}
	
	@Override
	public void clearPrefixPath()
	{
		this.prefixPath = "";
	}
	
	@Override
	public void createFolder(String path) throws IOException
	{
		Path fullPath = getFullPath(path);
		
		Files.createDirectory(fullPath);
	}
	
	@Override
	public void createFile(final InputStream inputStream, String path) 
		throws IOException
	{
		Path fullPath = getFullPath(path);
		
		Files.copy(inputStream, fullPath);
	}
	
	@Override
	public List<PathInfo> createFromZipFile(final File file, String path) throws IOException
	{
		List<PathInfo> rootPathInfos = new ArrayList<>();
		
		ZipFile zipFile = new ZipFile(file);
		Iterator<? extends ZipEntry> iterator = zipFile.stream().iterator();

		while (iterator.hasNext()) {
			ZipEntry zipEntry = iterator.next();
			
			if (zipEntry.isDirectory()) {
				createFileTree(new File(zipEntry.getName()), path);
			} else {
				createFile(zipFile.getInputStream(zipEntry), zipEntry.getName());
			}
			
			PathInfo pathInfo = new PathInfo();
			pathInfo.setName(zipEntry.getName());
			pathInfo.setIsDirectory(zipEntry.isDirectory());
			pathInfo.setLastModifiedAt(zipEntry.getLastModifiedTime().toMillis());
			
			rootPathInfos.add(pathInfo);
		}
		
		zipFile.close();
		
		return rootPathInfos;
	}

	@Override
	public void createFileTree(final File rootFile, String path)
		throws IOException
	{
		String[] files = rootFile.list();
		
		for (String fileName : files) {
			File file = new File(fileName);
			
			if (file.isDirectory()) {
				createFileTree(file, path);
			} else {
				createFile(new FileInputStream(file), path);
			}
		}
	}

	@Override
	public Path createTempFile(String path) throws IOException
	{
		Path fullPath = getFullTempPath(path);
		
		return Files.createFile(fullPath);
	}

	@Override
	public File writeToTemporaryFile(InputStream inputStream, String path) 
		throws IOException
	{
		Path tempFilePath = createTempFile(path);
		
		Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
		
		return tempFilePath.toFile();
	}

	@Override
	public void deleteTempFile(String path) throws IOException
	{
		Path fullPath = getFullTempPath(path);
		
		Files.delete(fullPath);
	}

	@Override
	public void moveFile(String sourcePath, String destinationPath) throws IOException
	{
		Path fullSourcePath = getFullPath(sourcePath);
		Path fullDestinationPath = getFullPath(destinationPath);
		
		if (fullDestinationPath.toFile().isDirectory() 
			|| !areParentDirsEqual(fullSourcePath, fullDestinationPath)) {
			fullDestinationPath = fullDestinationPath.resolve(fullSourcePath.getFileName());
		}
		
		Files.move(fullSourcePath, fullDestinationPath, StandardCopyOption.ATOMIC_MOVE);
	}
	
	private boolean areParentDirsEqual(Path left, Path right)
	{
		String leftParentDir = left.getParent().toString();
		String rightParentDir = right.getParent().toString();
		
		return leftParentDir.equals(rightParentDir);
	}
	
	@Override
	public void moveFolder(String sourcePath, String destinationPath) throws IOException
	{
		Path fullSourcePath = getFullPath(sourcePath);
		Path fullDestinationPath = getFullPath(destinationPath);
		
		if (! Files.exists(fullDestinationPath)) {
			fullSourcePath.toFile().renameTo(fullDestinationPath.toFile());
			return;
		}
				
		Files.walkFileTree(fullSourcePath, new MoveFilesVisitor(
			fullSourcePath,
			createDirectoryIn(fullSourcePath, fullDestinationPath)
		));
	}
	
	private Path createDirectoryIn(Path sourcePath, Path destinationPath) throws IOException
	{
		String rawSourcePath = sourcePath.toAbsolutePath().toString();
		String sourceFolderName = rawSourcePath.substring(rawSourcePath.lastIndexOf(File.separator));
		String path = destinationPath.toAbsolutePath().toString() + sourceFolderName;
		
		return Files.createDirectory(Paths.get(path));
	}

	@Override
	public boolean isWritable(String path)
	{
		Path pathWrapper = getFullPath(path);
		
		return exists(path) && Files.isWritable(pathWrapper);
	}
	
	@Override
	public boolean isReadable(String path)
	{
		Path pathWrapper = getFullPath(path);
		
		return exists(path) && Files.isReadable(pathWrapper);
	}

	@Override
	public boolean exists(String path)
	{
		Path pathWrapper = getFullPath(path);
		
		return Files.exists(pathWrapper);
	}
	
	@Override
	public void deleteFile(String path) throws IOException
	{
		Path fullPath = getFullPath(path);
		
		Files.delete(fullPath);
	}
	
	@Override
	public void deleteFolder(String path) throws IOException
	{
		Path fullPath = getFullPath(path);
		
		Files.walkFileTree(fullPath, new DeleteFilesVisitor());
	}

	@Override
	public byte[] getFileAsBinary(String path) throws IOException
	{
		Path fullPath = getFullPath(path);
		
		return Files.readAllBytes(fullPath);
	}
	
	@Override
	public byte[] getTempFileBytes(String path) throws IOException
	{
		Path fullPath = getFullTempPath(path);
		
		return Files.readAllBytes(fullPath);
	}

	@Override
	public File getFile(String path) throws IOException
	{
		return getFullPath(path).toFile();
	}
	
	@Override
	public List<File> listDirectory(String path) throws IOException
	{
		return Arrays.asList(getFullPath(path).toFile().listFiles());
	}

	@Override
	public Path getFullPath(String path)
	{	
		String fullPath = pathProvider.getRootPath() 
			+ File.separator 
			+ (! prefixPath.isEmpty() ? prefixPath + File.separator : "")
			+ ((path.startsWith(File.separator)) ? path.substring(path.lastIndexOf(File.separator)) : path);
		
		return Paths.get(fullPath);
	}
	
	private Path getFullTempPath(String path)
	{
		String fullTempPath = pathProvider.getTempPath()
			+ File.separator
			+ ((path.startsWith(File.separator)) ? path.substring(path.lastIndexOf(File.separator)) : path);
		
		return Paths.get(fullTempPath);
	}
}
