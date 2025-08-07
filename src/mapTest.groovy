class mapTest {

    static void main(String[] args) {
        // 创建 Map
        def book = [
                title : "Groovy in Action",
                author: "Paul King",
                year  : 2019
        ]

        println "--- 初始 Map ---"
        println book

// 查
        println "\n--- 查找 ---"
        println "Title: ${book.title}"
        println "Author: ${book['author']}"

// 改
        println "\n--- 修改 ---"
        book.year = 2021
        book['author'] = "Alex"//第二种写法
        println book

// 增
        println "\n--- 添加 ---"
        book.publisher = "Manning"
        book['pages'] = 500//第二种写法
        println book

// 删
        println "\n--- 删除 ---"
        book.remove('year')
        book.remove('publisher')
        println book

// 遍历
        println "\n--- 遍历 ---"
        book.each { key, value ->
            println "$key : $value"
        }

// 判断 key 是否存在
        println "\n--- 判断键是否存在 ---"
        println "是否有 title: ${book.containsKey('title')}"
        println "是否有 year: ${book.containsKey('year')}"

    }

}
