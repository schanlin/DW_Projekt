package backend.test_result;

public class Average {
    private int studentID;
    private int fachID;
    private double average;

    public Average(int studentID, int fachID, double average) {
        this.studentID = studentID;
        this.fachID = fachID;
        this.average = average;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getFachID() {
        return fachID;
    }

    public double getAverage() {
        return average;
    }
}
