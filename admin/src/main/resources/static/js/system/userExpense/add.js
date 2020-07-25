layui.use(['form', 'layer', 'jquery', 'tableSelect', 'laydate'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer,
        tableSelect = layui.tableSelect,
        laydate = layui.laydate;


    // 表单校验
    form.verify({
        isDecimal: function (value, item) {
            return isDecimal(value, item);
        }
    })


    form.on('submit(add)', function (data) {
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/userExpense/addorupdate'),
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
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index); //再执行关闭
            //刷新父页面
            parent.location.reload();
        } else {
            top.layer.msg(ajaxReturnData.msg, {icon: 5});
        }
        return false;
    });

    //用户名的下拉框选择
    tableSelect.render({
        elem: '#userName',
        checkedKey: 'userName',
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
                {field: 'userName', title: '姓名', align: 'center'},
                {field: 'mobile', title: '手机号', align: 'center'}
            ]]
        },
        done: function (elem, data) {
            var NEWJSON = []
            layui.each(data.data, function (index, item) {
                NEWJSON.push(item.userName);
                $("#userId").val(item.id);//给userId赋值
            })
            elem.val(NEWJSON.join(","))
        }
    })


    //服务名称的下拉框选择
    tableSelect.render({
        elem: '#serviceName',
        checkedKey: 'serviceName',
        searchKey: 'serviceName',
        table: {
            method: 'POST',
            url: PageContext.getUrl('/userExpense/toChooseServiceInfo/list'),
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
                {field: 'serviceName', title: '服务名称', align: 'center'},
                {field: 'serviceCode', title: '服务Code', align: 'center'}
            ]]
        },
        done: function (elem, data) {
            var NEWJSON = []
            layui.each(data.data, function (index, item) {
                NEWJSON.push(item.serviceName);
                $("#serviceCode").val(item.serviceCode);//给serviceCode赋值
            })
            elem.val(NEWJSON.join(","))
        }
    })

    //时间选择器控件，获取基本的时间
    laydate.render({
        elem: '#time', //指定创建时间元素
        type: 'datetime',//日期时间选择器 	可选择：年、月、日、时、分、秒
        range: false,//设置 true，将默认采用 “ - ” 分割
        format: 'yyyy-MM-dd HH:mm:ss', //可任意组合,时间选择格式
        value: new Date(),//时间默认当前时间
        isInitValue: false,
        lang: 'cn',//语言
        calendar: true,//显示公立
        change: function (value, date) {//监听日期被切换
            lay('#createTime').html(new Date(value));
        }
    });

})