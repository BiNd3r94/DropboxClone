package de.hse.ws18swa10.api.file.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hse.ws18swa10.api.file.FileHistoryRepositoryInterface;
import de.hse.ws18swa10.api.file.PathProviderInterface;
import de.hse.ws18swa10.entity.User;

public class GitFileHistoryRepository implements FileHistoryRepositoryInterface
{
	private final GitClientInterface gitClient;
	private final PathProviderInterface pathProvider;

	public GitFileHistoryRepository(
		final GitClientInterface gitClient,
		final PathProviderInterface pathProvider
	) {
		this.gitClient = gitClient;
		this.pathProvider = pathProvider;
	}
	
	@Override
	public List<String> getChanges(final User user, String path) throws Exception
	{
		String projectDir = pathProvider.getRootPath() + File.separator + user.getFileSpaceName();
		
		gitClient.setProjectDir(projectDir);
		
		List<String> changes = new ArrayList<>();
		List<String> commits = gitClient.getCommits(path);
		
		for (String commit : commits) {
			changes.add(extractCommitMessage(commit));
		}
		
		return changes;
	}
	
	private String extractCommitMessage(String commit)
	{
		return commit.substring(commit.indexOf(" "));
	}
}
