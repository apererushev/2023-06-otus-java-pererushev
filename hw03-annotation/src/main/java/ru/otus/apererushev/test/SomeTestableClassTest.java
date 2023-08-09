package ru.otus.apererushev.test;

import org.apache.commons.lang3.NotImplementedException;
import org.testng.Assert;
import ru.otus.apererushev.biseneslogic.SomeTestableClass;
import ru.otus.apererushev.biseneslogic.TestableException;
import ru.otus.apererushev.testengine.*;

import java.math.BigDecimal;

public class SomeTestableClassTest {

    @Before
    public void before() {
        System.out.printf("Before [%s]\n", this.toString());
    }

    @After
    public void someRandomMethodName() {
        System.out.printf("After [%s]\n", this.toString());
    }

    @Test
    @Description(value = "Testing method doSum()")
    public void doSumTest() {
        final var testable = new SomeTestableClass();

        final BigDecimal expected = new BigDecimal("10.1");
        final var result = testable.doSum(new BigDecimal("10"), new BigDecimal("0.1"));

        Assert.assertEquals(result, expected);
    }

    @Test
    public void doExceptionTest() {
        final var testable = new SomeTestableClass();

        Assert.expectThrows(TestableException.class, testable::doException);
    }

    @Test
    @Ignore
    public void doNothingTest() {
        final var testable = new SomeTestableClass();

        testable.doNothing();
    }

    public void methodWithoutAnnotation() {
        throw new NotImplementedException();
    }

    @Test
    public void doErrorTest() {
        Assert.assertTrue(false);
    }
}
