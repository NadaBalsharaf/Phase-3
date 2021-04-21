/*
* Project Phase 3 of CPCS 324 
* section: DAR
* Group NO. : 3
* Members:
* Lameer Shamsaldeen - 1806835
* Nada Balsharaf - 1807769
* Mariam Mahdi - 1825889
* instryctor: Dr.Bassma Alsulami
*code taken and modified from https://algorithms.tutorialhorizon.com/maximum-bipartite-matching-problem/
 */
package bipartiteMatch;

/**
 *
 * @author Lamee
 */
public class BipartiteMatchAlgorithm {

    ////class graph
    static class Graph {

        // define total number of jobs, applications. adjmatrix and TotalApp for all applications and jobs
        int jobs;
        int applicants;
        int adjMatrix[][];
        int TotalApp[][];

        // constructure
        public Graph(int applicants, int jobs) {
            this.jobs = jobs;
            this.applicants = applicants;
            adjMatrix = new int[applicants][jobs];
            TotalApp = new int[jobs][jobs];
        }

        // to fill matrices with edges between applications and jobs
        public void canDoJob(int applicant, int job) {
            //add edge - means applicant can do this job
            adjMatrix[applicant][job] = 1;
            //matrix to calculate the available applications in each job
            TotalApp[applicant][job] = 1;
        }

        // print available applications for each hospital
        public void printTotalAppl() {
            // names of hospitals
            String job[] = {"King Abdelaziz University", "King Fahad", "East Jeddah", "King fahad Armed Forced", "King Faisal Specialist", "Ministry Of national Guard"};
            // define rows and columns of the matrix(TotalApp) that contains all applications and jobs
            int rows = TotalApp.length;
            int colmn = TotalApp[0].length;
            // define sumcol to sum number of applications in each hospital
            int sumCol;
            // calculate sum of each column
            for (int i = 0; i < colmn; i++) {
                sumCol = 0;
                for (int j = 0; j < rows; j++) {
                    sumCol = sumCol + TotalApp[j][i];
                }
                System.out.println("hospital " + job[i] + " = " + sumCol);
            }
        }
    }

    /**
     * method to calculate matching edges
     * @param graph
     * @return  
     */
    public static int maxMatching(Graph graph) {
        int applicants = graph.applicants;
        int jobs = graph.jobs;
        //an array to track which job is assigned to which applicant
        int assign[] = new int[jobs];
        for (int i = 0; i < jobs; i++) {
            assign[i] = -1;    //initially set all jobs are available
        }
        //count the occupaid jobs
        int jobCount = 0;
        //for all applicants
        for (int applicant = 0; applicant < applicants; applicant++) {
            //for each applicant, all jobs will be not visited initially
            boolean visited[] = new boolean[jobs];
            //check if applicant can get a job
            System.out.println("maximum number of application = " + jobCount);
            if (bipartiteMatch(graph, applicant, visited, assign)) {
                //if yes then increase the job count  
                jobCount++;
            }
        }
        return jobCount;
    }

    /**
     * method to check a job with an applicant
     * @param graph
     * @param applicant
     * @param visited
     * @param assign
     * @return
     */
    public static boolean bipartiteMatch(Graph graph, int applicant, boolean visited[], int assign[]) {
       //check each job for the applicant
        for (int job = 0; job < graph.jobs; job++) {
            //check if applicant can do this job means adjMatrix[applicant][job] == 1
            // and applicant has not considered for this job before, means visited[job]==false
            if (graph.adjMatrix[applicant][job] == 1 && !visited[job]) {
                //mark as job is visited, means applicant is considered for this job
                visited[job] = true;
                //now check if job was not assigned earlier - assign it to this applicant
                // OR job was assigned earlier to some other applicant 'X' earlier then
                //make recursive call for applicant 'X' to check if some other job can be assigned
                // so that this job can be assigned to current candidate.
                int assignedApplicant = assign[job];
                if (assignedApplicant < 0 || bipartiteMatch(graph, assignedApplicant, visited, assign)) {
                    assign[job] = applicant;    //assign job job to applicant applicant
                    System.out.println(applicant+ " --> "+job);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        //create graph with giving number of applications and jobs
        int applicants = 6, jobs = 6;
        Graph graph = new Graph(applicants, jobs);
        //enter edges between applications and jobs
        graph.canDoJob(0, 0);
        graph.canDoJob(0, 1);
        graph.canDoJob(1, 5);
        graph.canDoJob(2, 0);
        graph.canDoJob(2, 3);
        graph.canDoJob(3, 2);
        graph.canDoJob(4, 3);
        graph.canDoJob(4, 4);
        graph.canDoJob(5, 5);
        // print the available applications in each job
        System.out.println("Maximum number of available applications in :");
        graph.printTotalAppl();
        // solution start
        System.out.println("-------------------------------------------------------------------"
                + "\nSolution: ");
        System.out.println("-------------------------------------------------------------------"
                + "\nMaximum bipartite matching = " + maxMatching(graph));
    }

}
