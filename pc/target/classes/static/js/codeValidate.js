/*********  图形验证码 start **************/
function loadVerifyCodeImage() {
    $(".verifyCodeImage").attr("src","/user/get/verifycodeImg?"+ Math.random());
    $(".verifyCodeImage").show();
}

$(".verifyCodeImage").click(function() {
    loadVerifyCodeImage();
});

/*********  图形验证码 end ***********/
