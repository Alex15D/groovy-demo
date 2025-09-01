import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil

static void main(String[] args) {
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
    def xml = new XmlSlurper().parseText(body)
    Map map = [:].withDefault { [] }
    String lineKey = ""
    xml.Line.each { line ->
        String PostedDateText = line.PostedDate?.text() ?: ""
        String PostedMonth = PostedDateText.length() >= 7 ? PostedDateText[0..6] : PostedDateText
        lineKey = "${line.EmployeeID?.text() ?: ""}|${line.EmployeeCompanyCode?.text() ?: ""}|${line.TransactionCurrency?.text() ?: ""}|${PostedMonth}"
        map[lineKey] << line
    }
    def outXml = new StreamingMarkupBuilder().bind {
        "GroupedTransactions"(attr: "xmlCombineByKeyAttr") {
            map.each { key, list ->
                "Group" {
                    "EmployeeID"(key.split("\\|")[0])
                    "CompanyCode"(key.split("\\|")[1])
                    "Currency"(key.split("\\|")[2])
                    "PostedMonth"(key.split("\\|")[3])
                    "Transactions" {
                        list.each { line ->
                            "Line" {
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
    println(XmlUtil.serialize(outXml).readLines().findAll { it.trim() }.join("\n"))


}