package de.hse.ws18swa10.api.file;

import de.hse.ws18swa10.api.file.git.DefaultGitClient;
import de.hse.ws18swa10.api.file.git.GitFileHistoryRepository;
import de.hse.ws18swa10.cli.WindowsConsole;

public class HistoryRepositoryFactory
{
	public static FileHistoryRepositoryInterface createDefaultHistoryRepository()
	{
		return new GitFileHistoryRepository(
			new DefaultGitClient(new WindowsConsole()),
			PathProviderFactory.createDefaultPathProvider()
		);
	}
}
