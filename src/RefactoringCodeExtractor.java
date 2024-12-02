import gr.uom.java.xmi.diff.CodeRange;

import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.util.GitServiceImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * Main Class to Run !
 * Extracts code snippets affected by refactorings from a Git repository and saves them as paired files.
 * Uses RefactoringMiner and JGit libraries for Git analysis and refactoring detection.
 */
public class RefactoringCodeExtractor {

    public static void main(String[] args) {

        // Path to the local Git repository (clone location) and output folder for code pairs
        String localRepoPath = "Cloned_test_repo/refactoring-toy-example";
        String outputPath = "./code_pairs/";

        try {
            // Open the local Git repository
            Repository repository = GitRepositoryUtil.openRepository(localRepoPath);

            // Initialize Git service and refactoring miner
            GitService gitService = new GitServiceImpl();
            GitHistoryRefactoringMinerImpl miner = new GitHistoryRefactoringMinerImpl();

            // Clone the repository if it doesn't already exist locally
            Repository repo = gitService.cloneIfNotExists(
                    localRepoPath,
                    "https://github.com/danilofes/refactoring-toy-example");

            // Counter for naming code pairs to ensure unique names for the folders
            final int[] counter = {1};

            // Analyze the repository for refactorings in the Master branch
            miner.detectAll(repo, "master", new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
                    System.out.println("Processing commit: " + commitId);

                    // Process each detected refactoring
                    for (Refactoring ref : refactorings) {
                        System.out.println("Detected refactoring: " + ref.getName());

                        // Extract left (parent) and right (child) code ranges
                        List<CodeRange> leftSide = ref.leftSide();
                        List<CodeRange> rightSide = ref.rightSide();

                        for (int i = 0; i < Math.min(leftSide.size(), rightSide.size()); i++) {

                            // Extract the file paths from both sides of the refactoring
                            CodeRange left = leftSide.get(i);
                            CodeRange right = rightSide.get(i);

                            // Fetch content from the parent and current commit
                            try {

                                String leftContent = GitFileSearcher.searchAndAccessFile(repository, left.getFilePath(), GitFileSearcher.getParentCommitHash(repository,commitId)); //Parent Commit
                                String rightContent = GitFileSearcher.searchAndAccessFile(repository, right.getFilePath(), commitId);

                                // Save the code pair to the output directory
                                saveCodePair(outputPath, left, right, leftContent, rightContent, counter[0]);
                                counter[0]++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Extracts content from a specified line range in a file - Not used but might be needed in future tasks
     *
     * @param content   The file's complete content.
     * @param startLine The starting line number (1-based).
     * @param endLine   The ending line number (1-based).
     * @return The content within the specified line range.
     */
    private static String getContentInRange(String content, int startLine, int endLine) {
        String[] lines = content.split("\n");
        StringBuilder filteredContent = new StringBuilder();
        // Extract lines from startLine to endLine
        for (int i = startLine - 1; i <= endLine - 1 && i < lines.length; i++) {
            filteredContent.append(lines[i]).append("\n");
        }

        return filteredContent.toString();
    }


    /**
     * Saves the original and evolved code as separate files in a specified directory.
     *
     * @param outputPath    The base directory for saving code pairs.
     * @param left          The code range from the parent commit.
     * @param right         The code range from the current commit.
     * @param leftContent   The content from the parent commit.
     * @param rightContent  The content from the current commit.
     * @param Counter       A unique identifier for the code pair.
     * @throws IOException If an error occurs while saving the files.
     */
    private static void saveCodePair(String outputPath, CodeRange left, CodeRange right, String leftContent, String rightContent, int Counter) throws IOException {
        // Create a unique subfolder name based on the file path
        String pairFolderName = outputPath + "_pair"+Counter+"/";

        // Ensure the subfolder exists
        Files.createDirectories(Paths.get(pairFolderName));

        // Generate filenames for the original and evolved code
        String leftFileName = pairFolderName + "original_" + Counter +"_"+left.getFilePath().replace("/", "_").replace(":", "_");
        String rightFileName = pairFolderName + "evolved_" +Counter +"_"+right.getFilePath().replace("/", "_").replace(":", "_");

        // Write the file contents to disk
        Files.write(Paths.get(leftFileName), leftContent.getBytes());
        Files.write(Paths.get(rightFileName), rightContent.getBytes());

        System.out.println("Saved code pair in: " + pairFolderName);
    }

}
