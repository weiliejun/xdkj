layui.use(['form', 'layer', 'jquery'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer;
    form.on('submit(add)', function (data) {
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/sysmanager/addorupdate'),
            type: 'post',
            async: false,
            data: data.field,
            success: function (data) {
                ajaxReturnData = data;
            }
        });
        //结果回应
        if (ajaxReturnData.flag == 'true') {
            top.layer.msg('保存成功', {icon: 1});
            //先得到当前iframe层的索引
            var index = top.layer.getFrameIndex(window.name);
            parent.layer.close(index); //再执行关闭
            //刷新父页面
            parent.location.reload();
        } else {
            $("#token").val(ajaxReturnData.token);
            top.layer.msg(ajaxReturnData.msg, {icon: 5});
        }
        return false;
    });
});