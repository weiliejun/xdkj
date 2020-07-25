layui.use(['form', 'layer', 'jquery'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer;

    // 表单校验
    form.verify({
        password: function (value, item) {
            return checkPwd(value, item);
        }
    })

    form.on('submit(add)', function (data) {
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/sysmanager/password/edit'),
            type: 'post',
            async: false,
            data: data.field,
            success: function (data) {
                ajaxReturnData = data;
            }
        });
        //结果回应
        if (ajaxReturnData.flag == 'true') {
            top.layer.msg("修改成功，请重新登录", {icon: 1}, function () {
                //先得到当前iframe层的索引
                var index = top.layer.getFrameIndex(window.name);
                top.layer.close(index); //再执行关闭
                //退出，重新登录
                top.window.location.href = PageContext.getUrl("/portal/logout");
            });

        } else {
            $("#token").val(ajaxReturnData.token);
            top.layer.msg(ajaxReturnData.msg, {icon: 5});
        }
        return false;
    });
});