/**
 * 项目路径 contextPath 路径
 */
var PageContext = {
    contextPath : ''
};


/**
 * 按钮禁用、启用
 */
var Button = {
    disabled:function(btnId){
        $('#'+btnId).attr("disabled","disabled").css("background","#999");
    },
    enabled:function(btnId){
        $('#'+btnId).removeAttr("disabled").css("background","#07b");
    }
};

/**
 * ajax 请求封装
 */
var AJAX = {};
AJAX.bashPath = "";
//设置ajax当前状态(是否可以发送);
AJAX.ajaxStatus = true;
//是否使用遮罩
AJAX.maskStatus = false;
// ajax封装
AJAX.ajax = function (url, data, success, cache, alone, async, type, dataType, error) {
    var type = type || 'post';//请求类型
    var dataType = dataType || 'json';//接收数据类型
    var async = async && true;//异步请求
    var alone = alone || false;//独立提交（一次有效的提交）
    var cache = cache || false;//浏览器历史缓存
    var success = success || function (data) {
        /*console.log('请求成功');*/
        setTimeout(function () {
            layer.msg(data.msg);//通过layer插件来进行提示信息
        },500);
        if(data.status){//服务器处理成功
            setTimeout(function () {
                if(data.url){
                    location.replace(data.url);
                }else{
                    location.reload(true);
                }
            },1500);
        }else{//服务器处理失败
            if(alone){//改变ajax提交状态
                AJAX.ajaxStatus = true;
            }
        }
    };
    var error = error || function (data) {
        setTimeout(function () {
            if(data.status == 404){
                //请求失败，请求未找到
            }else if(data.status == 503){
                //请求失败，服务器内部错误
            }else {
                //请求失败,网络连接超时
            }
            AJAX.ajaxStatus = true;
        },500);
    };
    /*判断是否可以发送请求*/
    if(!AJAX.ajaxStatus){
        return false;
    }
    AJAX.ajaxStatus = false;//禁用ajax请求
    /*正常情况下1秒后可以再次多个异步请求，为true时只可以有一次有效请求（例如添加数据）*/
    if(!alone){
        setTimeout(function () {
            AJAX.ajaxStatus = true;
        },1000);
    }
    $.ajax({
        'url': url,
        'data': data,
        'type': type,
        'dataType': dataType,
        'async': async,
        'success': success,
        'error': error,
        'jsonpCallback': 'jsonp' + (new Date()).valueOf().toString().substr(-4),
        'beforeSend': function () {
            //显示遮罩
            if(AJAX.maskStatus){
                Mask.show();
            }
            //禁用按钮
            if($(".btnEnabled").length > 0){
                $(".btnEnabled").addClass("aDisabledBtn");
            }

        },
        'complete': function () {
            //启用按钮
            if($(".btnEnabled").length > 0){
                $(".btnEnabled").removeClass("aDisabledBtn");
            }
            //关闭遮罩
            if(AJAX.maskStatus){
                Mask.hide();
            }
            //重新设置不适用遮罩
            AJAX.maskStatus = false;
        }
    });
};

/**
 * submitAjax(post方式提交)
 * 调用实例
 $(function () {
        $('#form-login').submit(function () {
            submitAjax('#form-login');
            return false;
        });
      });
 */
AJAX.submitAjax = function (form, success, cache, alone) {
    cache = cache || true;
    var form = $(form);
    var url = form.attr('action');
    var data = form.serialize();
    AJAX.ajax(url, data, success, cache, alone, true, 'post','json');
};

// ajax提交(post方式提交)
AJAX.post = function (url, data, success) {
    AJAX.ajax(url, data, success, false, false, true, 'post','json');
};

// ajax提交(get方式提交)
AJAX.get = function(url, success) {
    AJAX.ajax(url, {}, success, false,false, true, 'get','json');
};

// jsonp跨域请求(get方式提交)
AJAX.jsonp = function(url, success, cache, alone) {
    AJAX.ajax(url, {}, success, cache, alone, true, 'get','jsonp');
};

//获取验证码60秒倒计时
function commonRemoteValidateCode(obj){
    var countdown=60;
    settime(obj);
    function settime(obj) {
        if (countdown == 0) {
            $(obj).removeClass("aDisabledBtn");
            $(obj).text("重新获取验证码");
            countdown = 60;
            return;
        } else {
            $(obj).addClass("aDisabledBtn");
            $(obj).text("(" + countdown + ") s 重新发送");
            countdown--;
        }
        setTimeout(function() {
                settime(obj) }
            ,1000)
    }
}

/**
 * 校验手机号是否合法
 * @param mobile
 * @returns {boolean}
 */
function isMobile(mobile){
    var reg = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(17[0-9]{1})|(18[0-9]{1})|(19[0-9]{1}))+\d{8})$/;
    if (mobile == null) {
        return false;
    }
    return reg.test(mobile);
}




