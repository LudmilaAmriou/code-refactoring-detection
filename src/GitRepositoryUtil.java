import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for opening and accessing Git repositories using JGit.
 */
public class GitRepositoryUtil {

    /**
     * Opens a Git repository located at the specified path.
     *
     * @param repoPath The file system path to the Git repository (without the `.git` suffix).
     * @return An instance of the JGit `Repository` object representing the Git repository.
     * @throws IOException If the repository cannot be found or opened.
     */
    public static Repository openRepository(String repoPath) throws IOException {
        // Create a FileRepositoryBuilder to configure and open the repository.
        FileRepositoryBuilder builder = new FileRepositoryBuilder();

        // Configure the builder:
        // 1. Specify the path to the `.git` directory of the repository.
        // 2. Read environment variables to support default configurations.
        // 3. Automatically find the `.git` directory if the path isn't explicitly provided.
        return builder.setGitDir(new File(repoPath + "/.git")) // Specify the `.git` directory.
                .readEnvironment() // Read environment variables for additional configuration.
                .findGitDir() // Search for the `.git` directory if not explicitly set.
                .build(); // Build and return the repository instance.
    }
}
