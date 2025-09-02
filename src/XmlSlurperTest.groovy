import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil

class XmlSlurperTest {
    static void main(String[] args) {
        String body = '''<Order>
  <OrderID>ABC123</OrderID>
</Order>'''
        def xml = new XmlSlurper().parseText(body)
        xml.appendNode{ "NewField"("12345")}
        def orderId = xml.OrderID?.text()


        println(XmlUtil.serialize(xml).readLines().findAll { it.trim() }.join("\n"))




    }
}
