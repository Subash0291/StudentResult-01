public class Student {
    private int id;
    private String name;
    private String email;
    private int total;
    private double average;
    private String grade;
    private String status;

    public Student(int id, String name, String email, int total) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.total = total;
        this.average = total / 5.0;
        this.grade = calculateGrade(this.average);
        this.status = this.average >= 50 ? "Pass" : "Fail";
    }

    private String calculateGrade(double avg) {
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F";
    }

    public int getId()        { return id; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }
    public int getTotal()     { return total; }
    public double getAverage(){ return average; }
    public String getGrade()  { return grade; }
    public String getStatus() { return status; }
}
