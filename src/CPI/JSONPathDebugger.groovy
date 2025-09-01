package CPI

import groovy.json.JsonSlurper

class JSONPathDebugger {
    static void main(String[] args) {
        // 模拟 JSON 消息体
        String jsonBody = '''
        {
            "users": [
                {"id": "100", "name": "Alex", "role": "Admin"},
                {"id": "200", "name": "John", "role": "User"}
            ]
        }
        '''

        // 批量 JSONPath 测试列表
        def jsonPathList = [
                "\$.users[?(@.id=='100')].name",
                "\$.users[?(@.role=='User')].name",
                "\$.users.length()"
        ]

        def jsonSlurper = new JsonSlurper()
        def json = jsonSlurper.parseText(jsonBody)

        jsonPathList.eachWithIndex { path, idx ->
            def result = evaluateJsonPath(json, path)
            println "[${idx+1}] JSONPath: ${path}"
            println "    结果: ${result}"
        }
    }

    // 简单 JSONPath 解析器
    static def evaluateJsonPath(def json, String path) {
        try {
            if(path == '$.users.length()') return json.users.size()
            def matcher = path =~ /\$\.\w+\[(\d+)\]/
            if(matcher.matches()) {
                int idx = matcher[0][1] as int
                return json.users[idx]
            }
            if(path =~ /\$\.\w+\[\?\(@\.(\w+)=='(\w+)'\)\]\.(\w+)/) {
                def m = (path =~ /\$\.\w+\[\?\(@\.(\w+)=='(\w+)'\)\]\.(\w+)/)[0]
                def key = m[1]; def val = m[2]; def returnField = m[3]
                def match = json.users.find { it[key] == val }
                return match ? match[returnField] : null
            }
            return null
        } catch(Exception e) {
            return "解析失败: ${e.message}"
        }
    }
}
