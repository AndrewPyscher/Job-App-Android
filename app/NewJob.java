

public class NewJob {

    int employer_id, salary;
    String job_title, description, type;
    boolean active;

    public NewJob(int employer_id, String job_title, String description, int salary, boolean active, String type) {
        this.employer_id = employer_id;
        this.job_title = job_title;
        this.description = description;
        this.salary = salary;
        this.active = active;
        this.type = type;
    }

}

