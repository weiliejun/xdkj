layui.use(['layer', 'form', 'table', 'laydate'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;
    laydate = layui.laydate;

    var cols = [[
        {
            field: 'SERVICE_NAME',
            width: 220,
            title: '服务名称',
            align: 'center',
            sort: true
        }, {
            field: 'USER_NAME',
            width: 220,
            title: '用户名',
            align: 'center'
        }, {
            field: 'MOBILE',
            width: 255,
            title: '手机号',
            align: 'center'
        }, {
            field: 'EXPENSE_TYPE',
            width: 200,
            title: '消费类型',
            align: 'center',
            templet: '#EXPENSE_TYPE',
        }, {
            field: 'EXPENSE_AMOUNT',
            width: 150,
            title: '消费金额',
            align: 'center',
        },
        {
            field: 'CREATE_TIME',
            width: 250,
            title: '消费时间',
            align: 'center',
            templet: '#createTime',
            sort: true
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#userExpensebar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#userExpenseTables', '/userExpense/list', cols, table);

    //监听工具条
    table.on('tool(userExpenseTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "修改消费",
                type: 2,
                skin: '',
                offset: ['85px', '530px'],
                area: ['540px', '520px'],
                content: PageContext.getUrl("/userExpense/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    console.log(data);
                    if (data) {
                        body.find("#id").val(data.ID);
                        body.find("#userName").val(data.USER_NAME);
                        body.find("#serviceName").val(data.SERVICE_NAME);
                        body.find("#expenseAmount").val(data.EXPENSE_AMOUNT);
                        //消费类型赋值
                        body.find("input[name=expenseType]").each(function (i, item) {
                            if ($(item).val() == data.EXPENSE_TYPE) {
                                $(item).prop('checked', true);
                            }
                        });
                        //时间类型的转换
                        var date = new Date(data.CREATE_TIME);
                        var dateStr = date.Format("yyyy-MM-dd hh:mm:ss");
                        body.find("#time").val(dateStr);
                        form.render();
                    }
                }
            });
        } else if (obj.event === 'disable') {//禁用
            layer.confirm('真的禁用消费么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/userExpense/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('userExpenseTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'enable') {//启用
            layer.confirm('真的将该消费置为可用么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/userExpense/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('userExpenseTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除该消费么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/userExpense/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('userExpenseTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('删除失败', {icon: 5});
                }
                layer.close(index);
            });
        }
    });

    //按钮事件监听
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //按钮事件定义
    var active = {
        search: function () {
            Common.searchTable('searchForm', initTable);
        },
        searchFormClear: function () {
            Common.searchTableClear('searchForm');
        },
        add: function () {
            Common.openFrame("/userExpense/toadd", "新增消费记录", '680px', '550px');
        }
    };


});