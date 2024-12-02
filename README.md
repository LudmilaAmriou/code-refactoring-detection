# Code Refactoring Detection Using LLMs and RefactoringMiner

## Project Overview

This project aims to detect code refactorings in Java repositories using **RefactoringMiner** and large language models (**LLMs**). The core goal is to identify refactorings between pairs of Java code files and leverage an LLM to analyze these refactorings and provide further insights.

### Key Components:

- **RefactoringMiner**: A Java tool used to detect refactorings between two Java code versions in a Git repository.
- **LLM (QWEncoder)**: A large language model used to analyze and detect refactorings from the code files generated by RefactoringMiner. We use the `qwen2.5-coder` model, known for its fast processing capabilities.
- **Jupyter Notebook**: A Python program that interacts with the LLM to detect refactorings in pairs of Java files, explaining and comparing the refactorings.

## Project Structure

The files required for review and generated based on the project logic are as follows:

```bash
.
├── Cloned_test_repo
├── code_pairs
│   ├── _pair1
│   ├── _pair2
│   └── ...
├── README.md
├── refactoring_pairs.csv
├── src
│   ├── GitFileSearcher.java
│   ├── GitRepositoryUtil.java
│   └── RefactoringCodeExtractor.java
├── task3.ipynb
├── test_BONUSPROMPT
│   ├── model_output_pair14.txt
│   ├── model_output_pair19.txt
│   └── ...
├── test_COMPAREPROMPTS
│   ├── pair16
│   ├── pair2
│   └── ...
├── test_GUIDEDPROMPT
│   ├── model_output_pair10.txt
│   ├── model_output_pair16.txt
│   └── ...
└── test_UNGUIDEDPROMPT
    ├── model_output_pair12.txt
    ├── model_output_pair13.txt
    └── ...
```

## Folder Descriptions:

- **Cloned_test_repo**: Contains the cloned refactored repository through the Java code (not included in Git).

- **code_pairs**: Contains code pair files saved the from RefactoringMiner task.

  - `_pair1`, `_pair2`, ..., `_pair95`.

- **README.md**: Provides an overview of the project, setup instructions, and other essential information.

- **refactoring_pairs.csv**: CSV file listing the refactoring pairs used in the project.

- **src**: Java source code for detecting detect with RefactoringMiner all refactorings in a repository (and its
  commits) and saving the pairs of code files (original and evolved) containing the applied refactorings (Task2).

  - **GitFileSearcher.java**: Utility class for searching and accessing files from a Git repository.
  - **GitRepositoryUtil.java**: Utility class for interacting with Git repositories.
  - **RefactoringCodeExtractor.java**: Extracts refactoring code from repository files.

- **task3.ipynb**: Jupyter notebook for Task 3, containing code, analysis, and results comparison. The notebook uses an LLM's API (QwenCoder2.5-latest) to detect refactorings by prompting it with the saved pairs of code files as input.

- **test_BONUSPROMPT**: Contains files for testing with the BONUS prompt.

  - `model_output_pair14.txt`, `model_output_pair19.txt`, ...

- **test_COMPAREPROMPTS**: Contains test cases for comparing different prompts.

  - `pair16`, `pair2`, ...

- **test_GUIDEDPROMPT**: Contains test cases for the 'GUIDED' prompt.

  - `model_output_pair10.txt`, `model_output_pair16.txt`, ...

- **test_UNGUIDEDPROMPT**: Contains test cases for the 'UNGUIDED' prompt.
  - `model_output_pair12.txt`, `model_output_pair13.txt`, ...

## Description of the Components

### Java Code (RefactoringMiner Integration)

The **Java program** located in the `src/` folder uses **RefactoringMiner** to detect refactorings between Java code files. The program works by analyzing a repository's commit history and identifying refactorings such as:

- Moving methods between classes
- Changing method signatures
- Modifying access levels of methods or attributes
- Adding or removing method calls

Once the refactorings are detected, the program saves pairs of Java code files (original and evolved). The **original** file represents the **parent commit file**, while the **evolved** file corresponds to the **current commit file**, containing the applied refactorings.

### Python Code (LLM Interaction)

The **Python code** in the Jupyter notebook (`task3.ipynb`) interacts with the **QWEncoder LLM** to detect refactorings from the saved pairs of Java code files. We used the **QWEncoder latest model** because of its fast processing abilities and accuracy in analyzing code refactorings and more. It is also open-source!

#### Key Functions in the Jupyter Notebook

##### `execute function`

The `execute` function allows you to perform refactoring analysis on a random selection of code pairs. It is highly flexible, allowing you to specify various parameters such as the number of pairs, the directory path, and the output folder where results will be saved. This function is designed to process and analyze code pairs efficiently.

##### `compare function`

The `compare` function provides the ability to compare multiple code pairs using various prompt templates. It processes each pair and saves the results, making it easy to analyze different refactorings across a range of code pairs. Like the `execute` function, it is adaptable to different configurations, allowing you to adjust the number of pairs processed and the range of pairs selected.

These functions are extremely useful for executing and comparing refactoring analyses in a flexible and configurable manner, enabling efficient processing of Java code pairs with different templates and output settings.

### Prompts Used for LLM Detection

Three different prompts were tested and compared in this project:

1. **Guided Prompt**: A structured approach where specific types of refactorings are identified and explained.
2. **Unguided Prompt**: A more open-ended approach that allows the model to identify refactorings without explicit guidance.
3. **Bonus Prompt**: A documented prompt used for a thorough analysis, focusing on a variety of potential refactorings.

### Justification for Prompt Types

1. **Guided Prompt**:  
   The **Guided Prompt** offers a structured approach where specific types of refactorings are identified and explained. This is useful when we need to direct the model’s attention to particular, well-defined refactoring patterns, such as renaming methods, changing method signatures, or modifying method access levels. By providing clear instructions, we can ensure that the model focuses on specific refactorings, making this approach ideal for targeted analysis of predefined refactoring types.

2. **Unguided Prompt**:  
   The **Unguided Prompt** provides a more open-ended approach, allowing the model to freely identify any refactorings without specific guidance. This prompt type is beneficial when we want to explore all potential refactorings in the code. The model is not constrained by predefined categories, enabling it to identify a broader range of refactorings that may not be immediately apparent or classified under specific patterns.

3. **Bonus Prompt**:  
   The **Bonus Prompt** takes a more detailed and thorough approach, focusing on a variety of potential refactorings, including both high-level and granular changes. It may combine elements from both the guided and unguided prompts, providing a comprehensive analysis of the refactorings present in the codebase. This approach is useful when we want a deeper exploration of the code changes,

### Comparison and Rationale

Each of the three prompts was compared based on **clarity, accuracy, and processing time**. The results of this comparison, along with a detailed explanation of the prompt structures and rationale behind them, are provided in the notebook.

## Project Motivation

The work draws inspiration from the research paper [Automatic Library Migration Using Large Language Models:First Results](https://arxiv.org/pdf/2408.16151) which explores advanced techniques for refactoring detection.

## Requirements

- **Java 11+**: For running the RefactoringMiner-based Java program.
- **Python 3.x**: For running the Jupyter notebook.
- **Jupyter Notebook**: For running the Python code.
- **Ollama**: A tool for interacting with language models locally.

  - To install Ollama, run the following command:
    ```bash
    brew install ollama
    ```
  - Once installed, you can install the **QWEncoder 2.5-Coder-latest** model using the following command:
    ```bash
    ollama pull qwen2.5-coder:latest
    ```
    The following two model variants are available for download:
    - **qwen2.5-coder:latest** (4.7 GB, 7B parameters)
    - **qwen2.5-coder:3b** (1.9 GB, 3B parameters)

- **RefactoringMiner**: For detecting refactorings in Java code.
  - To use the RefactoringMiner API in Java, follow the guide in their [README file, How to use RefactoringMiner as a Maven dependency section](https://github.com/tsantalis/RefactoringMiner/blob/master/README.md#how-to-use-refactoringminer-as-a-maven-dependency).
  - This guide provides the steps to add RefactoringMiner to your Maven or Gradle project, including dependency management and integration into your Java code.

### Hardware Requirements:

- **For running the 7B parameter model (`qwen2.5-coder:latest`)**:

  - Minimum **16 GB of RAM** and a **GPU** with at least **8 GB VRAM** for optimal performance.
  - **CPU** with multiple cores can also be used, but GPU acceleration is highly recommended for better performance.

- **For running the 3B parameter model (`qwen2.5-coder:3b`)**:
  - **8 GB of RAM** should suffice.
  - A **GPU** with at least **4 GB VRAM** is recommended but not mandatory for lower model variants.

### Why Use Ollama and LangChain?

- **Ollama** allows you to run large language models like QWEncoder locally on your machine, without needing an external API service. This provides you with faster processing times and increased control over the model. Using Ollama, you can interact with the models directly, reducing dependency on third-party services and costs associated with cloud-based APIs.

- **LangChain** is a framework that facilitates building applications with LLMs (Language Models) by chaining different tasks and managing data flows between components. It simplifies the integration of LLMs into your Python projects and makes it easier to work with models like QWEncoder for refactoring analysis. LangChain helps you manage the logic of interacting with the models and integrating them into larger workflows.

## Getting Started: Installation and Execution

1. Clone the repository:
   - For HTTPS:
     ```bash
     git clone https://github.com/LudmilaAmriou/code-refactoring-detection.git
     ```
   - For SSH:
     ```bash
     git clone git@github.com:LudmilaAmriou/code-refactoring-detection.git
     ```
2. Run the Java program from the `src/` directory to detect refactorings in your repository. The main Java program is `RefactoringCodeExtractor.java`. You can run it using the following command:

   ```bash
   javac src/RefactoringCodeExtractor.java
   java -cp src RefactoringCodeExtractor
   ```

3. Open the Jupyter notebook (task3.ipynb) from the notebooks/ directory and run the cells you want to execute!
