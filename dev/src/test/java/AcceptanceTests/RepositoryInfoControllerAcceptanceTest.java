package AcceptanceTests;

import Application.Controllers.RepositoryInfoController;
import Application.Records.RepositoryInfo;
import Application.Service.GitHubFetcher;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RepositoryInfoControllerAcceptanceTest {

    @Mock
    private GitHubFetcher mockGitHubFetcher;

    @Mock
    private HttpSession mockHttpSession;

    @Test
    void testRepositoryInfoController() {
        MockitoAnnotations.openMocks(this);

        RepositoryInfoController repositoryInfoController = new RepositoryInfoController();
        repositoryInfoController.setGitHubFetcher(mockGitHubFetcher);

        String organizationName = "exampleOrg";
        String phrase = "repo";
        String sessionId = "session123";
        Integer page = 1;

        List<RepositoryInfo> repositoryInfoList = Collections.singletonList(new RepositoryInfo("repo1", 1, "url", "creationTime", 5));

        when(mockHttpSession.getId()).thenReturn(sessionId);
        when(mockGitHubFetcher.repositoryInfo(organizationName, phrase, sessionId, page, null)).thenReturn(repositoryInfoList);

        List<RepositoryInfo> responseList = repositoryInfoController.repositoryInfo(organizationName, phrase, page, null, mockHttpSession);

        assertEquals(repositoryInfoList, responseList);

        verify(mockGitHubFetcher, times(1)).repositoryInfo(organizationName, phrase, sessionId, page, null);
    }
}
