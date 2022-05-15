# 											Java JDK8 Stream API

## 1、什么是Stream？

流(Stream) 是数据渠道，用于操作数据源（集合、数组等）所生成的元素序列。 **“集合讲的是数据，流讲的是计算”**

**流(Stream)处理的特性：** 
① Stream 自己不会存储元素。 
② Stream 不会改变源对象。相反，他们会返回一个持有结果的新Stream。 
③ Stream 操作是延迟执行的。这意味着他们会等到需要结果的时候才执行。

## 2、Stream的操作三个步骤

① **创建 Stream**
	一个数据源（如：集合、数组），获取一个流
② **中间操作**
	一个中间操作链，对数据源的数据进行处理
③  **终止操作**
	一个终止操作，执行中间操作链，并产生结果

![image-20220503225948285](C:\Users\rongyong\AppData\Roaming\Typora\typora-user-images\image-20220503225948285.png)



中间操作又可以分为无状态（Stateless）与有状态（Stateful）操作，
	无状态：元素的处理不受之前元素的影响，
	有状态：该操作只有拿到所有元素之后才能继续下去。
终结操作又可以分为短路（Short-circuiting）与非短路（Unshort-circuiting）操作，
	短路：遇到某些符合条件的元素就可以得到最终结果，
	非短路：必须处理完所有元素才能得到最终结果。
我们通常还会将中间操作称为懒操作，也正是由这种懒操作结合终结操作、数据源构成的处理管道（Pipeline），实现了 Stream 的高效。

![up-5b49df1799136150b992f140c169f604508.png](https://oscimg.oschina.net/oscnet/up-5b49df1799136150b992f140c169f604508.png)

## 3、创建Stream流

通过Collection系列集合提供的Stream()串行流或parallelStream()并行流。
其中，Java8中的Collection接口扩展了，提供了两个获取流的方法：

```java
// 顺序流	
default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

// 并行流
default Stream<E> parallelStream() {
    return StreamSupport.stream(spliterator(), true);
}
```


### 3.1 数组创建流

数组流： 

```java
static <T> Stream<T> stream(T[] array); // 源码
// 例子
Employee[] employees = new Employee[10];
Stream<Employee> stream2 = Arrays.stream(employees);
```

**重载形式，能够处理对应基本类型的数组：** 

```java
public static IntStream stream(int[] array);
public static LongStream stream(long[] array);
public static DoubleStream stream(double[] array);
```

### 3.2 值创建流

```java
// 返回一个流
public static<T> Stream<T> of(T... values);
```

### 3.3 创建无限流(特殊)

可以使用静态方法 Stream.iterate() 和Stream.generate(), 创建无限流。
一般创建无线流，我们都会设置一个终止操作限制

```java
// 迭代
public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f);
// 初始值是 0，新值是前一个元素值 + 2
Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);

// 生成
public static<T> Stream<T> generate(Supplier<T> s);
// 接受一个 Supplier 作为参数
Stream.generate(Math::random).limit(10).forEach(System.out::println);
```

### 3.4 创建空流 
在使用流参数调用方法时，空流可能有助于避免 NullPointerException
```java
Stream<String> stream = Stream.empty();
```

## 4、API的使用

Stream + Lambda的组合就是为了让 Java 语句更像查询语句，取代繁杂的 for if 

### 4.1、遍历（foreach）

```java
// 主要区别在并行处理上，forEach是并行处理的，forEachOrder是按顺序处理的，显然前者速度更
stream.forEach(System.out::println);
stream.forEachOrder(System.out::println);
```

### 4.2、收集（collect）

```java
// collect：收集器。将流转换为其他形式 ： list、set、map
Set<Integer> collect = integers.stream().collect(Collectors.toSet());
Map<Integer, Integer> map = integers.stream().collect(Collectors.toMap(Integer::intValue, val -> val));
List<String> colors = appleStore.stream().map(Apple::getColor).collect(Collectors.toList());
```

### 4.3、过滤（filter)

```java
// 筛选重量大于等于200的苹果
List<Apple> collect = appleStore.stream().filter(apple -> apple.getWeight() >= 200).collect(Collectors.toList());
```

### 4.4、去重  （distinct）

```java
// 简单去重
List<Integer> collect = integers.stream().distinct().collect(Collectors.toList());

// 利用TreeSet去重
ArrayList<Apple> collect = appleStore.stream().
    collect(Collectors.collectingAndThen(
        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Apple::getBirthplace))), ArrayList::new));

// 利用TreeSet，根据两个两个属性去重
ArrayList<Apple> distinctMap = appleStore.stream().
    collect(Collectors.collectingAndThen(
        Collectors.toCollection(() -> new TreeSet<>(
            Comparator.comparing(apple -> apple.getBirthplace() + "/" + apple.getColor()))), ArrayList::new));
```

### 4.5、限制（limit）

```java
// 获取三条数据
List<Integer> collect = integers.stream().limit(3).collect(Collectors.toList());
```

### 4.6、跳过（skip）

```java
// skip：获取流中除去前 n 个元素的其它所有元素
List<Integer> collect = integers.stream().skip(3).collect(Collectors.toList());
```

### 4.7、映射（map）

```java
// 获取苹果产地集合去重
List<String> collect = appleStore.stream().map(Apple::getBirthplace).distinct().collect(Collectors.toList());
```

### 4.8、扁平化（flatMap）

```java
String[] strs1 = new String[]{"1"};
String[] strs2 = new String[]{"2"};
String[] strs3 = new String[]{"3"};
Stream<String[]> stream1 = Stream.of(strs1, strs2, strs3);
List<String[]> collect1 = stream1.collect(Collectors.toList());// [["1"],["2"],["3"]]
// flatMap 即对流中每个元素进行平铺后，形成多个流合在一起
Stream<String[]> stream2 = Stream.of(strs1, strs2, strs3);
List<String> collect2 = stream2.flatMap(Arrays::stream).collect(Collectors.toList());// collect2结果["1","2","3"]
```

### 4.9、转换类型(mapToLong、mapToDouble、mapToInt)

```java
String[] strs1 = new String[]{"1","2","3","4"};
List<Double> collect = appleStore.stream().mapToDouble(Apple::getWeight).boxed().collect(Collectors.toList());
```

### 4.10、排序（sorted）

```java
// 单个属性-排序(升序)
List<Apple> collect = appleStore.stream().sorted(Comparator.comparing(Apple::getWeight)).collect(Collectors.toList());
// 多个属性-升序
List<Apple> collect = appleStore.stream()
    .sorted(Comparator.comparing(Apple::getWeight)
            .thenComparingInt(Apple::getId))
    .collect(Collectors.toList());
// 多个属性排序-降序（reversed）
List<Apple> collect2 = appleStore.stream()
    .sorted(Comparator.comparing(Apple::getWeight).reversed()
            .thenComparing(Apple::getId).reversed()
           )
    .collect(Collectors.toList());

```

### 4.12、任意匹配到一个元素（anyMatch）

```java
// 判断集合元素属性是否存等于xx
boolean match = appleStore.stream().anyMatch(apple -> "深圳".equals(apple.getBirthplace()));// 结果match = true
```

### 4.13、全部匹配（allMatch）

```java
// 判断集合元素某属性是全部等于xx
boolean match = appleStore.stream().allMatch(apple -> "深圳".equals(apple.getBirthplace()));// 结果match = false
```

### 4.14、是否没有匹配所有元素（noneMatch）

```java
// 结果
boolean match = appleStore.stream().noneMatch(apple -> "广州".equals(apple.getBirthplace()); // 结果match = false
boolean match = appleStore.stream().noneMatch(apple -> "北京".equals(apple.getBirthplace()); // 结果match = true
```

### 4.15、流中的任意元素（findAny）

```java
// 将返回集合中的首个匹配的元素
Apple findAny = appleStore.stream().filter(apple -> "广州".equals(apple.getBirthplace())).findAny().get();
```

### 4.16、返回集合第一个元素（findFirst）

```java
Apple apple = appleStore.stream().findFirst().get();
```

### 4.17、归一（reduce）

```java
// 简单：流中元素反复结合起来，得到一个值
 String str = appleStore.stream().map(Apple::getBirthplace).reduce(String::concat).get();
// 添加分隔符
String str = appleStore.stream().map(Apple::getBirthplace).reduce((v1, v2) -> v1 + "+" + v2).get(); //结果 广州+广州+深圳+广州+深圳+深圳+广州
// 去重，添加分隔符、归一
String str = appleStore.stream()
    .map(Apple::getBirthplace)
    .distinct()
    .reduce((v1,v2)->v1+"/"+v2).get(); // 结果 广州/深圳
```

### 4.18、集合中元素的数量（count）

```java
// 返回符合筛选条件结果数量
long count = appleStore.stream().filter(apple -> "广州".equals(apple.getBirthplace())).count();
```



## 5、使用场景

### 5.1、转化树形结构数据

```java
    @Test
    public void test25(){
        //获取父节点，0表示父节点
        List<Area> collect = areas.stream().filter(area -> area.getParentId()==0)
                .peek(area -> area.setChildren(getChildren(area,areas)))
                .collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(collect));
    }

    /**
     * 递归查询子节点
     * @param root 父节点
     * @param areas areas
     * @return 子节点集合
     */
    private List<Area> getChildren(Area root, List<Area> areas) {
        return areas.stream().filter(area -> area.getParentId() == root.getId()).peek(
                area -> area.setChildren(getChildren(area, areas))
        ).collect(Collectors.toList());
    }
```

### 5.2、list转Map

```java
// 以某个属性分组
Map<String, List<Apple>> collect = appleStore.stream().collect(Collectors.groupingBy(Apple::getBirthplace));
//给出key重复时，使用哪个key作为主键，以下代码中的(key1, key2) -> key2)代表key1和key2键重复时返回key2做主键
Map<Integer, Apple> map = appleStore.stream().collect(Collectors.toMap(Apple::getId,apple->apple, (key1, key2) -> key2));
```

### 5.2、分组

```java
// 简单分组
Map<String, List<Apple>> collect = appleStore.stream().collect(Collectors.groupingBy(Apple::getBirthplace));
// 分组，求和
Map<String, Integer> collect = appleStore.stream()
    .collect(Collectors.groupingBy(Apple::getBirthplace, Collectors.summingInt(Apple::getWeight)));
// 分组 ，求平均值
Map<String, Double> collect = appleStore.stream()
    .collect(Collectors.groupingBy(Apple::getBirthplace, Collectors.averagingDouble(Apple::getWeight)));
```

5.3、求最大值/最小值

```java
// 多属性最大值
Apple apple = appleStore.stream().max(
        Comparator.comparingInt(Apple::getWeight)
                .thenComparingInt(Apple::getId)).get();

// 多属性求最大值
Apple apple = appleStore.stream().max(
    Comparator.comparingInt(Apple::getWeight)
    .thenComparing(Apple::getId, (a1, a2) -> a1 > a2 ? a2 : a1)).get();

// 多属性求最小值
Apple apple = appleStore.stream().min(
    Comparator.comparingInt(Apple::getWeight)
    .thenComparing(Apple::getId, (a1, a2) -> a1 > a2 ? a2 : a1)).get();

```



好文推荐：JAVA进阶之Stream实现原理：
https://baijiahao.baidu.com/s?id=1709430137050419004&wfr=spider&for=pc