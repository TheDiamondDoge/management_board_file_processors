package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        for (int i = 0; i <= 20; i++) {
            String query = "INSERT INTO `test`.`prj_milestones` (project_id, milestone_label, baseline_date, actual_date, `completion`, url, show_in_timeline)\n" +
                    "SELECT a.ProjectID, ProjectMilestoneLabel"+ i +", ProjectMilestoneBaseline"+ i +", ProjectMilestoneActual"+ i +", IF(ProjectMilestoneCompletion"+ i +" is null, 0, ProjectMilestoneCompletion"+ i +"), b.ProjectMilestoneURL"+ i +", \n" +
                    "(SELECT IF(a.Timeline REGEXP ',"+ i +"|^"+ i +"$|"+ i +",' IS NULL, 0, a.Timeline REGEXP ',"+ i +"|^"+ i +"$|"+ i +",'))\n" +
                    "FROM `dm_prj`.`prj_workspace` as a, `dm_prj`.`prj_workspace_local` as b\n" +
                    "WHERE a.ProjectID = b.ProjectID\n" +
                    "AND a.ProjectMilestoneLabel"+ i +" IS NOT NULL ON DUPLICATE KEY UPDATE project_id = project_id;";

            System.out.println(query);
        }
    }
}
