package tianbing.exercise.java8.collector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author tianbing
 * DATE: 2018/8/13
 *
 * Collector<T, A, R>
 * T: 输入元素的类型
 * A: 计算的中间结果类型
 * R: 收集器结果类型  -> 对中间结果A进行Function 得到 R
 */
public class MyListCollector<T> implements Collector<T, List<T> , List<T>> {


    private boolean isIdentityFinished;

    public MyListCollector() {

    }

    public MyListCollector(boolean isIdentityFinished) {
        this.isIdentityFinished = isIdentityFinished;
    }

    /**
     * 保存中间结果的容器。
     * 中间结果是一个容器即可.
     * @return
     */
    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }


    /**
     * 中间结果不断对stream中的元素进行accumulator，结果保存在Supplier返回的容器中container
     * 这里是保存在list中.
     * BiConsumer::accept(container, elem)
     * @return
     */
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add;
    }

    /**
     * 并行处理的时候会有多个分组。
     * 每个分组都会单独计算，有自己单独的中间结果容器。combiner()把多个container容器融合成一个。
     * @return
     */
    @Override
    public BinaryOperator<List<T>> combiner() {
        return ( l1,  l2) -> {
            l1.addAll(l2);
            return l1;
        };
    }

    /**
     * 根据中间结果得到最终的结果
     * 这里是中间结果就是需要的最终结果
     * @return
     */
    @Override
    public Function<List<T>, List<T>> finisher() {
        if (isIdentityFinished) {
            throw new UnsupportedOperationException("Characteristics包括IDENTITY_FINISH,不需要调用finisher");
        }
        return Function.identity();
    }

    /**
     * 返回collector的一些特性，利用它能获取更好的性能。
     * CONCURRENT: 并发的，这种情况下不需要调用combiner
     * UNORDERED:  不需要保持stream中elem进入的顺序
     * IDENTITY_FINISH: 说明finisher()是Function.identity()。 可以从A cast to R. finisher()不需要调用
     * @return
     */
    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> set = new HashSet<>();
        if (isIdentityFinished) {
            set.add(Characteristics.IDENTITY_FINISH);
        }
        return Collections.unmodifiableSet(set);
    }
}
