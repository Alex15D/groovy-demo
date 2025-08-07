import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class JsonTest {
    static void main(String[] args) {

        def json = '''
{
  "books": {
    "book": [
      {
        "title": "Groovy in Action",
        "author": "Paul King",
        "year": 2019
      },
      {
        "title": "Learning Groovy",
        "author": "Adam Davis",
        "year": 2021
      },
      {
        "title": "Effective Java",
        "author": "Joshua Bloch",
        "year": 2018
      },
      {
        "title": "Clean Code",
        "author": "Robert C. Martin",
        "year": 2008
      },
      {
        "title": "Java Concurrency in Practice",
        "author": "Brian Goetz",
        "year": 2006
      }
    ]
  }
}

'''
        def data  = new JsonSlurper().parseText(json)


        //查询（Read） 打印所有书名和作者：
        data.books.book.each { book ->
            println "${book.title} by ${book.author} in ${book.year}"
        }


        //新增（Create） 向书列表中新增一本书：
        data.books.book << [
                title: "Mastering Groovy",
                author: "Jane Doe",
                year: 2025
        ]
        //尾部新增空book
        data.books.book << [:]
        //在第4和第五个book之间新增空book
        data.books.book.add(4, [:])
        //在第4个book增加新键值对
        data.books.book[4].test = "test"


        //修改（Update） 找到 "Clean Code" 这本书，修改作者名：
        def cleanCodeBook = data.books.book.find { it.title == "Clean Code" }
//        if (cleanCodeBook) {
//            cleanCodeBook.author = "Updated Author"
//        }
        //?.是 Groovy 的“安全导航操作符（Safe Navigation Operator）.
        // 它的作用：只在 cleanCodeBook 不为 null 时才去设置 author，否则什么也不做，避免抛出 NullPointerException。
        cleanCodeBook?.author = "Updated Author"
        cleanCodeBook?.test = "test"//Key存在则修改，不存在则新增




        //删除（Delete）删除出版年份为 2006 的书：
        data.books.book.removeAll { it.year == 2006 }
        //删除第五个book的year 键 year
        //删除（Delete）book[5]的Key为year的键值对
        data.books.book[5].remove("year")


        //最后，可以把修改后的数据转换回 JSON 字符串
        def updatedJson = JsonOutput.prettyPrint(JsonOutput.toJson(data))
        println updatedJson


        println(data.books.book.author.getClass())//ArrayList
        println(data.books.book.year.getClass())//ArrayList

        println(data.books.book.year[0])
        println(data.books.book.author[0])

        List list =["1","2","3"]//String的ArrayList
        println(list.getClass())

    }
}
