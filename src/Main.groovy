static void main(String[] args) {
    def str1 = '单引号字符串'
    def str2 = "双引号字符串"
    def name = 'Alex'
    def str3 = "Hello, ${name}"  // GString（支持变量插值）

//    def s = "SAP CPI"
//    println s.size()                   // 长度：7
//    println s.toUpperCase()           // 全大写
//    println s.toLowerCase()           // 全小写
//    println s[0]                       // 第一个字符：'S'
//    println s[-1]                      // 最后一个字符：'I'
//    println s.substring(0, 3)          // 截取前3个字符：'SAP'
//    println s.contains("CPI")          // 是否包含子串：true

    def list = [10, 20, 30, 'Groovy']
//    list.add(40)
//    list << 50              // 推荐写法（更简洁）
//    println list[0]         // 第一个元素：10
//    println list[-1]        // 最后一个元素：50
//    list[1] = 99            // 把第二个元素 20 改成 99
//    list.remove(0)          // 删除第一个元素（10）
//    list.remove('Groovy')   // 删除指定值
//
    list.add("x")
    list << "X"
    list.remove("x")
    list[0] = 00
    println list[0]
    list.eachWithIndex { val, i -> println "${i}: ${val}" }  // 带索引
    list.each { println it }     // 简洁写法


//    def map = [name: 'Alex', age: 30] //等价于：def map = ['name': 'Alex', 'age': 30]
//
//    map['city'] = 'Singapore'   // 添加
//    map.age = 31                // 修改 age
//
//    println map['name']         // Alex
//    println map.city            // Singapore
//    map.remove('age')
//    map.each { key, value -> println "$key = $value" }
    def map = [name: 'Alex', age: 30] //等价于：def map = ['name': 'Alex', 'age': 30]
    map.each {
        println(it)}//222Branch

//    map.each {k, v -> $k=1 $v= 2}
}