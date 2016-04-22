通用服务 如 手机归属地 文件操作等
=============文件操作接口=============
1、http://common-service.inner.eakay.cn/file/uploads
method:post
param:
    uploadfiles     file    必填  支持多个
    biz             int     必填  业务方唯一标识：1-order，2-mall，3-customer
    key             int     必填  业务方biz下的 关键字 如：biz为1 key为2 表示order业务方车辆相关图片
    keyId           int     必填  图片外部id  如：biz=1 key=2 那么keyId就是order业务方车辆图片对应的车辆ID

成功返回值：httpStatus:201
    [
      {
        "success": true,
        "errorCode": null,
        "errorMsg": null,
        "fileDO": {
          "id": 20,
          "createTime": {
            "centuryOfEra": 20,
            "yearOfEra": 2016,
            "yearOfCentury": 16,
            "weekyear": 2016,
            "monthOfYear": 4,
            "weekOfWeekyear": 16,
            "hourOfDay": 11,
            "minuteOfHour": 32,
            "secondOfMinute": 36,
            "millisOfSecond": 313,
            "secondOfDay": 41556,
            "millisOfDay": 41556313,
            "minuteOfDay": 692,
            "year": 2016,
            "dayOfMonth": 22,
            "dayOfWeek": 5,
            "era": 1,
            "dayOfYear": 113,
            "chronology": {
              "zone": {
                "fixed": false,
                "uncachedZone": {
                  "fixed": false,
                  "cachable": true,
                  "id": "Asia/Shanghai"
                },
                "id": "Asia/Shanghai"
              }
            },
            "millis": 1461295956313,
            "zone": {
              "fixed": false,
              "uncachedZone": {
                "fixed": false,
                "cachable": true,
                "id": "Asia/Shanghai"
              },
              "id": "Asia/Shanghai"
            },
            "afterNow": false,
            "beforeNow": true,
            "equalNow": false
          },
          "updateTime": {
            "centuryOfEra": 20,
            "yearOfEra": 2016,
            "yearOfCentury": 16,
            "weekyear": 2016,
            "monthOfYear": 4,
            "weekOfWeekyear": 16,
            "hourOfDay": 11,
            "minuteOfHour": 32,
            "secondOfMinute": 36,
            "millisOfSecond": 313,
            "secondOfDay": 41556,
            "millisOfDay": 41556313,
            "minuteOfDay": 692,
            "year": 2016,
            "dayOfMonth": 22,
            "dayOfWeek": 5,
            "era": 1,
            "dayOfYear": 113,
            "chronology": {
              "zone": {
                "fixed": false,
                "uncachedZone": {
                  "fixed": false,
                  "cachable": true,
                  "id": "Asia/Shanghai"
                },
                "id": "Asia/Shanghai"
              }
            },
            "millis": 1461295956313,
            "zone": {
              "fixed": false,
              "uncachedZone": {
                "fixed": false,
                "cachable": true,
                "id": "Asia/Shanghai"
              },
              "id": "Asia/Shanghai"
            },
            "afterNow": false,
            "beforeNow": true,
            "equalNow": false
          },
          "sourceIpAddr": "123.56.44.79",
          "fileSize": 39408,
          "crc32": 0,
          "name": "2CE6EF7086CC.png",
          "groupName": "IDCARD",
          "remoteFileName": "M00/00/66/ezgsT1cZm1OAWwy8AACZ8ASiEgA9448680",
          "biz": 1,
          "key": 1,
          "keyId": 1213123123
        }
      }
    ]
失败返回值：httpStatus:500
    [
      {
        "success": true,
        "errorCode": null,
        "errorMsg": null,
        "fileDO": {
          "id": 20,
          "createTime": {
            "centuryOfEra": 20,
            "yearOfEra": 2016,
            "yearOfCentury": 16,
            "weekyear": 2016,
            "monthOfYear": 4,
            "weekOfWeekyear": 16,
            "hourOfDay": 11,
            "minuteOfHour": 29,
            "secondOfMinute": 30,
            "millisOfSecond": 719,
            "secondOfDay": 41370,
            "millisOfDay": 41370719,
            "minuteOfDay": 689,
            "year": 2016,
            "dayOfMonth": 22,
            "dayOfWeek": 5,
            "era": 1,
            "dayOfYear": 113,
            "chronology": {
              "zone": {
                "fixed": false,
                "uncachedZone": {
                  "fixed": false,
                  "cachable": true,
                  "id": "Asia/Shanghai"
                },
                "id": "Asia/Shanghai"
              }
            },
            "millis": 1461295770719,
            "zone": {
              "fixed": false,
              "uncachedZone": {
                "fixed": false,
                "cachable": true,
                "id": "Asia/Shanghai"
              },
              "id": "Asia/Shanghai"
            },
            "afterNow": false,
            "beforeNow": true,
            "equalNow": false
          },
          "updateTime": {
            "centuryOfEra": 20,
            "yearOfEra": 2016,
            "yearOfCentury": 16,
            "weekyear": 2016,
            "monthOfYear": 4,
            "weekOfWeekyear": 16,
            "hourOfDay": 11,
            "minuteOfHour": 29,
            "secondOfMinute": 30,
            "millisOfSecond": 719,
            "secondOfDay": 41370,
            "millisOfDay": 41370719,
            "minuteOfDay": 689,
            "year": 2016,
            "dayOfMonth": 22,
            "dayOfWeek": 5,
            "era": 1,
            "dayOfYear": 113,
            "chronology": {
              "zone": {
                "fixed": false,
                "uncachedZone": {
                  "fixed": false,
                  "cachable": true,
                  "id": "Asia/Shanghai"
                },
                "id": "Asia/Shanghai"
              }
            },
            "millis": 1461295770719,
            "zone": {
              "fixed": false,
              "uncachedZone": {
                "fixed": false,
                "cachable": true,
                "id": "Asia/Shanghai"
              },
              "id": "Asia/Shanghai"
            },
            "afterNow": false,
            "beforeNow": true,
            "equalNow": false
          },
          "sourceIpAddr": "123.56.44.79",
          "fileSize": 39408,
          "crc32": 0,
          "name": "2CE6EF7086CC.png",
          "groupName": "IDCARD",
          "remoteFileName": "M00/00/66/Zch5C1cZmpqAR1cDAACZ8ASiEgA2420825",
          "biz": 1,
          "key": 1,
          "keyId": 1213123123
        }
      },
      {
        "success": false,
        "errorCode": "001",
        "errorMsg": "空文件错误。",
        "fileDO": null
      }
    ]
2、http://common-service.inner.eakay.cn/file/fetch/1/1/123456789
  method:get
  param:
      {biz}     int    必填  业务方唯一标识：1-order，2-mall，3-customer
      {key}     int    必填  业务方biz下的 关键字 如：biz为1 key为2 表示order业务方车辆相关图片
      {keyId}   int    必填  图片外部id  如：biz=1 key=2 那么keyIid就是order业务方车辆图片对应的车辆ID

  成功返回值：httpStatus:200
      {
        "success": true,
        "errorCode": null,
        "errorMsg": null,
        "fileDO": {
          "id": 19,
          "createTime": {
            "centuryOfEra": 20,
            "yearOfEra": 2016,
            "yearOfCentury": 16,
            "weekyear": 2016,
            "monthOfYear": 4,
            "weekOfWeekyear": 16,
            "hourOfDay": 11,
            "minuteOfHour": 34,
            "secondOfMinute": 10,
            "millisOfSecond": 63,
            "secondOfDay": 41650,
            "millisOfDay": 41650063,
            "minuteOfDay": 694,
            "year": 2016,
            "dayOfMonth": 22,
            "dayOfWeek": 5,
            "era": 1,
            "dayOfYear": 113,
            "chronology": {
              "zone": {
                "fixed": false,
                "uncachedZone": {
                  "fixed": false,
                  "cachable": true,
                  "id": "Asia/Shanghai"
                },
                "id": "Asia/Shanghai"
              }
            },
            "millis": 1461296050063,
            "zone": {
              "fixed": false,
              "uncachedZone": {
                "fixed": false,
                "cachable": true,
                "id": "Asia/Shanghai"
              },
              "id": "Asia/Shanghai"
            },
            "afterNow": false,
            "beforeNow": true,
            "equalNow": false
          },
          "updateTime": {
            "centuryOfEra": 20,
            "yearOfEra": 2016,
            "yearOfCentury": 16,
            "weekyear": 2016,
            "monthOfYear": 4,
            "weekOfWeekyear": 16,
            "hourOfDay": 11,
            "minuteOfHour": 34,
            "secondOfMinute": 10,
            "millisOfSecond": 63,
            "secondOfDay": 41650,
            "millisOfDay": 41650063,
            "minuteOfDay": 694,
            "year": 2016,
            "dayOfMonth": 22,
            "dayOfWeek": 5,
            "era": 1,
            "dayOfYear": 113,
            "chronology": {
              "zone": {
                "fixed": false,
                "uncachedZone": {
                  "fixed": false,
                  "cachable": true,
                  "id": "Asia/Shanghai"
                },
                "id": "Asia/Shanghai"
              }
            },
            "millis": 1461296050063,
            "zone": {
              "fixed": false,
              "uncachedZone": {
                "fixed": false,
                "cachable": true,
                "id": "Asia/Shanghai"
              },
              "id": "Asia/Shanghai"
            },
            "afterNow": false,
            "beforeNow": true,
            "equalNow": false
          },
          "sourceIpAddr": "123.56.44.79",
          "fileSize": 39408,
          "crc32": 0,
          "name": "2CE6EF7086CC.png",
          "groupName": "IDCARD",
          "remoteFileName": "M00/00/56/Zch5C1cYhRuAWn3CAACZ8ASiEgA2660252",
          "biz": 1,
          "key": 1,
          "keyId": 123456789
        }
      }
  失败返回值：httpStatus:500
      {
        "success": false,
        "errorCode": "010",
        "errorMsg": "本地库里不存在该文件或文件已被删除。",
        "fileDO": null
      }

3、http://common-service.inner.eakay.cn/file/26
  method:delete
  param:
      {id}     int    必填  文件唯一ID

  成功返回值：httpStatus:204
  失败返回值：httpStatus:500
      {
        "success": false,
        "errorCode": "010",
        "errorMsg": "本地库里不存在该文件或文件已被删除。",
        "fileDO": null
      }



