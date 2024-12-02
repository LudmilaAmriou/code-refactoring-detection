import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;


/**
 * Utility class for searching and accessing files from a Git repository using JGit.
 */
public class GitFileSearcher {

    /**
     * Searches for a file in a specified commit and retrieves its content.
     *
     * @param repository The cloned Git repository.
     * @param filePath The relative path of the file within the repository.
     * @param commitHash The hash of the commit where the file is searched.
     * @return The content of the file as a String.
     * @throws IOException If the file or commit is not found, or an error occurs.
     */
    public static String searchAndAccessFile(Repository repository, String filePath, String commitHash) throws IOException {
        try (Git git = new Git(repository)) {
            RevWalk revWalk = new RevWalk(repository);

            // Resolve the commit ID
            ObjectId commitId = repository.resolve(commitHash);
            if (commitId == null) {
                throw new IOException("Commit not found: " + commitHash);
            }

            // Parse the commit
            RevCommit commit = revWalk.parseCommit(commitId);

            // Create a TreeWalk to traverse the file tree of the specified commit.
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(commit.getTree()); // Add the tree of the commit to the TreeWalk.
            treeWalk.setRecursive(true); // Enable recursive traversal.
            treeWalk.setFilter(PathFilter.create(filePath)); // Set a filter to search for the specific file.


            // Search for the file in the tree
            if (!treeWalk.next()) {
                throw new IOException("File not found: " + filePath);
            }

            // Read and return the content of the file as a String.
            return new String(repository.open(treeWalk.getObjectId(0)).getBytes());
        }
    }

    /**
     * Retrieves the parent commit hash of a given commit.
     *
     * @param repository The Git repository.
     * @param commitHash The hash of the commit for which the parent is needed.
     * @return The hash of the parent commit.
     * @throws IOException If the commit or parent commit is not found, or an error occurs.
     */
    public static String getParentCommitHash(Repository repository, String commitHash) throws IOException {
        RevWalk revWalk = new RevWalk(repository);

        // Resolve the commit ID
        ObjectId commitId = repository.resolve(commitHash);
        if (commitId == null) {
            throw new IOException("Commit not found: " + commitHash);
        }

        // Parse the commit
        RevCommit commit = revWalk.parseCommit(commitId);

        // Get the parent commit
        RevCommit parentCommit = commit.getParent(0); // Get the first parent
        if (parentCommit == null) {
            throw new IOException("No parent commit found for: " + commitHash);
        }

        // Return the parent commit's hash
        return parentCommit.getName();
    }

    // Test the Class
    public static void main(String[] args) {
        try {
            // Correct repository path
            Repository repository = GitRepositoryUtil.openRepository("Cloned_test_repo/refactoring-toy-example");

            // Relative file path within the repository

            String fileContent = searchAndAccessFile(repository, "src/org/animals/Dog.java","d4bce13a443cf12da40a77c16c1e591f4f985b47");
            System.out.println(fileContent);
            System.out.println("----------------------------------------------------");
            String fileContent2 = searchAndAccessFile(repository, "src/org/animals/Dog.java",getParentCommitHash(repository,"d4bce13a443cf12da40a77c16c1e591f4f985b47"));
            System.out.println(fileContent2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}