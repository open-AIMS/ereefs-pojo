package au.gov.aims.ereefs.pojo.utils;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading JSON files.
 */
public class JSONFileReader {

    /**
     * Recursively load JSON files form the specified directory.
     *
     * @param directoryName the root directory to load JSON files from.
     * @return a {@code List} of {@code JSONObject}s.
     * @throws IOException thrown if unable to load a file.
     */
    static public List<JSONObject> loadList(String directoryName) throws IOException {
        return loadList(new File(directoryName));
    }

    /**
     * Recursively load JSON files form the specified directory.
     *
     * @param directory the root directory to load JSON files from.
     * @return a {@code List} of {@code JSONObjects}.
     * @throws IOException thrown if unable to load a file.
     */
    static public List<JSONObject> loadList(File directory) throws IOException {

        List<JSONObject> list = new ArrayList<>();

        if (!directory.exists()) {
            throw new FileNotFoundException("The directory \"" + directory.getPath() +
                "\" does not exist.");
        }
        if (!directory.isDirectory()) {
            throw new FileNotFoundException("\"" + directory.getName() + "\" is not a directory.");
        }

        // Retrieve a list of all files in the directory.
        File[] allFiles = directory.listFiles();
        for (File file : allFiles) {

            // If the current file is a directory, recursively load from that directory also.
            if (file.isDirectory()) {
                list.addAll(loadList(file));
            }

            // If the file ends in '.json', load it.
            if (file.getName().toLowerCase().endsWith(".json")) {
                list.add(loadFileAsJSONObject(file));
            }
        }

        return list;
    }

    /**
     * Load the specified JSON file.
     *
     * @param filename the filename of the JSON file to load.
     * @return a {@code JSONObject} representing the JSON file.
     * @throws IOException thrown if unable to load a file.
     */
    static public JSONObject loadFileAsJSONObject(String filename) throws IOException {
        return loadFileAsJSONObject(new File(filename));
    }

    /**
     * Load the specified JSON file, returning a {@code String} representation.
     *
     * @param filename the filename of the JSON file to load.
     * @return a {@code String} representing the JSON file.
     * @throws IOException thrown if unable to load a file.
     */
    static public String loadFileAsString(String filename) throws IOException {
        return loadFileAsString(new File(filename));
    }

    /**
     * Load the specified JSON file.
     *
     * @param file the reference of the JSON file to load.
     * @return a {@code JSONObject} representing the JSON file.
     * @throws IOException thrown if unable to load a file.
     */
    static public JSONObject loadFileAsJSONObject(File file) throws IOException {
        return new JSONObject(loadFileAsString(file));
    }

    /**
     * Load the specified JSON file.
     *
     * @param file the reference of the JSON file to load.
     * @return a {@code JSONObject} representing the JSON file.
     * @throws IOException thrown if unable to load a file.
     */
    static public String loadFileAsString(File file) throws IOException {
        StringBuffer sb = new StringBuffer();
        try (BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(file)
            )
        )) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

}
