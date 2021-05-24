package au.gov.aims.ereefs.pojo.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for files.
 */
public class FileUtils {

    /**
     * Invoke {@link #listFiles(File)} with the specified {@code path}.
     */
    static public List<String> listFiles(String path) {
        return listFiles(new File(path), null);
    }

    /**
     * Invoke {@link #listFiles(File, String)} with the specified {@code path} and a {@code null}
     * search string.
     */
    static public List<String> listFiles(File dir) {
        return listFiles(dir, null);
    }

    /**
     * Invoke {@link #listFiles(File, String)} with the specified {@code path} and search string.
     */
    static public List<String> listFiles(String path, String filenameSuffixSearch) {
        return listFiles(new File(path), filenameSuffixSearch);
    }

    /**
     * Recursively build a list of all files under the specified directory.
     *
     * @param dir the directory in which to search for files.
     * @param filenameSuffixSearch if specified, the search String is applied to all files before
     *                             being added to the result list.
     * @return a list of Strings containing for path/filenames for all files beneath the specified
     * directory.
     */
    static public List<String> listFiles(File dir,
                                         String filenameSuffixSearch) {

        // Declare the result.
        final List<String> filenames = new ArrayList<>();

        // Loop through all 'files' below the current directory.
        final File[] files = dir.listFiles();
        for (File file : files) {

            // If the 'file' is a directory, recursively call this method and add the results to
            // the current list.
            if (file.isDirectory()) {
                filenames.addAll(listFiles(file, filenameSuffixSearch));
            } else {

                // This is a file. Has a filename suffix search string been specified? If so, apply
                // it before the file can be added to the result list.
                String filename = file.getAbsolutePath();
                if (filenameSuffixSearch != null) {
                    if (filename.endsWith(filenameSuffixSearch)) {
                        filenames.add(filename);
                    }
                } else {
                    filenames.add(filename);
                }
            }
        }
        return filenames;
    }

}
