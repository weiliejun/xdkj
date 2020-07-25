layui.use(['form', 'layer', 'jquery'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer;
    form.on('submit(addSysFunction)', function (data) {
        var parentCode = $("#parentCodeLabel").text();
        $("#parentCode").val(parentCode);

        var id = $("#idLabel").text();
        $("#id").val(id);
        $.ajax({
            url: PageContext.getUrl('/sysfunction/addorupdate'),
            type: 'post',
            async: false,
            data: $('#dataForm').serialize(),
            error: function () {
                layer.msg('系统繁忙，请稍后重试', {icon: 5});
            },
            success: function (data) {
                //结果回应
                if (data.flag == 'true') {
                    layer.msg('保存成功', {icon: 1, time: 1000}, function () {
                        //先得到当前iframe层的索引
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index); //再执行关闭
                        //刷新父页面
                        parent.location.reload();
                    });
                } else {
                    layer.msg(data.msg, {icon: 5});
                }
            }
        });

        return false;
    });


    $("#choseIcon").click(function () {
        var index = top.layer.open({
            type: 2,
            area: ['95%', '95%'],
            content: PageContext.getUrl('/sysfunction/systemSetting/icons'),
            shadeClose: true,
            cancel: function (index) {//关闭后自动复制
                var body = top.layer.getChildFrame('body', index);
                var icon = body.find("#copyText").text();
                $("#icon").val(icon);

            }
        });
        return false;
    });
});