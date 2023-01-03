package me.whiteship.designpatterns._01_creational_patterns._05_prototype.brian;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HighCupPowerNeedData implements Cloneable {

    private final int data1;
    private final String data2;
    private final List<OtherData> data3;

    public List<OtherData> getData3() {
        return data3;
    }

    public HighCupPowerNeedData(int data1, String data2, List<OtherData> data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data1, data2, data3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighCupPowerNeedData that = (HighCupPowerNeedData) o;
        return data1 == that.data1
                && Objects.equals(data2, that.data2)
                && Objects.equals(data3, that.data3);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // super.clone() method performs a "shallow copy" of this object
        // , not a "deep copy" operation.
//         return super.clone();

        // This is performs a "deep copy" operation
        List<OtherData> data3 = new ArrayList<>();
        for(OtherData otherData : this.data3) {
            data3.add(new OtherData(otherData.getData()));
        }
        return new HighCupPowerNeedData(data1, data2, data3);
    }

    @Override
    public String toString() {
        return "HighCupPowerNeedData{" +
                "data1=" + data1 +
                ", data2='" + data2 + '\'' +
                ", data3=" + data3 +
                '}';
    }
}
