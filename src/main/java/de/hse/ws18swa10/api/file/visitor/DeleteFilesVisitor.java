package de.hse.ws18swa10.api.file.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteFilesVisitor extends SimpleFileVisitor<Path>
{
	@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) 
		throws IOException
	{
		if (! attr.isDirectory()) {
			Files.delete(file);
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
		throws IOException
	{
		Files.delete(dir);
		
		return FileVisitResult.CONTINUE;
	}
}
