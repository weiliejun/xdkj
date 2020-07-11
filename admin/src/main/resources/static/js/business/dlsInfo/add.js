layui.use(['form', 'upload', 'layer', 'laytpl', 'jquery'], function () {

    var $ = layui.$,
        form = layui.form,
        upload = layui.upload,
        laytpl = layui.laytpl,
        layer = layui.layer;

    /*$(function () {
        $.ajax({
            url: PageContext.getUrl("/dlsInfo/detail/" + $("#id").val()),
            type: 'post',
            async: false,
            success: function (data) {
                // alert(data)
                // console.log(data);
                if (data.flag === "true") {
                    var getTpl = document.getElementById('detailView').innerHTML
                        , view = document.getElementById('view');
                    laytpl(getTpl).render(data.data, function (html) {
                        view.innerHTML = html;
                    });

                }

            }
        });
    });*/

    form.on('submit(save)', function (data) {alert("333");
        console.log(data.field);
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/dlsInfo/addOrUpdate'),
            contentType: "application/json",
            type: 'post',
            async: false,
            data: JSON.stringify(data.field),
            success: function (data) {
                ajaxReturnData = data;
            }
        });
        //结果回应
        if (ajaxReturnData.flag == 'true') {
            top.layer.msg('保存成功', {icon: 1});
            // 保存成功后禁用掉保存按钮
            $('#saveButton').addClass('layui-btn-disabled').attr('disabled', "true");
            console.log(PageContext.getUrl("/dlsInfo/list"));
            window.location.href = PageContext.getUrl("/dlsInfo/list");
            //先得到当前iframe层的索引
            //var index = parent.layer.getFrameIndex(window.name);
            //parent.layer.close(index); //再执行关闭
            //刷新父页面
            //parent.location.reload();
        } else {
            top.layer.msg(ajaxReturnData.msg, {icon: 5});
        }
        return false;
    });

});