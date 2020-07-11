function isEmail(email){
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	if (email == null) {
		return false;
	}
	return reg.test(email);
}

function getLength(str) { 
    var len = str.length; 
    var reLen = 0; 
    for (var i = 0; i < len; i++) {        
        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) { 
            // 全角
            reLen += 2; 
        } else { 
            reLen++; 
        } 
    } 
    return reLen;    
}

// 检查身份证是否是正确格式
function checkCard(cId) {
	var pattern;
	if (cId.length == 15) {
		pattern = /^\d{15}$/;// 正则表达式,15位且全是数字
		if (pattern.exec(cId) == null) {
			return false;
		}
		if (!isdate("19" + cId.substring(6, 8), cId.substring(8, 10), cId
				.substring(10, 12))) {
			return false;
		}
	} else if (cId.length == 18) {
		pattern = /^\d{17}(\d|x|X)$/;// 正则表达式,18位且前17位全是数字，最后一位只能数字,x,X
		if (pattern.exec(cId) == null) {
			return false;
		}
		if (!isdate(cId.substring(6, 10), cId.substring(10, 12), cId.substring(
				12, 14))) {
			return false;
		}
		var strJiaoYan = [ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3",
				"2" ];
		var intQuan = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];
		var intTemp = 0;
		for (var i = 0; i < cId.length - 1; i++)
			intTemp += cId.substring(i, i + 1) * intQuan[i];
		intTemp %= 11;
		if (cId.substring(cId.length - 1, cId.length).toUpperCase() != strJiaoYan[intTemp]) {
			return false;
		}
	} else {
		return false;
	}
	return true;
}

// 检查年月日是否是合法日期
function isdate(intYear, intMonth, intDay) {
	if (isNaN(intYear) || isNaN(intMonth) || isNaN(intDay))
		return false;
	if (intMonth > 12 || intMonth < 1)
		return false;
	if (intDay < 1 || intDay > 31)
		return false;
	if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11)
			&& (intDay > 30))
		return false;
	if (intMonth == 2) {
		if (intDay > 29)
			return false;
		if ((((intYear % 100 == 0) && (intYear % 400 != 0)) || (intYear % 4 != 0))
				&& (intDay > 28))
			return false;
	}
	return true;
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
Date.prototype.Format = function (fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"H+": this.getHours(), //小时
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}