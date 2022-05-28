package nnu.ogms.basins.Entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProjectEntity {
    private String projectName;
    @Value("${file.project-save-path}")
    private String projectSavePath;
}
