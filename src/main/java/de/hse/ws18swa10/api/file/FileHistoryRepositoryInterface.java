package de.hse.ws18swa10.api.file;

import java.util.List;

import de.hse.ws18swa10.entity.User;

public interface FileHistoryRepositoryInterface
{
	public List<String> getChanges(final User user, String path) throws Exception;
}
