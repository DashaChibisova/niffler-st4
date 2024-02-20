package guru.qa.niffler.db.logging;

import com.jayway.jsonpath.internal.JsonFormatter;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class JsonAllureAppender {

    private final String templateName = "json-query.ftl";
    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

    public void logJson(String name, String json) {
        JsonAttachment attachment = new JsonAttachment(
                name,
                JsonFormatter.prettyPrint(json)
        );
        attachmentProcessor.addAttachment(attachment, new FreemarkerAttachmentRenderer(templateName));
    }
}
