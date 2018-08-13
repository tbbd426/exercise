package tianbing.exercise.java8.collector;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author tianbing
 * DATE: 2018/8/13
 */
public class MyListCollectorTest {

    @Test
    public void testMyToListInteger() {
        Stream<Integer> numbers = Stream.of(1,2,3,4,5,7);
        List<Integer> results = numbers.collect(new MyListCollector<>());
        Assert.assertEquals(6, results.size());
        Integer[] expected = {1,2,3,4,5,7 };
        Assert.assertArrayEquals(expected , results.toArray());
    }

    @Test
    public void testMyToListString() {
        Stream<String> strings = Stream.of("hello", "world");
        List<String>  result = strings.collect(new MyListCollector<>());;
        String[] expected = {"hello", "world"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testIsConcurrent() {
        Stream<Integer> numbers = Stream.of(1,2,3,4,5,7);
        List<Integer> results = numbers.parallel().collect(new MyListCollector<>(false));
        Assert.assertEquals(6, results.size());
        Integer[] expected = {1,2,3,4,5,7 };
        Assert.assertArrayEquals(expected , results.toArray());
    }
}
