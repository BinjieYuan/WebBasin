package nnu.ogms.basins.Controller;

import nnu.ogms.basins.Service.ProjectService;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @PostMapping("/create")
    public Object createProject(@RequestParam("newProjectName")String projectName){
        if (projectName.isEmpty()){
            return new ResponseMessage(ErrorEnum.NEW_PROJECT_NAME_EMPTY.getHttpCode(), ErrorEnum.NEW_PROJECT_NAME_EMPTY.getMessage(), ErrorEnum.NEW_PROJECT_NAME_EMPTY.getCode(), null);
        }
        return projectService.namedProject(projectName);
    }
}
