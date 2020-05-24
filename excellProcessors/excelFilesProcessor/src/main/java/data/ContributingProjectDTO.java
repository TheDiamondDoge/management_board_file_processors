package data;

import util.ProjectStates;

import java.util.List;

public class ContributingProjectDTO {
    private int projectId;
    private String projectName;
    private ProjectStates projectState;
    private String projectType;
    private MilestoneDTO lastApproved;
    private List<MilestoneDTO> milestones;

    public ContributingProjectDTO() {
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ProjectStates getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectStates projectState) {
        this.projectState = projectState;
    }

    public MilestoneDTO getLastApproved() {
        return lastApproved;
    }

    public void setLastApproved(MilestoneDTO lastApproved) {
        this.lastApproved = lastApproved;
    }

    public List<MilestoneDTO> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneDTO> milestones) {
        this.milestones = milestones;
    }
}
