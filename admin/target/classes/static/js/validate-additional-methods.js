/**
 * @Description layui自定义表单校验，layui默认的校验有： required（必填项）、phone（手机号）、email（邮箱）、url（网址）、number（数字）
 *                                                       、date（日期）、identity（身份证）
 */

function checkUserName(value, item) { //value：表单的值、item：表单的DOM对象
    if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
        return '用户名不能有特殊字符';
    }
    if (/(^\_)|(\__)|(\_+$)/.test(value)) {
        return '用户名首尾不能出现下划线\'_\'';
    }
    if (/^\d+\d+\d$/.test(value)) {
        return '用户名不能全为数字';
    }
}

function checkPwd(value, item) { //value：表单的值、item：表单的DOM对象
    if(!/^[\@A-Za-z0-9_()\!\#\$\%\^\&\*\.\~]{6,16}$/.test(value)){
        return '密码不能有空格,长度在6-16个字符之间';
    }
}
// 金额校验，最多保留两位小数，值域为：0~999999999999
function isDecimal(value, item) {
    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
    if (Number(value) < 0) {
        return '请输入正确的金额，且至多两位小数';
    }
    if (Number(value) > 999999999999) {
        return '请输入正确的金额，且至多两位小数';
    }
    if(!reg.test(value)){
        return '请输入正确的金额，且至多两位小数';
    }
}