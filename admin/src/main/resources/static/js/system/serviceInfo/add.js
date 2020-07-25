layui.use(['form', 'layer', 'jquery', 'tableSelect'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer,
        tableSelect = layui.tableSelect;

    form.on('submit(add)', function (data) {
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/serviceInfo/addorupdate'),
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
        elem: '#serviceCode',
        checkedKey: 'id',
        searchKey: 'name',
        table: {
            method: 'POST',
            url: PageContext.getUrl('/serviceInfo/tableSelectAll/list'),
            request: {
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
                NEWJSON.push(item.code);
                $("#serviceCode").val(item.code);
                $("#serviceName").val(item.name);
            })
            elem.val(NEWJSON.join(","));
        }
    })
});