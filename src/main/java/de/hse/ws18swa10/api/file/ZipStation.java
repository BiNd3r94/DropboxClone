package de.hse.ws18swa10.api.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipStation
{
	private String rootFolderPath = null;
	
	public void zipFolder(final Path folderPath, final Path targetFilePath) 
		throws IOException
	{
		possiblySetRootFolderPath(folderPath);
		
		File folder = folderPath.toFile();
		File targetFile = targetFilePath.toFile();
		File[] files = folder.listFiles();
		
		FileOutputStream fos = new FileOutputStream(targetFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		for (File file : files) {
			if (file.isDirectory()) {
				zipFolder(file.toPath(), targetFilePath);
			} else {
				String entryPath = getPathRelativeToRoot(file.getAbsolutePath().toString());
				addFileToZip(entryPath, file, zos);
			}
		}
		
		zos.close();
		fos.close();
		
		possiblyResetRootFolderPath(folderPath);
	}
	
	private void possiblySetRootFolderPath(final Path currentFolderPath)
	{
		if (rootFolderPath == null) {
			rootFolderPath = currentFolderPath.toAbsolutePath().toString();
		}
	}
	
	private void addFileToZip(final String entryPath, final File file, final ZipOutputStream zos) 
		throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		
		ZipEntry zipEntry = new ZipEntry(entryPath);
		zos.putNextEntry(zipEntry);

		int length;
		byte[] bytes = new byte[1024];
		
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	private String getPathRelativeToRoot(String path)
	{
		String relativeFilePath = path.replace(rootFolderPath, "");
		
		if (relativeFilePath.startsWith(File.separator)) {
			relativeFilePath = relativeFilePath.substring(File.separator.length());
		}
		
		return relativeFilePath;
	}
	
	private void possiblyResetRootFolderPath(final Path currentFolderPath)
	{
		if (rootFolderPath.equals(currentFolderPath.toAbsolutePath().toString())) {
			rootFolderPath = null;
		}
	}
}
