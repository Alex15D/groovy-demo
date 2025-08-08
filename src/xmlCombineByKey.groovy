import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlParser
import groovy.xml.XmlUtil

class xmlCombineByKey {
    static void main(String[] args) {
        // 输入的XML字符串，模拟从某个来源读取的原始数据
        String body = '''<?xml version="1.0" encoding="UTF-8"?>
<document>
  <Line>
    <EmployeeID>100</EmployeeID>
    <EmployeeCompanyCode>CC01</EmployeeCompanyCode>
    <TransactionCurrency>USD</TransactionCurrency>
    <PostedDate>2025-08-05</PostedDate>
    <TransactionDate>2025-08-04</TransactionDate>
    <EmployeeFirstName>John</EmployeeFirstName>
    <EmployeeLastName>Doe</EmployeeLastName>
    <PostedAmount>100.00</PostedAmount>
    <NetAmount>90.00</NetAmount>
    <GrossAmount>110.00</GrossAmount>
    <TaxAmount>10.00</TaxAmount>
    <GLAccount>4000</GLAccount>
  </Line>
  <Line>
    <EmployeeID>100</EmployeeID>
    <EmployeeCompanyCode>CC01</EmployeeCompanyCode>
    <TransactionCurrency>USD</TransactionCurrency>
    <PostedDate>2025-08-20</PostedDate>
    <TransactionDate>2025-08-18</TransactionDate>
    <EmployeeFirstName>John</EmployeeFirstName>
    <EmployeeLastName>Doe</EmployeeLastName>
    <PostedAmount>50.00</PostedAmount>
    <NetAmount>45.00</NetAmount>
    <GrossAmount>55.00</GrossAmount>
    <TaxAmount>5.00</TaxAmount>
    <GLAccount>4000</GLAccount>
  </Line>
  <Line>
    <EmployeeID>100</EmployeeID>
    <EmployeeCompanyCode>CC01</EmployeeCompanyCode>
    <TransactionCurrency>USD</TransactionCurrency>
    <PostedDate>2025-09-01</PostedDate>
    <TransactionDate>2025-08-31</TransactionDate>
    <EmployeeFirstName>John</EmployeeFirstName>
    <EmployeeLastName>Doe</EmployeeLastName>
    <PostedAmount>200.00</PostedAmount>
    <NetAmount>180.00</NetAmount>
    <GrossAmount>220.00</GrossAmount>
    <TaxAmount>20.00</TaxAmount>
    <GLAccount>4000</GLAccount>
  </Line>
  <Line>
    <EmployeeID>200</EmployeeID>
    <EmployeeCompanyCode>CC01</EmployeeCompanyCode>
    <TransactionCurrency>USD</TransactionCurrency>
    <PostedDate>2025-08-10</PostedDate>
    <TransactionDate>2025-08-09</TransactionDate>
    <EmployeeFirstName>Alice</EmployeeFirstName>
    <EmployeeLastName>Smith</EmployeeLastName>
    <PostedAmount>120.00</PostedAmount>
    <NetAmount>110.00</NetAmount>
    <GrossAmount>130.00</GrossAmount>
    <TaxAmount>10.00</TaxAmount>
    <GLAccount>4100</GLAccount>
  </Line>
  <Line>
    <EmployeeID>100</EmployeeID>
    <EmployeeCompanyCode>CC02</EmployeeCompanyCode>
    <TransactionCurrency>USD</TransactionCurrency>
    <PostedDate>2025-08-15</PostedDate>
    <TransactionDate>2025-08-14</TransactionDate>
    <EmployeeFirstName>John</EmployeeFirstName>
    <EmployeeLastName>Doe</EmployeeLastName>
    <PostedAmount>75.00</PostedAmount>
    <NetAmount>68.00</NetAmount>
    <GrossAmount>80.00</GrossAmount>
    <TaxAmount>7.00</TaxAmount>
    <GLAccount>4200</GLAccount>
  </Line>
</document>
'''

        // 使用XmlParser将字符串形式的XML解析为Node结构，方便Groovy遍历处理
        def xml = new XmlParser().parseText(body)

        // 定义一个Map，键为String，值为Node列表，withDefault保证如果访问不存在的key时自动创建空列表
        Map<String, List<Node>> map = [:].withDefault { [] }

        // 遍历所有Line节点，按特定字段拼接的key对Line进行分组
        xml.Line.each { line ->
            // 从当前Line节点获取PostedDate子节点的文本内容
            // 使用安全导航操作符 ?. 来避免PostedDate节点不存在时抛出空指针异常
            // 如果PostedDate节点不存在或文本为空，则使用Elvis操作符 ?: 返回空字符串""，确保postedDateText不为null
            def postedDateText = line.PostedDate?.text() ?: ""
            // 截取PostedDate的年月部分作为月份，防止字符串长度不足做兼容处理
            def postedMonth = postedDateText.length() >= 7 ? postedDateText[0..6] : postedDateText
            // 拼接分组key：EmployeeID|EmployeeCompanyCode|TransactionCurrency|PostedMonth
            def key = "${line.EmployeeID?.text() ?: ""}|${line.EmployeeCompanyCode?.text() ?: ""}|${line.TransactionCurrency?.text() ?: ""}|${postedMonth}"
            // 将当前line节点添加进对应key的列表
            map[key] << line
        }

        // 使用StreamingMarkupBuilder构建新的XML结构
        def builder = new StreamingMarkupBuilder()
        def outXml = builder.bind {
            // 根元素GroupedTransactions，附加一个属性"xmlCombineByKeyAttr"
            "GroupedTransactions"(attr: "xmlCombineByKeyAttr") {
                // 遍历map中每个key和对应的Line列表
                map.each { key, list ->
                    "Group" {
                        // 解析key字符串拆分，依次输出分组字段作为元素
                        "EmployeeID"(key.split("\\|")[0])
                        "CompanyCode"(key.split("\\|")[1])
                        "Currency"(key.split("\\|")[2])
                        "PostedMonth"(key.split("\\|")[3])

                        // Transactions元素包裹当前组的所有Line明细
                        "Transactions" {
                            // 遍历分组内的每个Line节点，依次生成Line子节点
                            list.each { line ->
                                "Line" {
                                    // 使用安全导航操作符 ?. 来避免节点不存在时抛出空指针异常
                                    // 如果节点不存在或文本为空，则使用Elvis操作符 ?: 返回空字符串""，确保节点值不为null
                                    "EmployeeFirstName"(line.EmployeeFirstName?.text() ?: "")
                                    "EmployeeLastName"(line.EmployeeLastName?.text() ?: "")
                                    "PaymentType"(line.PaymentType?.text() ?: "")
                                    "ApprovalStatus"(line.ApprovalStatus?.text() ?: "")
                                    "PostedDate"(line.PostedDate?.text() ?: "")
                                    "TransactionDate"(line.TransactionDate?.text() ?: "")
                                    "PostedAmount"(line.PostedAmount?.text() ?: "")
                                    "NetAmount"(line.NetAmount?.text() ?: "")
                                    "GrossAmount"(line.GrossAmount?.text() ?: "")
                                    "TaxAmount"(line.TaxAmount?.text() ?: "")
                                    "EmployeeCostObject"(line.EmployeeCostObject?.text() ?: "")
                                    "GLAccount"(line.GLAccount?.text() ?: "")
                                    "TaxCode"(line.TaxCode?.text() ?: "")
                                    "TotalAmount"(line.TotalAmount?.text() ?: "")
                                    "CardProvider"(line.CardProvider?.text() ?: "")
                                    "StatementDate"(line.StatementDate?.text() ?: "")
                                    "Year"(line.Year?.text() ?: "")
                                    "BusinessPurpose"(line.BusinessPurpose?.text() ?: "")
                                    "Debit"(line.Debit?.text() ?: "")
                                    "Credit"(line.Credit?.text() ?: "")
                                    "Currency"(line.TransactionCurrency?.text() ?: "")
                                    "EmployeeID"(line.EmployeeID?.text() ?: "")
                                    "CompanyCode"(line.EmployeeCompanyCode?.text() ?: "")
                                }
                            }
                        }
                    }
                }
            }
        }

        // 将构建的XML结构序列化为字符串，去除空白行后打印输出
        println(XmlUtil.serialize(outXml).readLines().findAll { it.trim() }.join("\n"))
    }
}
