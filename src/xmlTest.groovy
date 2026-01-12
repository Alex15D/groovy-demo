import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil

class xmlTest {
    static void main(String[] args) {

        def xml = '''
<books>
    <book title="Groovy in Action">
        <author>Paul King</author>
        <year>2019</year>
    </book>
    <book title="Learning Groovy">
        <author>Adam Davis</author>
        <year>2021</year>
    </book>
    <book title="Effective Java">
        <author>Joshua Bloch</author>
        <year>2018</year>
    </book>
    <book title="Clean Code">
        <author>Robert C. Martin</author>
        <year>2008</year>
    </book>
    <book title="Java Concurrency in Practice">
        <author>Brian Goetz</author>
        <year>2006</year>
    </book>
</books>

'''

        def root = new XmlSlurper().parseText(xml)
//        def firstBook = root.book[0]
//        println(firstBook.name())
//        firstBook.attributes()["title"] = "9999999999"
//        println(firstBook.attributes()["title"])
//        println firstBook.attributes().getClass()
//        println root.book[0].author[0].getClass()
//        println root.book[0].author[0].text()
//        root.book[0].year[0].value = "test"
//        println root.book[0].year[0].text()
//        println root.book[0].year[0].value.getClass()

        // 1️⃣ 查：打印所有 book 的 title 和 author
        println "All books:"
        root.book.each { book ->
            println "- ${book.@title} by ${book.author.text()}"//book.@title = book.attributes()["title"]
        }
        //在 Groovy 中，双引号字符串支持GString 插值（变量插入），也就是可以用 ${} 或 $variable 直接把变量的值嵌入字符串里。
        root.book.each { it ->
            println("${it.year.text()}")
        }

        //1️⃣ 查：在root.book集合中查找第一个<book>元素，其子元素<year>的文本值是"2008"的那一本，并将它的第一个<author>元素的值改为"author"。
        root.book.find {
            it ->
                it.year[0].text() == "2008"

        }.author[0].value = "2008author"
        //1️⃣ 查：在一个 XML 结构中，找到 title 属性为 "Clean Code" 的 <book> 节点，然后把该书的第一个 <author> 子节点的值改成 "2008Clean Code"。
        root.book.find {
            it ->
                it.@title == "Clean Code"

        }.author[0].value = "2008Clean Code"


// 2️⃣ 改：修改第 1 本书的 title 属性 Map
        root.book[0].attributes().title = 'Groovy in Depth' //=root.book[0].attributes()['title'] = 'Groovy in Depth'


// 3️⃣ 改：修改第 2 本书的 author 节点内容 ArrayList
        root.book[1].author[0].value = 'Alex Smith'

// 4️⃣ 增：添加一本新书
        def newBook = new Node(root, 'book', [title: 'Mastering Groovy'])
        new Node(newBook, 'author', 'Jane Doe')
        new Node(newBook, 'year', '2025')
        // 4️⃣ 增：添加一本新书带属性
        def newBook1 = new Node(root.book[3] as Node, "newNode", [attribute: "attribute"], "value")


// 5️⃣ 删：删除第 5 本书
        root.remove(root.book[4])
        // 5️⃣ 删：从第 5 本书（root.book[4]）中删除它的第一个 <year> 子节点。
        root.book[4].remove(root.book[4].year[0])


// 6️⃣ 打印最终 XML
        println "\nUpdated XML:"
        println("\n")
//        def xmlBack = XmlUtil.serialize(root)
        println XmlUtil.serialize(root).readLines().findAll { it.trim() }.join("\n")



    }

}
