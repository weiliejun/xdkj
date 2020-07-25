layui.use(['form', 'layer', 'jquery', 'tableSelect'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer,
        tableSelect = layui.tableSelect;

    // (选择父级1时，隐藏类型下拉框/去掉必填 )
    form.on('select(grade)', function (data) {
        var selectVal = data.value;
        if (selectVal == '1') {
            $("#parentCodeDiv").removeAttr("lay-verify", "required");
            $('#parentCodeDiv').addClass("layui-hide");
        } else if (selectVal == '2') {
            $("#parentCodeDiv").attr("lay-verify");
            $('#parentCodeDiv').removeClass('layui-hide');
        }
        form.render();
    });

    form.on('submit(add)', function (data) {
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/sysdictionary/addorupdate'),
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

    //服务名称的下拉框选择
    tableSelect.render({
        elem: '#parentCode',
        checkedKey: 'id',
        searchKey: 'name',
        table: {
            method: 'POST',
            url: PageContext.getUrl('/sysdictionary/tableSelect/list'),
            request: {
                selectRange: 'parentScope',
                pageName: 'currentPage',
                limitName: 'pageSize'
            },
            response: { //定义后端 json 格式
                statusName: 'flag', //状态字段名称
                statusCode: 'true', //状态字段成功值
                msgName: 'msg', //消息字段
                countName: 'count', //总页数字段
                dataName: 'data' //数据字段
            },
            cols: [[
                {type: 'radio'},
                {field: 'name', title: '类型名称', align: 'center'},
                {field: 'code', title: '类型编号', align: 'center'}
            ]]
        },
        done: function (elem, data) {
            var NEWJSON = []
            layui.each(data.data, function (index, item) {
                NEWJSON.push(item.name);
                $("#parentCode").val(item.code);
            })
            elem.val(NEWJSON.join(","));
        }
    })
});