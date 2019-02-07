package de.hse.ws18swa10.api.file;

import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.entity.Permission;

public class FileSharePermissionReader
{
	private static final String PERMISSION_CREATE = "CREATE"; 
	private static final String PERMISSION_DELETE = "DELETE";
	private static final String PERMISSION_READ = "READ";
	private static final String PERMISSION_UPDATE = "UPDATE";
	
	public boolean allowsCreationOfFiles(final FileShare fileShare)
	{
		return hasFilePermission(fileShare, PERMISSION_CREATE);
	}
	
	public boolean allowsDeletionOfFiles(final FileShare fileShare)
	{
		return hasFilePermission(fileShare, PERMISSION_DELETE);
	}
	
	public boolean allowsReadingOfFiles(final FileShare fileShare)
	{
		return hasFilePermission(fileShare, PERMISSION_READ);
	}
	
	public boolean allowsUpdateOfFiles(final FileShare fileShare)
	{
		return hasFilePermission(fileShare, PERMISSION_UPDATE);
	}
	
	private boolean hasFilePermission(final FileShare fileShare, String permissionName)
	{
		Permission permission = new Permission();
		permission.setName(permissionName);
		permission.setIsDirectory(false);
		
		return hasPermission(fileShare, permission);
	}
	
	public boolean allowsCreationOfFolders(final FileShare fileShare)
	{
		return hasFolderPermission(fileShare, PERMISSION_CREATE);
	}
	
	public boolean allowsDeletionOfFolders(final FileShare fileShare)
	{
		return hasFolderPermission(fileShare, PERMISSION_DELETE);
	}
	
	public boolean allowsReadingOfFolders(final FileShare fileShare)
	{
		return hasFolderPermission(fileShare, PERMISSION_READ);
	}
	
	public boolean allowsUpdateOfFolders(final FileShare fileShare)
	{
		return hasFolderPermission(fileShare, PERMISSION_UPDATE);
	}
	
	private boolean hasFolderPermission(final FileShare fileShare, String permissionName)
	{
		Permission permission = new Permission();
		permission.setName(permissionName);
		permission.setIsDirectory(true);
		
		return hasPermission(fileShare, permission);
	}
	
	private boolean hasPermission(
		final FileShare fileShare, 
		final Permission permission
	) {
		for (Permission p : fileShare.getPermissions()) {
			if (p.getName().equals(permission.getName()) 
			 && p.getIsDirectory() == permission.getIsDirectory()) {
				return true;
			}
		}
		
		return false;
	}
}
