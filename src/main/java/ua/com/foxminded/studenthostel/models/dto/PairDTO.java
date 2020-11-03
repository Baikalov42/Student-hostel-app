package ua.com.foxminded.studenthostel.models.dto;

import java.math.BigInteger;

public class PairDTO {

    private BigInteger studentId;
    private BigInteger secondId;

    public BigInteger getStudentId() {
        return studentId;
    }

    public void setStudentId(BigInteger studentId) {
        this.studentId = studentId;
    }

    public BigInteger getSecondId() {
        return secondId;
    }

    public void setSecondId(BigInteger secondId) {
        this.secondId = secondId;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "studentId=" + studentId +
                ", secondId=" + secondId +
                '}';
    }
}
