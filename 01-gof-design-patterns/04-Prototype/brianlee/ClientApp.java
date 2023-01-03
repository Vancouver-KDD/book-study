package me.whiteship.designpatterns._01_creational_patterns._05_prototype.brian;

import java.util.List;

public class ClientApp {

    public static void main(String[] args) throws CloneNotSupportedException {
        List<OtherData> data3 = List.of(new OtherData("3"), new OtherData("4"), new OtherData("5"));
        HighCupPowerNeedData data = new HighCupPowerNeedData(1, "2", data3);
        HighCupPowerNeedData cloneData = (HighCupPowerNeedData) data.clone();

        System.out.println(data);
        System.out.println(cloneData);

        System.out.println(data != cloneData);
        System.out.println(data.equals(cloneData));
        System.out.println(data.getClass() == cloneData.getClass());
        System.out.println(data.getData3() == cloneData.getData3());
    }
}
