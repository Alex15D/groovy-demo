package CPI

import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class JSONDataTypeConverter {
    static void main(String[] args) {
        String body = '''{
  "header": {
    "shipperName": "FNT",
    "loadPortCode": "NZAKL",
    "bookingReference": "SHIPPINGLINEBOOKINGREF",
    "pointOfOriginCode": "AKC",
    "vessel": {
      "shipName": "CMA CGM SEMARANG",
      "voyageNumber": "2416",
      "partnerPortShippingReference": "CMC9741"
    },
    "loadPortFacility": "Auckland",
    "lineOperatorCode": "ARMC",
    "portOfDischarge": "AUMEL",
    "foreignPortOfDischarge": "AUMEL",
    "userReference": "UNIQUEREFERENCE",
    "notificationEmails": [
      "info@portconnect.co.nz"
    ]
  },
  "containers": [
    {
      "containerNumber": "HAZD1234510",
      "isoTypeCode": "4500",
      "isFull": "true",
      "commodityCode": "74",
      "cargoWeightKg": "16000.0",
      "totalWeightKg": "18000.0",
      "hazardous": [
        {
          "medicalFirstAidGuideSet": "true",
          "flashPointSet": "true",
          "hazardousClass": "2.1",
          "unNumber": "1950",
          "packagingGroup": "1",
          "limitedQuantities": "true",
          "marinePollutant": "true",
          "hazardousWeight": "200.0",
          "quantity": "10",
          "emsCode": "F-BS-C",
          "hazardContact": {
            "name": "John Smith",
            "phone": "021 588 998"
          }
        },
        {
          "medicalFirstAidGuideSet": "true",
          "flashPointSet": "true",
          "hazardousClass": "2.2",
          "unNumber": "1950",
          "packagingGroup": "0",
          "limitedQuantities": "false",
          "marinePollutant": "false",
          "hazardousWeight": "1000.0",
          "quantity": "75",
          "emsCode": "F-YS-Y",
          "hazardContact": {
            "name": "John Smith",
            "phone": "021 588 998"
          }
        }
      ],
      "containerSeals": [
        {
          "sealType": "Shipper",
          "sealCode": "4455"
        }
      ],
      "arrivalCarrierType": "Rail",
      "carrier": "CONLIN"
    },
    {
      "containerNumber": "OOGC1234651",
      "attachedContainerNumbers": [
        "ADCU1234510"
      ],
      "isoTypeCode": "22P1",
      "isFull": "true",
      "commodityCode": "47",
      "isNonOperatingReefer": "false",
      "cargoWeightKg": "16000.0",
      "totalWeightKg": "18000.0",
      "overGauge": [
        {
          "area": "Top",
          "measureCm": "10.0"
        },
        {
          "area": "Left",
          "measureCm": "20.0"
        },
        {
          "area": "Right",
          "measureCm": "30.0"
        },
        {
          "area": "Front",
          "measureCm": "40.0"
        },
        {
          "area": "Back",
          "measureCm": "50.0"
        }
      ],
      "containerSeals": [
        {
          "sealType": "Shipper",
          "sealCode": "TBC"
        }
      ],
      "arrivalCarrierType": "Truck",
      "carrier": "AACAR "
    },
    {
      "containerNumber": "REEF1234510",
      "isoTypeCode": "2230",
      "isFull": "true",
      "commodityCode": "2199",
      "isNonOperatingReefer": "false",
      "refrigeration": {
        "isFantainer": "false",
        "co2Percent": "50.0",
        "o2Percent": "40.0",
        "maximumOffPowerHours": "24.0",
        "offPowerTemperature": "-21.0",
        "offPowerTimestamp": "2024-09-11T10:00:00+12:00",
        "onPowerTargetTime": "2024-09-11T10:00:00+12:00",
        "requiredTemperature": "-21.0",
        "humidityPercent": "20.0",
        "refrigerationType": "Chilled"
      },
      "vent": {
        "ventSettingType": "PercentageOpen",
        "ventSetting": "30.0"
      },
      "cargoWeightKg": "16000.0",
      "totalWeightKg": "18000.0",
      "containerSeals": [
        {
          "sealType": "Shipper",
          "sealCode": "1223"
        },
        {
          "sealType": "Shipper",
          "sealCode": "5665"
        }
      ],
      "arrivalCarrierType": "Truck",
      "carrier": "WALTER"
    }
  ]
}
'''
        def json  = new JsonSlurper().parseText(body)
        // Process containers
        // 3. Process containers
        json.containers?.each { c ->

            // Container level Boolean
            convertBoolean(c, "flexiTank")
            convertBoolean(c, "isFull")
            convertBoolean(c, "isNonOperatingReefer")

            // Container level Decimal
            convertDecimal(c, "cargoWeightKg")
            convertDecimal(c, "totalWeightKg")

            // Latest submission status
            if (c.latestSubmissionStatus instanceof Map) {
                convertBoolean(c.latestSubmissionStatus, "success")
            }

            // Refrigeration section
            if (c.refrigeration instanceof Map) {

                def r = c.refrigeration

                convertBoolean(r, "isFantainer")
                convertBoolean(r, "activeRefrigerationRequired")

                convertDecimal(r, "co2Percent")
                convertDecimal(r, "o2Percent")
                convertDecimal(r, "maximumOffPowerHours")
                convertDecimal(r, "offPowerTemperature")
                convertDecimal(r, "requiredTemperature")
                convertDecimal(r, "humidityPercent")

                convertInteger(r, "timeAllowedOffPowerHours")
                convertInteger(r, "timeAllowedOffPowerMinutes")
            }

            // Vent section
            if (c.vent instanceof Map) {
                convertDecimal(c.vent, "ventSetting")
            }

            // Hazardous section
            c.hazardous?.each { h ->
                convertDecimal(h, "flashPoint")
                convertDecimal(h, "hazardousWeight")

                convertBoolean(h, "medicalFirstAidGuideSet")
                convertBoolean(h, "flashPointSet")
                convertBoolean(h, "limitedQuantities")
                convertBoolean(h, "marinePollutant")
            }

            // OverGauge section
            c.overGauge?.each { o ->
                convertDecimal(o, "measureCm")
            }
        }
        // Convert back to JSON
        def newBody = JsonOutput.prettyPrint(JsonOutput.toJson(json))
        println newBody

    }

    /* ---------- Converter Methods ---------- */

// Convert to Boolean only if key exists
    static void convertBoolean(Map map, String key) {
        if (map?.containsKey(key) && map[key] != null) {
            map[key] = map[key].toBoolean()
        }
    }

// Convert to BigDecimal only if key exists
    static void convertDecimal(Map map, String key) {
        if (map?.containsKey(key) && map[key] != null) {
            map[key] = map[key].toBigDecimal()
        }
    }

// Convert to Integer only if key exists
    static void convertInteger(Map map, String key) {
        if (map?.containsKey(key) && map[key] != null) {
            map[key] = map[key].toInteger()
        }
    }

}


