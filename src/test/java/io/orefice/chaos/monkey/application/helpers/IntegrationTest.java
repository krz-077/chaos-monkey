package io.orefice.chaos.monkey.application.helpers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest(
        properties = {
                "destroyer.configuration.namespace=aCronNamespace",
                "destroyer.configuration.label=aCronLabel"
        })
public abstract class IntegrationTest {

}
