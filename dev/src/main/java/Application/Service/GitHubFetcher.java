package Application.Service;

import Application.Records.RepositoryInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GitHubFetcher {
    private static final Logger logger = Logger.getLogger(GitHubFetcher.class.getName());
    private Map<String, Integer> sessionIdToLastPage = new HashMap<>();
    private final String GITHUB_API_GET_REPOSITORIES = "https://api.github.com/orgs/%s/repos";
    public GitHubFetcher() {
        try {
            FileHandler fh = new FileHandler("log.txt", true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            logger.addHandler(fh);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RepositoryInfo> repositoryInfo(String organizationName, String phrase, String sessionId, Integer page, Integer perPage) {
        logger.info("Entering RepositoryInfo method... params: organizationName="+organizationName+" phrase="+phrase+" page="+page+ " sessionId="+sessionId+ " perPage="+perPage);

        if(phrase != null && phrase.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phrase length must be at least 3 characters.");
        }
        JSONArray repositories = fetchRepositories(organizationName, sessionId, page, perPage);

        List<RepositoryInfo> repositoryInfos = filterRepositoriesByRepoName(repositories, phrase);
        logger.info("Exiting RepositoryInfo method... params: organizationName="+organizationName+" phrase="+phrase+" page="+page+ " sessionId="+sessionId+" per page="+perPage);

        return repositoryInfos;
    }

    private List<RepositoryInfo> filterRepositoriesByRepoName(JSONArray repositories, String phrase) {
        List<RepositoryInfo> repositoryInfoList = new ArrayList<>();

        for (int i = 0; i < repositories.length(); i++) {
            JSONObject repoJson = repositories.getJSONObject(i);
            String repoName = repoJson.getString("name");

            logger.info("Processing repository #" + (i + 1) +" name="+repoName);

            if (phrase == null || repoName.toLowerCase().contains(phrase.toLowerCase())) {
                int ownerId = repoJson.getJSONObject("owner").getInt("id");
                String url = repoJson.getString("url");
                String creationTime = repoJson.getString("created_at");
                int starsCount = repoJson.getInt("stargazers_count");

                RepositoryInfo repositoryInfo = new RepositoryInfo(repoName, ownerId, url, creationTime, starsCount);
                repositoryInfoList.add(repositoryInfo);
            }
        }
        return repositoryInfoList;
    }

    private JSONArray fetchRepositories(String organizationName, String sessionId, Integer page, Integer perPage) {
        String url = getNextPageUrl(organizationName, sessionId, page, perPage);
        try {
            HttpURLConnection connection = getConnectionWithGitHub(url);
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return new JSONArray(response.toString());
            } else {
                logger.severe("Server returned status code: " + statusCode);
                JSONArray emptyArray = new JSONArray();
                return emptyArray;
            }
        }
        catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public HttpURLConnection getConnectionWithGitHub(String url) throws IOException {
        URL gitHubUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) gitHubUrl.openConnection();
        return connection;
    }

    private String getNextPageUrl(String organizationName, String sessionId, Integer page, Integer perPage) {
        sessionIdToLastPage.putIfAbsent(sessionId, 1);
        int lastPage;

        if(page == null) {
            lastPage = sessionIdToLastPage.get(sessionId);
        } else {
            lastPage = page;
        }

        if(perPage == null) {
            //default value 30
            perPage = 30;
        }

        String url = String.format(GITHUB_API_GET_REPOSITORIES, organizationName) + "?page=" + lastPage+ "&per_page="+perPage;
        sessionIdToLastPage.put(sessionId, sessionIdToLastPage.get(sessionId) + 1);
        return url;
    }
}
