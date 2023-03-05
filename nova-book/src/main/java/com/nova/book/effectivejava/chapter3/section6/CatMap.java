package com.nova.book.effectivejava.chapter3.section6;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @description: 我们可以同时应用接口和抽象类，这个可以参考jdk的相关设计
 * 对于Map的通用实现，提供了AbstractMap来实现（比如size isEmpty），对于特定的实现类，则各自完成，比如HashMap和TreeMap
 * 通过这样的设计，接口的实现者可以选择继承 AbstractXxx 类，来简化部分功能，只关注未实现的方法.也可以只参考而不继承，
 * 独立完成实现类，更加灵活高效，并且，＂基本实现类＂（AbstractX（xx）还能一定程度上弥补接口的更新功能，
 * 比如我们设计了一个接口 Mapper，它声明了selectById updateById deleteById 三个方法，
 * 然后项目中有几百个Mapper实现了这个功能。后来，我们发现需要增加新的方法，如果只用接口，那么我们在接口上增加对应方法声明后，
 * 需要每个实现类都要修改，新增这个方法，或者我们选择破坏接口完整性，将新增的方法声明为另外的接口（这样显然会导致接口设计丑陋复杂）
 * 怎么处理这种问题？我们可以设计一个BaseMapper ，作为抽象类实现Mapper，其他的实现类可以继承BaseMapper，在需要新增一个方法的时候，
 * 只要在BaseMapper 中添加一个基本的默认实现，就能避免修改几百个问题
 *
 * 补充说明：虽然这种方法是一种解决方案，但还是建议，不是特殊情况，尽量避免接口在广泛使用后进行修改，最好在设计之时认真考虑情况需要哪些功能，
 * 也可以考虑版本化接口方案或者接口继承初始版本： public interface IMapper 更新方式1：public interface IMapper2，在接口被使用的位置，
 * 提供重载方法，并行提供 IMapper2 的方法更新方式2： public interface IMapper2 extends IMapper：因为是子类，
 * 原来使用 IMapper 的地方都可以直接用 IMapper2 代 替，用到新方法的，再补充 IMapper2 的相关处理的各自实现
 * @author: wzh
 * @date: 2023/2/22 12:32
 */
class CatMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Map.super.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Map.super.replaceAll(function);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        return Map.super.replace(key, value);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return Map.super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Map.super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Map.super.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

    @Override
    public CatMap<K, V> clone() {
        try {
            return (CatMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
