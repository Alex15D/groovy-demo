import groovy.xml.XmlSlurper
import javax.xml.xpath.XPathFactory

class XPathDebugger {
    static void main(String[] args) {
        // 模拟 CPI 消息体 (XML)
        String xmlBody = '''
        <root>
            <user id="100">
                <name>Alex</name>
                <role>Admin</role>
            </user>
            <user id="200">
                <name>John</name>
                <role>User</role>
            </user>
        </root>
        '''

        // 批量 XPath 测试列表
        def xpathList = [
                "//user[@id='100']/name/text()",
                "//user[@id='200']/role/text()",
                "count(//user)"
        ]

        def xpath = XPathFactory.newInstance().newXPath()
        xpathList.eachWithIndex { expr, idx ->
            def result = xpath.evaluate(expr, new org.xml.sax.InputSource(new StringReader(xmlBody)))
            println "[${idx+1}] XPath: ${expr}"
            println "    结果: ${result}"
        }
    }
}
