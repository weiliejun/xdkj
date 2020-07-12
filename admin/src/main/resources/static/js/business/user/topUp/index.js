layui.use(['form', 'layer', 'jquery', 'tableSelect'], function () {
    var $ = layui.$,
        form = layui.form,
        tableSelect = layui.tableSelect;

    // 表单校验
    form.verify({
        isDecimal: function (value, item) {
            return isDecimal(value, item);
        }
    })

    form.on('submit(add)', function (data) {
        $.ajax({
            url: PageContext.getUrl('/userTopUp'),
            type: 'post',
            async: false,
            data: data.field,
            success: function (data) {
                if (data.flag == "true") {
                    top.layer.msg('保存成功', {icon: 1});
                } else {
                    top.layer.msg(data.msg, {icon: 5});
                }
                //重新加载当前页面
                location.reload();
            }
        });
        return false;
    });

    //用户名的下拉框选择
    tableSelect.render({
        elem: '#nickName',
        checkedKey: 'nickName',
        searchKey: 'mobile',
        table: {
            method: 'POST',
            url: PageContext.getUrl('/userExpense/tochooseUser/list'),
            request: {
                pageName: 'currentPage'
                , limitName: 'pageSize'
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
                {field: 'nickName', title: '姓名', align: 'center'},
                {field: 'mobile', title: '手机号', align: 'center'}
            ]]
        },
        done: function (elem, data) {
            var NEWJSON = []
            layui.each(data.data, function (index, item) {
                NEWJSON.push(item.nickName);
                $("#userId").val(item.id);//给userId赋值
            })
            elem.val(NEWJSON.join(","))
        }
    });

});