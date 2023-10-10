package ru.otus.apererushev;

import ru.otus.apererushev.busneslogic.CheckByFortyTwo;
import ru.otus.apererushev.busneslogic.VeryImportantLogic;
import ru.otus.apererushev.loggedlist.StringLoggedList;
import ru.otus.apererushev.proxy.LogProxy;

import java.util.List;

@SuppressWarnings({"unchecked", "java:S106"})
public class Main {
    public static void main(String[] args) {
        var veryImportantLogicLogProxy = new LogProxy<>(VeryImportantLogic.class);

        var calc42 = veryImportantLogicLogProxy.createInstanceWithLodging(CheckByFortyTwo.class);
        System.out.printf("Do we get 42? Answer is %s%n", calc42.isItTrue(1, 2, 39)); // залогируется вызов метода isItTrue

        var listLogProxy = new LogProxy<>(List.class);

        var someList = listLogProxy.createInstanceWithLodging(StringLoggedList.class);
        someList.add("Hello"); // залогируется вызов метода add
        someList.add("world"); // залогируется вызов метода add
        someList.add("?"); // залогируется вызов метода add
        System.out.println(someList);
        someList.remove("?"); // т.к. метод не аннотирован @Log логирования не будет
        someList.add(2, "!"); // залогируется вызов метода add, но с двумя параметрами
        System.out.println(someList);
        someList.clear(); // залогируется вызов метода clear
    }
}