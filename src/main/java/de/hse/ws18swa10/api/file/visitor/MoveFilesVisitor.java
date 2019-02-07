package de.hse.ws18swa10.api.file.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MoveFilesVisitor extends SimpleFileVisitor<Path>
{
	private final Path sourcePath;
	private final Path destinationPath;
	
	public MoveFilesVisitor(final Path sourcePath, final Path destinationPath)
	{
		super();
		this.sourcePath = sourcePath;
		this.destinationPath = destinationPath;
	}

	@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) 
		throws IOException
	{
		Path currentDestinationPath = getCurrentDestinationPath(file);
		
		if (attr.isDirectory()) {
			Files.createDirectory(currentDestinationPath);
		} else {			
			Files.move(file, currentDestinationPath);
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	private Path getCurrentDestinationPath(final Path file)
	{
		String currentSourceFilePath = file.toAbsolutePath().toString();
		String fullSourcePath = sourcePath.toAbsolutePath().toString();
		String pathRelativeToSource = currentSourceFilePath.substring(fullSourcePath.length());
		String finalDestinationPath = destinationPath + File.separator + pathRelativeToSource;
		
		return Paths.get(finalDestinationPath);
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
		throws IOException
	{		
		Files.delete(dir);
		
		return FileVisitResult.CONTINUE;
	}
}
