package backend.test_result;

public class AverageByStudent {
    private int studentId;
    private double average;

    public AverageByStudent(int studentId, double average) {
        this.studentId = studentId;
        this.average = average;
    }

    public int getStudentId() {
        return studentId;
    }

    public double getAverage() {
        return average;
    }
}
