package backend.test_result;

public class AverageBySubject {
    private int subjectId;
    private double average;

    public AverageBySubject(int subjectId, double average) {
        this.subjectId = subjectId;
        this.average = average;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public double getAverage() {
        return average;
    }
}
