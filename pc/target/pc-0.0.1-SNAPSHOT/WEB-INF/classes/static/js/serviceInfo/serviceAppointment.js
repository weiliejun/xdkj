/******************** 文档下载 start  ***********************/
/********* form校验（账户中心：PAAS-免费试用， IAAS-咨询，定制研发-需求描述 ）***** start ***********/
var formValidate = $("#appointmentForm").validate({
    rules : {
        consultDesc : {
            required : true,
            minlength : 1,
            maxlength : 500
        }
    },
    messages : {
        consultDesc : {
            required : "内容不能为空！",
            minlength : "长度在1-500个字符之间",
            maxlength : "长度在1-500个字符之间"
        }
    },
});
//  提交
$("#butSubmit").click(function() {
    var serviceType = $("#serviceType").val();
    var url =  pageContext.contextPath +"/appointment/consult/"+serviceType+"/submit";
    if (formValidate.form()) {
        $("#butSubmit").addClass("disabled");
        jQuery.ajax({
            type : "POST",
            url:url,
            data : $("#appointmentForm").serialize(),
            success : function(data) {
                if(data.flag =="true"){
                    GlobalPrompt.prompt("您的申请已提交，非常感谢您的支持。");
                   $("#consultDesc").val("");
                }else if(data.flag =="false"){
                    GlobalPrompt.prompt(data.msg);
                }
            },
            error : function(e) {
                GlobalPrompt.prompt('抱歉，程序访问出现错误！请联系管理员.');
            }
        });
           $("#butSubmit").removeClass("disabled");
    }
});

/******************** end  ***********************/

