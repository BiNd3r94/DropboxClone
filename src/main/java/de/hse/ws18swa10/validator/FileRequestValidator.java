package de.hse.ws18swa10.validator;

import de.hse.ws18swa10.entity.FileRequest;
import de.hse.ws18swa10.exception.EntityValidationException;

public final class FileRequestValidator extends BaseEntityValidator<FileRequest>
{
	private static final String REQUESTER_KEY = "requester";
	private static final String REQUEST_KEY = "request";
	private static final String TARGET_PATH_KEY = "targetPath";
	
	public void validate(final FileRequest fileRequest) throws EntityValidationException
	{
		addRequiredAttribute(REQUESTER_KEY, fileRequest.getRequester());
		addRequiredAttribute(REQUEST_KEY, fileRequest.getRequest());
		addRequiredAttributeIgnoreString(TARGET_PATH_KEY, fileRequest.getTargetPath());
		
		if (! isMissingAttribute(REQUESTER_KEY)) {
			addRequiredAttribute(REQUESTER_KEY, fileRequest.getRequester().getId());
		}
		
		throwOnMissingOrInvalidAttributes();
	}
}
