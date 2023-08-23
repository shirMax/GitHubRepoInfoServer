package Application.Controllers;

import Application.Records.RepositoryInfo;
import Application.Service.GitHubFetcher;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class RepositoryInfoController {
    private GitHubFetcher gitHubFetcher;
    public RepositoryInfoController() {
        gitHubFetcher = new GitHubFetcher();
    }

    /**
     * Retrieves a list of repositories for the specified organization.
     *
     * @param organizationName The name of the organization to retrieve repositories for.
     * @param phrase           An optional search phrase to filter repositories by name.
     * @param page             An optional page number for pagination of results.
     * @param perPage          An optional parameter to specify the number of repositories to display per page.
     * @param httpSession      The HTTP session to associate with the request.
     * @return A list of {@link RepositoryInfo} objects representing the repositories.
     */
    @Operation(summary = "retrieve organization repositories", description = "Retrieves a list of repositories for the specified organization. This endpoint allows you to filter repositories by organization name and an optional search phrase, as well as paginate the results using the 'page' and 'per_page' parameters." +
            " Organization name must be provided, and if a search phrase is given, it must be at least 3 characters long." +
            " Pagination is optional, where 'page' specifies the page number of results, and 'per_page' indicates the number of repositories to display per page.")
    @GetMapping("/repositories")
    public List<RepositoryInfo> repositoryInfo(@RequestParam(value = "organizationName") String organizationName,
                                               @RequestParam(value = "phrase", required = false) @Size(min = 3) String phrase,
                                               @RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "per_page", required = false) Integer perPage,
                                               HttpSession httpSession) {


        String sessionId = httpSession.getId();
        return gitHubFetcher.repositoryInfo(organizationName, phrase, sessionId, page, perPage);
    }

    public void setGitHubFetcher(GitHubFetcher gitHubFetcher) {
        this.gitHubFetcher = gitHubFetcher;
    }
    }

