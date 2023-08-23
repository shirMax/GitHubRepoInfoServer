package UnitTests;

import Application.Records.RepositoryInfo;
import Application.Service.GitHubFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitHubFetcherTest {

    private GitHubFetcher gitHubFetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gitHubFetcher = new GitHubFetcher();
    }


    @Test
    void testRepositoryInfo_withInvalidPhrase() {
        assertThrows(ResponseStatusException.class, () -> {
            gitHubFetcher.repositoryInfo("exampleOrg", "ab", "session123", 1, null);
        });
    }

    @Test
    void testRepositoryInfo_withNegativePage() {
        List<RepositoryInfo> result = gitHubFetcher.repositoryInfo("exampleOrg", null, "session123", -1, null);

        assertEquals(0, result.size());
    }

    @Test
    void testRepositoryInfo_withPage() {
        List<RepositoryInfo> result = gitHubFetcher.repositoryInfo("exampleOrg", null, "session123", -1, null);

        assertEquals(0, result.size());
    }

    @Test
    void testRepositoryInfo_withNegativePerPage() {
        List<RepositoryInfo> result = gitHubFetcher.repositoryInfo("exampleOrg", null, "session123", 1, -1);

        assertEquals(0, result.size());
    }

    @Test
    void testRepositoryInfo_withPerPageOne() {
        List<RepositoryInfo> result = gitHubFetcher.repositoryInfo("adobe", null, "session123", 1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void testRepositoryInfo_differentSessions_PaginationSuccess() {
        String sessionId1 = "sessionId1";
        String sessionId2 = "sessionId2";

        List<RepositoryInfo> result1 = gitHubFetcher.repositoryInfo("adobe", null, sessionId1, 1, 1);
        List<RepositoryInfo> result2 = gitHubFetcher.repositoryInfo("adobe", null, sessionId2, 1, 1);

        assertEquals(result1, result2, "Contents of the lists are not equal");
    }

    @Test
    void testRepositoryInfo_IdenticalSessions_PaginationSuccess() {
        String sessionId1 = "sessionId1";

        List<RepositoryInfo> result1 = gitHubFetcher.repositoryInfo("adobe", null, sessionId1, null, 1);
        List<RepositoryInfo> result2 = gitHubFetcher.repositoryInfo("adobe", null, sessionId1, null, 1);

        assertNotEquals(result1, result2, "Contents of the lists are equal");
    }

}
