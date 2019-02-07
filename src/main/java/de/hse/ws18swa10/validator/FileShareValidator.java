package de.hse.ws18swa10.validator;

import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.exception.EntityValidationException;

public final class FileShareValidator extends BaseEntityValidator<FileShare>
{
	private static final String OWNER_KEY = "owner";
	private static final String PATH_KEY = "path";
	
	public void validate(FileShare fileShare) throws EntityValidationException
	{
		addRequiredAttribute(OWNER_KEY, fileShare.getOwner());
		addRequiredAttribute(PATH_KEY, fileShare.getPath());
		
		if (! isMissingAttribute(OWNER_KEY)) {
			addRequiredAttribute(OWNER_KEY, fileShare.getOwner().getId());
		}

		throwOnMissingOrInvalidAttributes();
	}
}
