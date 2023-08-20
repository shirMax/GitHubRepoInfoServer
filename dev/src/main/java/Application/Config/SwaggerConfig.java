package Application.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
        @Value("${http://localhost:8080}")
        private String devUrl;

        @Bean
        public OpenAPI myOpenAPI() {
            Server devServer = new Server();
            devServer.setUrl(devUrl);
            devServer.setDescription("Server URL in Development environment");

            Info info = new Info()
                    .title("GitHub Fetcher API")
                    .version("1.0")
                    .description("This API retrieves data on github organizations.");

            return new OpenAPI().info(info).servers(List.of(devServer));
        }
}
