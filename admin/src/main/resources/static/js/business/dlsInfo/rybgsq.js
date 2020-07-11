layui.use(['form', 'upload', 'layer', 'laytpl', 'jquery'], function () {

    var $ = layui.$,
        form = layui.form,
        upload = layui.upload,
        laytpl = layui.laytpl,
        layer = layui.layer;

    $(function () {
        $.ajax({
            url: PageContext.getUrl("/dlsInfo/detail/" + $("#ryId").val()),
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
    });

    form.on('submit(save)', function (data) {
        console.log(data.field);
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/rybgsq/addOrUpdate'),
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

    window.selectZhrCallback = function (selectZhr) {
        var zhrId = '';
        var zhrName = '';
        for (var p in selectZhr) {
            zhrId += selectZhr[p].id + ',';
            zhrName += selectZhr[p].name + ',';
        }
        $("#zhrId").val(zhrId);
        $("#zhrName").val(zhrName);
        $("#zhr").val(JSON.stringify(selectZhr));
    }
    window.selectSprCallback = function (selectSpr) {
        var sprId = '';
        var sprName = '';
        for (var p in selectSpr) {
            sprId += selectSpr[p].id + ',';
            sprName += selectSpr[p].name + ',';
        }
        $("#sprId").val(sprId);
        $("#sprName").val(sprName);
        $("#spr").val(JSON.stringify(selectSpr));
    }
    //按钮事件监听
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //按钮事件定义
    var active = {
        selectSpr: function () {
            Common.openFrame("/dlsInfo/selectList?cxmk=selectSpr&ryid=" + $("#sprId").val(), "选择审批人", '1000px', '600px');
        },
        selectZhr: function () {
            Common.openFrame("/dlsInfo/selectList?cxmk=selectZhr&ryid=" + $("#zhrId").val(), "选择知会人", '1000px', '600px');
        },
        add: function () {
            window.location.href = PageContext.getUrl("/xmrwxx/toAdd" + "?id=" + $("#id").val() + "&xmId=" + $("#xmId").val() + "&xmjd=" + $("#xmjd").val());
        },
        goBack: function () {
            window.location.href = PageContext.getUrl("/xmxxgl/get/xmgl/" + $("#xmId").val() + "?xmjd=" + $("#xmjd").val());
        }
    };
});